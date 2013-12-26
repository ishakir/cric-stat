package models.db.retrieve

import anorm.~
import anorm.Id
import anorm.Pk
import anorm.RowParser
import anorm.SqlParser.get
import java.util.GregorianCalendar
import java.util.Calendar
import models.MatchMetadata
import models.abstracts.BattingRecord
import models.abstracts.Player
import models.abstracts.Season
import models.abstracts.Team
import models.abstracts.Extras
import models.abstracts.MatchPlayer
import models.abstracts.Match
import utils.db.DbFinder
import utils.db.BooleanHandling

class DbRetrieveMatch(
  iden: Pk[Long],
  teamId: Int,
  otherData: MatchMetadata,
  highfieldExtrasId: Int,
  nonHighfieldExtrasId: Int
) extends Match {

  val id: Long = iden.get

  lazy val season: Season = DbRetrieveSeason.findById(otherData.year.toString)
  lazy val date: Calendar = new GregorianCalendar(otherData.year, otherData.month, otherData.day)
  val opponentName: String = otherData.opponent
  val location: String = otherData.venue
  val matchType: String = otherData.matchType
  val scorers: String = otherData.scorers
  val umpires: String = otherData.umpires
  
  val team: Team = DbRetrieveTeam.findById(teamId.toString)
  
  private def anyTeamRuns(batsmen: Seq[BattingRecord], extras: Extras): Int = {
    val nonExtras: Int = batsmen.map(br => br.runs).sum
    val withExtras: Int = nonExtras + extras.noBalls + extras.wides + extras.legByes + extras.byes + extras.penaltyRuns
    withExtras
  }
  
  lazy val highfieldRuns: Int = anyTeamRuns(highfieldBatsmen.map(mp => mp.battingRecord), highfieldExtras)
  lazy val nonHighfieldRuns: Int = anyTeamRuns(nonHighfieldBatsmen.map(mp => mp.battingRecord), nonHighfieldExtras)

  lazy val highfieldWon: Boolean = highfieldRuns > nonHighfieldRuns
  val highfieldBattedFirst: Boolean = otherData.highfieldBattedFirst
  
  private lazy val allMatchPlayers: Seq[MatchPlayer] = DbRetrieveMatchPlayer.findFilteredByEqualsAttributes(
    Map(
      DbRetrieveMatchPlayer.matchName -> id.toString
    )
  )
  
  lazy val highfieldMatchPlayers: Seq[MatchPlayer] = allMatchPlayers.filter(mp => mp.player.isHighfield)
  lazy val nonHighfieldMatchPlayers: Seq[MatchPlayer] = allMatchPlayers.filter(mp => !mp.player.isHighfield)
  
  // A bit nasty, when we return the match we return the names
  // with the batting position to for easy referencing
  lazy val highfieldPlayers: Map[Int, Player] = highfieldMatchPlayers.map(mp => (mp.battingPosition, mp.player)).toMap
  lazy val nonHighfieldPlayers: Map[Int, Player] = nonHighfieldMatchPlayers.map(mp => (mp.battingPosition, mp.player)).toMap
  
  lazy val highfieldBatsmen: Seq[MatchPlayer] = highfieldMatchPlayers.filter(mp => mp.battingRecord != null)
  lazy val highfieldBowlers: Seq[MatchPlayer] = highfieldMatchPlayers.filter(mp => mp.bowlingRecord != null)

  lazy val nonHighfieldBatsmen: Seq[MatchPlayer] = nonHighfieldMatchPlayers.filter(mp => mp.battingRecord != null)
  lazy val nonHighfieldBowlers: Seq[MatchPlayer] = nonHighfieldMatchPlayers.filter(mp => mp.bowlingRecord != null)
  
  lazy val highfieldExtras: Extras = DbRetrieveExtras.findById(highfieldExtrasId.toString)
  lazy val nonHighfieldExtras: Extras = DbRetrieveExtras.findById(nonHighfieldExtrasId.toString)
  
  override def toString(): String = {
      "Match " + id + ": " + 
      "teamId: " + teamId + ", " + 
      "date: " + date + ", "
  }
  
  lazy val delete: Unit = DbRetrieveMatch.deleteById(id.toString)
  
}

object DbRetrieveMatch extends DbFinder[DbRetrieveMatch] {

  // Required by DbFinder
  val tableName: String = "Match"
  val idName: String = "match_id"

  // Other fields
  val teamName: String = "team"
  val seasonName: String = "season"
  val monthName: String = "month"
  val dayName: String = "day"
  val opponentName: String = "opponent"
  val venueName: String = "venue"
  val matchTypeName: String = "matchType"
  val scorersName: String = "scorers"
  val umpiresName: String = "umpires"
  val highfieldExtrasName: String = "highfield_extras"
  val nonHighfieldExtrasName: String = "non_highfield_extras"
  val highfieldBattedFirstName: String = "highfield_batted_first"

  val allFieldsParser: RowParser[DbRetrieveMatch] = {
    get[Long](idName) ~ get[Int](teamName) ~ get[Int](seasonName) ~ get[Int](monthName) ~ get[Int](dayName) ~ get[String](opponentName) ~ get[Option[String]](venueName) ~ get[Option[String]](matchTypeName) ~ get[Option[String]](scorersName) ~ get[Option[String]](umpiresName) ~ get[Int](highfieldExtrasName) ~ get[Int](nonHighfieldExtrasName) ~ get[Int](highfieldBattedFirstName) map {
      case id ~ team ~ season ~ month ~ day ~ opponent ~ venue ~ matchType ~ scorers ~ umpires ~ highfieldExtrasId ~ nonHighfieldExtrasId ~ highfieldBattedFirst =>
        
        val otherData = new MatchMetadata(
          season,
          month,
          day,
          opponent,
          optionToObject(venue),
          optionToObject(matchType),
          optionToObject(scorers),
          optionToObject(umpires),
          null,
          BooleanHandling.intToBool(highfieldBattedFirst)
        )
        
        new DbRetrieveMatch(
          Id(id),
          team,
          otherData,
          highfieldExtrasId,
          nonHighfieldExtrasId
        )
        
    }
  }

}