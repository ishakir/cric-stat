package models.db.retrieve

import anorm.~
import anorm.Id
import anorm.Pk
import anorm.RowParser
import anorm.SqlParser.get

import models.abstracts.Team

import utils.db.DbFinder

class DbRetrieveTeam(
  iden: Pk[Long],
  val name: String
) extends Team {
  
  lazy val id: Long = iden.get
  
  override def toString(): String = "Team " + id + ": " + name
  
}

object DbRetrieveTeam extends DbFinder[DbRetrieveTeam] {
  
  val tableName: String = "Team"
  val idName: String = "team_id"
  
  // Other fields
  val nameName: String = "name"
  
  val allFieldsParser: RowParser[DbRetrieveTeam] = {
    get[Long](idName) ~ get[String](nameName) map {
      case id ~ name =>
        new DbRetrieveTeam(Id(id), name)
    }
  }
  
}