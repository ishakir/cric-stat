package models.db.retrieve

import anorm.~
import anorm.Pk
import anorm.Id
import anorm.RowParser
import anorm.SqlParser.get
import anorm.SQL

import models.abstracts.BattingRecord
import models.abstracts.Player
import models.abstracts.Dismissal
import models.abstracts.MatchPlayer
import models.abstracts.Match
import models.abstracts.BowlingRecord

import play.api.db.DB
import play.api.Play.current
import play.api.Logger

import utils.db.DbFinder
import utils.db.PerformancesQuery

class DbRetrieveMatchPlayer(
  iden: Pk[Long],
  battingRecordId: Int,
  bowlingRecordId: Int,
  matchId: Int,
  val battingPosition: Int,
  playerId: Int) extends MatchPlayer {

  val id: Long = iden.get

  lazy val player: Player = if (playerId == DbRetrieveMatchPlayer.nullInt) null
  else DbRetrievePlayer.findById(playerId.toString)

  lazy val matchIn: Match = DbRetrieveMatch.findById(matchId.toString)

  lazy val battingRecord: BattingRecord = if (battingRecordId == DbRetrieveMatchPlayer.nullInt) null
  else DbRetrieveBattingRecord.findById(battingRecordId.toString)

  lazy val bowlingRecord: BowlingRecord = if (bowlingRecordId == DbRetrieveMatchPlayer.nullInt) null
  else DbRetrieveBowlingRecord.findById(bowlingRecordId.toString)

  lazy val fieldingDismissals: Seq[Dismissal] = DbFieldingQuery.findDismissalsByFielder(id.toString)

  lazy val delete: Unit = DbRetrieveMatchPlayer.deleteById(id.toString)

}

object DbRetrieveMatchPlayer extends DbFinder[DbRetrieveMatchPlayer] {

  // Required by DbFinder
  val tableName: String = "MatchPlayer"
  val idName: String = "mp_id"

  // Other Fields
  val battingRecordName: String = "batting_record"
  val bowlingRecordName: String = "bowling_record"
  val matchName: String = "match"
  val battingPositionName: String = "batting_position"
  val playerName: String = "player"

  val allFieldsParser: RowParser[DbRetrieveMatchPlayer] = {
    get[Long](idName) ~ get[Option[Int]](battingRecordName) ~ get[Option[Int]](bowlingRecordName) ~ get[Int](matchName) ~ get[Int](battingPositionName) ~ get[Option[Int]](playerName) map {
      case id ~ battingRecordId ~ bowlingRecordId ~ matchId ~ battingPosition ~ playerId =>

        new DbRetrieveMatchPlayer(Id(id), optionToInt(battingRecordId), optionToInt(bowlingRecordId), matchId, battingPosition, optionToInt(playerId))
    }
  }

  def findByPerformanceParams(params: PerformancesQuery): Seq[DbRetrieveMatchPlayer] = DB.withConnection { implicit connection =>

    Logger.info("Asked to find performances from params")

    val results: Seq[DbRetrieveMatchPlayer] = SQL(params.toString).as(allFieldsParser *)

    Logger.info("Found " + results.size + " results with ids " + results.map(pl => pl.id).mkString(","))

    results

  }

}