package controllers.parsing

import play.api.libs.json.JsValue
import play.api.Logger

import models.db.build.DbSaveMatch
import models.MatchMetadata

import utils.parsing.ParsedTeamInfo

object MatchParser {
  
  val teamName = "team"
  val opponentName = "opponent"
  val venueName = "venue"
  val leagueName = "league"
  val dateObjName = "date"
  val dateName = "date"
  val monthName = "month"
  val yearName = "year"
  val umpiresName = "umpires"
  val scorersName = "scorers"
  val highfieldBattedFirstName = "highfield_batted_first"
  
  def parseMatch(matchJson: JsValue): DbSaveMatch = {
    
    // Parse out the Bowling Records
    val highfieldBowlRecs = BowlingRecordParser.getBowlingRecords(matchJson, true)
    val nonHighfieldBowlRecs = BowlingRecordParser.getBowlingRecords(matchJson, false)
    
    // Parse out the Dismissals
    val highfieldDismissals = DismissalParser.getDismissals(matchJson, true)
    val nonHighfieldDismissals = DismissalParser.getDismissals(matchJson, false)
    
    // Parse out the Batting Records
    val highfieldBatRecs = BattingRecordParser.getBattingRecords(matchJson, true)
    val nonHighfieldBatRecs = BattingRecordParser.getBattingRecords(matchJson, false)
    
    // Grab all the metadata
    val otherData = parseMatchMetadata(matchJson)
    
    // Grab all the players
    val highfieldPlayers = PlayerParser.getPlayers(matchJson, true)
    val nonHighfieldPlayers = PlayerParser.getPlayers(matchJson, false)
    
    // Grab the extras
    val highfieldExtras = ExtrasParser.getExtras(matchJson, true)
    val nonHighfieldExtras = ExtrasParser.getExtras(matchJson, false)
    
    // Create the highfield summary
    val highfield = new ParsedTeamInfo(
      highfieldPlayers,
      highfieldBowlRecs,
      highfieldDismissals,
      highfieldBatRecs,
      highfieldExtras
    )
    
    val opposition = new ParsedTeamInfo(
      nonHighfieldPlayers,
      nonHighfieldBowlRecs,
      nonHighfieldDismissals,
      nonHighfieldBatRecs,
      nonHighfieldExtras
    )
    
    new DbSaveMatch(
      highfield,
      opposition,
      otherData
    )
    
  }
  
  def parseMatchMetadata(matchJson: JsValue): MatchMetadata = {
    
    val dateObj = (matchJson \ dateObjName)
    
    val year = (dateObj \ yearName).as[Int]
    val month = (dateObj \ monthName).as[Int]
    val day = (dateObj \ dateName).as[Int]
    
    Logger.debug("Parsed date as: ")
    Logger.debug("Year: " + year)
    Logger.debug("Month: " + month)
    Logger.debug("Day: " + day)
    
    val opponent = (matchJson \ opponentName).as[String]
    val venue = (matchJson \ venueName).as[String]
    val matchType = (matchJson \ leagueName).as[String]
    val scorers = (matchJson \ scorersName).as[String]
    val umpires = (matchJson \ umpiresName).as[String]
    
    val highfieldBattedFirst = (matchJson \ highfieldBattedFirstName).as[Boolean]
    
    Logger.debug("Parsed opponent as: " + opponent)
    Logger.debug("Parsed venue as: " + venue)
    Logger.debug("Parsed matchType as: " + matchType)
    Logger.debug("Parsed scorers as: " + scorers)
    Logger.debug("Parsed umpires as: " + umpires)
    
    // Grab team
    val team = (matchJson \ teamName).as[String]
    
    Logger.debug("Parsed team as: " + team)
    
    new MatchMetadata(
      year,
      month,
      day,
      opponent,
      venue,
      matchType,
      scorers,
      umpires,
      team,
      highfieldBattedFirst
    )
    
  }
  
}