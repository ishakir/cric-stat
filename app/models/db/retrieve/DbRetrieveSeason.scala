package models.db.retrieve

import anorm.SqlParser.get
import anorm.RowParser

import models.abstracts.Season

import utils.db.DbFinder
import utils.db.HasId

class DbRetrieveSeason(
  val year: Int
) extends Season with HasId {
  
  val id: Long = year.toLong
  def getYear(): Int = year
  
  override def toString(): String = "The year " + year

}

object DbRetrieveSeason extends DbFinder[DbRetrieveSeason] {
  
  // Required by DbFinder
  val tableName: String = "Season"
  val idName: String = "year"

  val allFieldsParser: RowParser[DbRetrieveSeason] = {
    get[Int](idName) map {
      case year =>
        new DbRetrieveSeason(year)
    }
  }

}
