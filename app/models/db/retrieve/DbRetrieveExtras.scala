package models.db.retrieve

import anorm.~
import anorm.Id
import anorm.Pk
import anorm.RowParser
import anorm.SqlParser.get

import models.abstracts.Extras

import utils.db.DbFinder

class DbRetrieveExtras(
  iden: Pk[Long],
  val noBalls: Int,
  val wides: Int,
  val legByes: Int,
  val byes: Int,
  val penaltyRuns: Int,
  val total: Int
) extends Extras {
  
  lazy val id: Long = iden.get
  
}

object DbRetrieveExtras extends DbFinder[DbRetrieveExtras] {
  
  // Required by DbFinder
  val tableName: String = "Extras"
  val idName: String = "extras_id"
    
  // Other fields
  val noBallsName: String = "noballs"
  val widesName: String = "wides"
  val legByesName: String = "legbyes"
  val byesName: String = "byes"
  val penaltyRunsName: String = "penalty_runs"
  val totalName: String = "total"
    
  val allFieldsParser: RowParser[DbRetrieveExtras] = {
    get[Long](idName) ~ get[Int](noBallsName) ~ get[Int](widesName) ~ get[Int](legByesName) ~ get[Int](byesName) ~ get[Int](penaltyRunsName) ~ get[Int](totalName) map {
      case id ~ noBalls ~ wides ~ legByes ~ byes ~ penaltyRuns ~ total => new DbRetrieveExtras(Id(id), noBalls, wides, legByes, byes, penaltyRuns, total)
    }
  }
  
}