package models.db.retrieve

import anorm.~
import anorm.Id
import anorm.Pk
import anorm.RowParser
import anorm.SQL
import anorm.SqlParser.get

import models.abstracts.Player
import models.abstracts.MatchPlayer
import models.abstracts.Match

import utils.db.BooleanHandling
import utils.db.DbFinder
import utils.db.PerformancesQuery
import utils.db.HasId

class DbRetrievePlayer(
  iden: Pk[Long],
  val surname: String,
  val initial: String,
  val isHighfield: Boolean
) extends Player with HasId {
  
  val id: Long = iden.get
  
  lazy val matches: Seq[Match] = {
    performances(new PerformancesQuery(id, None)).map{perf => perf.matchIn}
  }
  
  def performances(query: PerformancesQuery): Seq[MatchPlayer] = {
    DbRetrieveMatchPlayer.findByPerformanceParams(query)
  }
  
}

object DbRetrievePlayer extends DbFinder[DbRetrievePlayer] {
  
  // Required by DbFinder
  val tableName: String = "Player"
  val idName: String = "player_id"
  
  // Other Fields
  val nameName: String = "name"
  val initial: String = "init"
  val isHighfieldName = "is_highfield"
  
  val allFieldsParser: RowParser[DbRetrievePlayer] = {
    get[Long](idName) ~ get[String](nameName) ~ get[String](initial) ~ get[Int](isHighfieldName) map {
      case id ~ name ~ initial ~ isHighfield =>
        new DbRetrievePlayer(Id(id), name, initial, BooleanHandling.intToBool(isHighfield))
    }
  }
  
}