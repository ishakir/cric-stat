package models.db.build

import anorm.SQL
import play.api.db.DB
import play.api.Play.current
import models.db.retrieve.DbRetrieveBattingRecord
import utils.db.InsertStatement
import play.api.Logger

class DbSaveBattingRecord(
  val battingPosition: Int,
  val runs: Int
) {
  
  def save(disId: Long): Long = {
    
    val initialStatement = new InsertStatement(DbRetrieveBattingRecord.tableName)
    val statement = initialStatement.addValue(DbRetrieveBattingRecord.runsName, runs.toString)
    val furtherStatement = {
      if(disId == DbRetrieveBattingRecord.nullInt) statement
      else statement.addValue(DbRetrieveBattingRecord.dismissalIdName, disId.toString)
    }
    
    val id = DB.withConnection { implicit connection =>
      SQL(furtherStatement.toString).executeInsert()
    }
    
    val idLong = id.get
    
    Logger.debug("Saved record for batsman no " + battingPosition + " who scored " + runs)
    Logger.debug("This was saved against the dismissalId " + disId)
    Logger.debug("The id of this batting record is " + idLong)
    
    idLong
  
  }
  
}

object DbSaveBattingRecord {
  
  /**
   * Saves all the batting records in the list, with the dismissal ids that go with that batting 
   * record. Returns a map from batting position (which references matchplayer) to the corresponding
   * batting record id
   */
  def saveList(batRecs: Seq[DbSaveBattingRecord], playerToDisId: Map[Int, Long]): Map[Int, Long] = {
      
      def saveAndProcess(batRec: DbSaveBattingRecord, acc: Map[Int, Long]): Map[Int, Long] = {
        
        def addBattingRecordToMap(acc: Map[Int, Long], playerPosition: Int, bowlRecId: Long): Map[Int, Long] = {
          acc ++ Map(playerPosition -> bowlRecId)
        }
        
        // Handle whether we have a dismissal Id
        val dismissalIdOption = playerToDisId.get(batRec.battingPosition)
        val dismissalId = {
          if(dismissalIdOption.isEmpty) DbRetrieveBattingRecord.nullInt
          else dismissalIdOption.get
        }
        
        val id = batRec.save(dismissalId)
        val position = batRec.battingPosition
        addBattingRecordToMap(acc, position, id)
      
      }
            
      batRecs.fold(Map()) {
        case (acc: Map[Int, Long], br: DbSaveBattingRecord) => saveAndProcess(br, acc)
      }.asInstanceOf[Map[Int, Long]]
      
    }
  
}