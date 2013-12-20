package models.db.build

import anorm.SQL
import play.api.db.DB
import play.api.Play.current
import models.db.retrieve.DbRetrieveDismissal
import models.DismissalType
import models.DismissalType.toInt
import utils.db.InsertStatement
import play.api.Logger

class DbSaveDismissal(
  val dismissalType: DismissalType,
  val bowlerPosition: Int,
  val batsmanPosition: Int,
  val fielderPosition: Int) {

  def save(bowlingRecordId: Long): Long = {
		  
    val initialStatement = new InsertStatement(DbRetrieveDismissal.tableName)
    val statement = initialStatement.
      addValue(DbRetrieveDismissal.dismissalTypeName, toInt(dismissalType).toString)

    val furtherStatement = {
      if (bowlerPosition == DbRetrieveDismissal.nullInt) statement
      else statement.addValue(DbRetrieveDismissal.bowlingRecordName, bowlingRecordId.toString)
    }

    val id = DB.withConnection { implicit connection =>
      SQL(furtherStatement.toString).executeInsert()
    }
    
    val idLong = id.get
    
    Logger.debug("Saved dismissal where batsman no " + batsmanPosition + " was " + dismissalType + " with bowler no " + bowlerPosition + " and fielder no " + fielderPosition)
    Logger.debug("This is saved against bowling record with id "+ bowlingRecordId)
    Logger.debug("The dismissal's id is " + idLong)
    
    idLong

  }

}

object DbSaveDismissal {

  /**
   * This one is even more annoying, as for a dismissal we can have both a fielder and a batsman AND
   * a fielder can be associated with more than one dismissal we return first a map from batting position
   * to a list of dismissalIds, which represent the dismissals any one fielder took part in, and we return
   * a map from batting position to dismissal id, which represents the dismissal associated with a batsman
   */
  def saveList(dismissals: Seq[DbSaveDismissal], playerToBowlRecIdMap: Map[Int, Long]): (Map[Int, Seq[Long]], Map[Int, Long]) = {

    def saveAndProcess(dis: DbSaveDismissal, acc: (Map[Int, Seq[Long]], Map[Int, Long])): (Map[Int, Seq[Long]], Map[Int, Long]) = {

      def addDismissalToFielderMap(acc: Map[Int, Seq[Long]], fielderPosition: Int, dismissalId: Long): Map[Int, Seq[Long]] = {

        // Have we seen this fielder before?
        if (acc.keySet.contains(fielderPosition)) {
          val oldList = acc.get(fielderPosition).get
          val newList = dismissalId :: oldList.toList
          acc.updated(fielderPosition, newList)
        } else {
          val newList = List(dismissalId)
          acc ++ Map(fielderPosition -> newList)
        }

      }

      def addDismissalToBatsmanMap(acc: Map[Int, Long], batsmanPosition: Int, dismissalId: Long): Map[Int, Long] = {

        acc ++ Map(batsmanPosition -> dismissalId)

      }

      // Do we have a bowler, if so save with it
      val brId = if (dis.bowlerPosition == DbRetrieveDismissal.nullInt) {
        DbRetrieveDismissal.nullInt
      } else {
        playerToBowlRecIdMap.get(dis.bowlerPosition).get
      }

      val disId = dis.save(brId)

      val fielderPosition = dis.fielderPosition
      val batsmanPosition = dis.batsmanPosition

      val (currentFielderMap, currentBatsmanMap) = acc

      // Do we have a fielder, if so add it to them map
      val newFielderMap = {
        if (fielderPosition == DbRetrieveDismissal.nullInt) {
          currentFielderMap
        } else {
          addDismissalToFielderMap(currentFielderMap, fielderPosition, disId)
        }
      }

      // We definitely have a batsman, so add it to the map
      val newBatsmanMap = addDismissalToBatsmanMap(currentBatsmanMap, batsmanPosition, disId)

      (newFielderMap, newBatsmanMap)

    }

    dismissals.fold((Map(), Map())) {
      case (acc: (Map[Int, Seq[Long]], Map[Int, Long]), dis: DbSaveDismissal) => saveAndProcess(dis, acc)
    }.asInstanceOf[(Map[Int, Seq[Long]], Map[Int, Long])]

  }

}