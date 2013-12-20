package models.db.build

import anorm.SQL
import play.api.db.DB
import play.api.Play.current
import models.db.retrieve.DbRetrievePlayer
import utils.db.InsertStatement
import utils.db.BooleanHandling
import play.api.Logger

class DbSavePlayer(
  val battingPosition: Int,
  val initial: String,
  val surname: String,
  val isHighfield: Boolean) {

  lazy val retrievedPlayer: DbRetrievePlayer = {
    if (!isHighfield) null
    else {
      val allPlayers = DbRetrievePlayer.findFilteredByEqualsAttributes(
        Map(
          DbRetrievePlayer.initial -> initial,
          DbRetrievePlayer.nameName -> surname
        )
      )
      
      Logger.debug("Looking for players with initial: " + initial + " and name " + surname)
      allPlayers.foreach(player =>
        Logger.debug("Found " + player.initial + ". " + player.surname)
      )
        
      if (allPlayers.isEmpty) null
      else allPlayers.head

    }
  }

  lazy val exists: Boolean = retrievedPlayer != null
  
  lazy val playerId: Long = retrievedPlayer.id
  
  def save(): Long = {
    
    val initialStatement = new InsertStatement(DbRetrievePlayer.tableName)
    val finalStatement = initialStatement.
    		addValue(DbRetrievePlayer.nameName, surname).
    		addValue(DbRetrievePlayer.initial, initial).
    		addValue(DbRetrievePlayer.isHighfieldName, BooleanHandling.boolToInt(isHighfield).toString)
    		
    val id = DB.withConnection { implicit connection =>
      SQL(finalStatement.toString).executeInsert()
    }
    
    id.get
    
  }
  
  override def toString: String = {
    "Player: " + initial + ". " + surname + ". " + isHighfield
  }

}

object DbSavePlayer {
  
  def handleSave(player: DbSavePlayer): Long = {
    
    if(player.exists) player.playerId
    else player.save
    
  }
  
  // Returns a list of batting position -> playerId, like the others
  def saveList(players: Seq[DbSavePlayer]): Map[Int, Long] = {
    
    def saveAndProcess(acc: Map[Int, Long], player: DbSavePlayer): Map[Int, Long] = {
      
      def addBowlingRecordToMap(acc: Map[Int, Long], playerPosition: Int, bowlRecId: Long): Map[Int, Long] = {
          acc ++ Map(playerPosition -> bowlRecId)
        }
        
        val id = handleSave(player)
        val position = player.battingPosition
        addBowlingRecordToMap(acc, position, id)
      
    }
    
    players.fold(Map()) {
      case (acc: Map[Int, Long], player: DbSavePlayer) => saveAndProcess(acc, player)
    }.asInstanceOf[Map[Int, Long]]
    
  }
  
}