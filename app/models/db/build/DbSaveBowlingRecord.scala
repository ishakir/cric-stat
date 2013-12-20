package models.db.build

import anorm.SQL
import play.api.db.DB
import play.api.Play.current
import models.db.retrieve.DbRetrieveBowlingRecord
import utils.db.InsertStatement
import play.api.Logger

class DbSaveBowlingRecord(
  val battingPosition: Int,
  val fullovers: Int,
  val extraBalls: Int,
  val maidens: Int,
  val runs: Int,
  val wickets: Int
) {
  
  def save(): Long = {
    
    val initialStatement = new InsertStatement(DbRetrieveBowlingRecord.tableName)
    val statement = initialStatement.
      addValue(DbRetrieveBowlingRecord.oversName, fullovers.toString).
      addValue(DbRetrieveBowlingRecord.ballsName, extraBalls.toString).
      addValue(DbRetrieveBowlingRecord.maidensName, maidens.toString).
      addValue(DbRetrieveBowlingRecord.runsName, runs.toString).
      addValue(DbRetrieveBowlingRecord.wicketsName, wickets.toString)
      
    val id = DB.withConnection { implicit connection =>
      SQL(statement.toString).executeInsert()
    }
    
    val idLong = id.get
    
    Logger.debug("Saved bowling record for player no " + battingPosition + " with values: ")
    Logger.debug(fullovers + "." + extraBalls + "-" + maidens + "-" + runs + "-" + wickets)
    Logger.debug("This was saved with  id " + idLong)
    
    idLong
    
  }
  
}

object DbSaveBowlingRecord {
   
    /**
     * Important to document this of all things, this method will save all the bowling
     * records. What it will also do is return to you a map that tells you, for any given
     * batting position (as the players are labelled 1, .. , 11 for identification) what 
     * the id of his bowling position is
     */
   // Return is map from player position to BowlingRecord id
    def saveList(bowlRecs: Seq[DbSaveBowlingRecord]): Map[Int, Long] = {
      
      def saveAndProcess(bowlRec: DbSaveBowlingRecord, acc: Map[Int, Long]): Map[Int, Long] = {
        
        def addBowlingRecordToMap(acc: Map[Int, Long], playerPosition: Int, bowlRecId: Long): Map[Int, Long] = {
          acc ++ Map(playerPosition -> bowlRecId)
        }
        
        val id = bowlRec.save
        val position = bowlRec.battingPosition
        addBowlingRecordToMap(acc, position, id)
      
      }
            
      bowlRecs.fold(Map()) {
        case (acc: Map[Int, Long], br: DbSaveBowlingRecord) => saveAndProcess(br, acc)
      }.asInstanceOf[Map[Int, Long]]
      
    }
    
}