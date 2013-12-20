package models.db.build

import anorm.SQL

import play.api.db.DB
import play.api.Play.current

import models.db.retrieve.DbRetrieveMatchPlayer

import utils.db.InsertStatement

class DbSaveMatchPlayer(
  val battingPosition: Int,
  batRecId: Long,
  bowlRecId: Long,
  matchId: Long,
  playerId: Long
) {
  
  def save(): Long = {
    
    val initialStatement = new InsertStatement(DbRetrieveMatchPlayer.tableName).
    			addValue(DbRetrieveMatchPlayer.playerName, playerId.toString).
    			addValue(DbRetrieveMatchPlayer.matchName, matchId.toString).
    			addValue(DbRetrieveMatchPlayer.battingPositionName, battingPosition.toString)
    			
    val withBatRec = if(batRecId == DbRetrieveMatchPlayer.nullInt) {
      initialStatement
    } else {
      initialStatement.addValue(DbRetrieveMatchPlayer.battingRecordName, batRecId.toString)
    }
    
    val withBowlRec = if(bowlRecId == DbRetrieveMatchPlayer.nullInt) {
      withBatRec
    } else {
      withBatRec.addValue(DbRetrieveMatchPlayer.bowlingRecordName, bowlRecId.toString)
    }
    
    val id = DB.withConnection { implicit connection =>
      SQL(withBowlRec.toString).executeInsert()
    }
    
    id.get
    
  }
  
}

object DbSaveMatchPlayer {
  
  /**
   * Returns map from batting position to MatchPlayerId
   */
  def saveList(matchPlayers: Seq[DbSaveMatchPlayer]): Map[Int, Long] = {
    
    def saveAndProcess(matchPlayer: DbSaveMatchPlayer, acc: Map[Int, Long]): Map[Int, Long] = {
        
        def addMatchPlayerToMap(acc: Map[Int, Long], playerPosition: Int, matchPlayerId: Long): Map[Int, Long] = {
          acc ++ Map(playerPosition -> matchPlayerId)
        }
        
        val id = matchPlayer.save
        val position = matchPlayer.battingPosition
        addMatchPlayerToMap(acc, position, id)
      
      }
            
      matchPlayers.fold(Map()) {
        case (acc: Map[Int, Long], br: DbSaveMatchPlayer) => saveAndProcess(br, acc)
      }.asInstanceOf[Map[Int, Long]]
    
  }
  
}