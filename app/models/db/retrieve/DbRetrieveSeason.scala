package models.db.retrieve

import anorm.RowParser
import anorm.SqlParser.get
import models.abstracts.Match
import models.abstracts.Season
import utils.db.DbFinder

class DbRetrieveSeason(
  val year: Int
) extends Season {
  
  val id: Long = year.toLong
  def getYear(): Int = year
  
  override def toString(): String = "The year " + year
  
  lazy val matches: Seq[Match] = {
    DbRetrieveMatch.findFilteredByEqualsAttributes(Map(
      DbRetrieveMatch.seasonName -> id.toString
    ))
  }
  
  lazy val delete: Unit = DbRetrieveSeason.deleteById(id.toString)

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
