package models.db.build

import anorm.SQL

import play.api.db.DB
import play.api.Play.current

import models.db.retrieve.DbRetrieveExtras

import utils.db.InsertStatement

class DbSaveExtras(
  noBalls: Int,
  wides: Int,
  legByes: Int,
  byes: Int,
  penaltyRuns: Int,
  total: Int
) {

  def save(): Long = {
    
    val statement = new InsertStatement(DbRetrieveExtras.tableName)
            .addValue(DbRetrieveExtras.noBallsName, noBalls.toString)
            .addValue(DbRetrieveExtras.widesName, wides.toString)
            .addValue(DbRetrieveExtras.legByesName, legByes.toString)
            .addValue(DbRetrieveExtras.byesName, byes.toString)
            .addValue(DbRetrieveExtras.penaltyRunsName, penaltyRuns.toString)
            .addValue(DbRetrieveExtras.totalName, total.toString)
            
    val id = DB.withConnection { implicit connection =>
      SQL(statement.toString).executeInsert()
    }
    
    id.get
    
  }
  
}