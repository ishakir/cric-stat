package models.db.build

import anorm.SQL
import play.api.db.DB
import play.api.Play.current
import models.db.retrieve.DbRetrieveTeam
import utils.db.InsertStatement
import play.api.Logger

class DbSaveTeam(teamName: String) {
  
  lazy val retrievedTeam = {
    val teamList = DbRetrieveTeam.findFilteredByEqualsAttributes(
      Map(
        DbRetrieveTeam.nameName -> teamName
      )
    )
    if(teamList.isEmpty) null
    else if(teamList.size > 1) throw new IllegalStateException("More than one team named " + teamName)
    else teamList.head
  }
    
  
  lazy val exists: Boolean = {
    retrievedTeam != null
  }
  
  lazy val teamId: Long = retrievedTeam.id
  
  def save(): Long = {
    
    val initialStatement = new InsertStatement(DbRetrieveTeam.tableName)
    val statement = initialStatement.addValue(DbRetrieveTeam.nameName, teamName)
    
    val id = DB.withConnection { implicit connection =>
      SQL(statement.toString).executeInsert()
    }
    
    id.get
    
  }
  
}

object DbSaveTeam {
  
  def handleSave(teamName: String): Long = {
    
    Logger.debug("Saving team with name " + teamName)
    val teamSave = new DbSaveTeam(teamName)
    
    if(teamSave.exists) {
      Logger.debug("Team already existed with id " + teamSave.teamId)
      teamSave.teamId
    } else {
      val id = teamSave.save
      Logger.debug("Team didn't already exist, was created with id " + id)
      id
    }
    
  }
  
}