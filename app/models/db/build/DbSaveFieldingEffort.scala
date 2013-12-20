package models.db.build

import anorm.SQL

import play.api.Play.current
import play.api.db.DB

import models.db.retrieve.DbFieldingQuery

import utils.db.InsertStatement

class DbSaveFieldingEffort(
  matchPlayerId: Long,
  dismissalId: Long
) {
  
  def save(): Unit = {
    
    val initialStatement = new InsertStatement(DbFieldingQuery.tableName)
    val fullStatement = initialStatement.
    		addValue(DbFieldingQuery.matchPlayerName, matchPlayerId.toString).
    		addValue(DbFieldingQuery.dismissalName, dismissalId.toString)
    		
    DB.withConnection { implicit connection =>
      SQL(fullStatement.toString).executeInsert()
    }
    
  }
  
}

object DbSaveFieldingEffort {
  
  def saveList(mpId: Map[Int, Long], disIds: Map[Int, Seq[Long]]): Unit = {
    
    val savables = for(
        batPos <- 1 to 11;
        if(!disIds.get(batPos).isEmpty)
    ) yield {
      val matchPlayerId = mpId.get(batPos).get
      val dismissalIds = disIds.get(batPos).get
      for(dismissalId <- dismissalIds) yield new DbSaveFieldingEffort(matchPlayerId, dismissalId)
    }
    
    savables.flatten foreach (sv => sv.save)
    
  }
  
}