package models.db.build

import anorm.SQL
import play.api.db.DB
import play.api.Play.current
import models.db.retrieve.DbRetrieveSeason
import utils.db.InsertStatement
import play.api.Logger

class DbSaveSeason(season: Int) {
  
  lazy val retrievedSeason = DbRetrieveSeason.findById(season.toString)
  
  lazy val exists: Boolean = {
    retrievedSeason != null
  }
  
  lazy val seasonId: Long = retrievedSeason.id
  
  def save(): Long = {
    
    val initialStatement = new InsertStatement(DbRetrieveSeason.tableName)
    val statement = initialStatement.addValue(DbRetrieveSeason.idName, season.toString)
    
    DB.withConnection { implicit connection =>
      SQL(statement.toString).executeInsert()
    }
    
    // As there's no autoincrement we need to return it ourselves manually
    season
    
  }
  
}

object DbSaveSeason {
  
  def handleSave(season: Int): Long = {
    
    val seasonSave = new DbSaveSeason(season)
    Logger.debug("Saving season " + season)
    
    if(seasonSave.exists) {
      Logger.debug("Season already existed!")
      seasonSave.seasonId
    } else {
      Logger.debug("Season didn't already exist!")
      seasonSave.save
    }
  
  }
  
}