package models.db.build

import anorm.SQL
import models.MatchMetadata
import models.db.retrieve.DbRetrieveMatch
import play.api.Logger
import play.api.Play.current
import play.api.db.DB
import utils.db.InsertStatement
import utils.parsing.ParsedTeamInfo
import utils.db.BooleanHandling

class DbSaveMatch(
  highfield: ParsedTeamInfo,
  opposition: ParsedTeamInfo,
  otherData: MatchMetadata
) {
  
  def saveMatch(seasonId: Long, teamId: Long, highfieldExtrasId: Long, nonHighfieldExtrasId: Long): Long = {
    
    val initialStatement = new InsertStatement(DbRetrieveMatch.tableName)
    val withForeignKeys = initialStatement
    		.addValue(DbRetrieveMatch.seasonName, seasonId.toString)
    		.addValue(DbRetrieveMatch.teamName, teamId.toString)
    		.addValue(DbRetrieveMatch.highfieldExtrasName, highfieldExtrasId.toString)
    		.addValue(DbRetrieveMatch.nonHighfieldExtrasName, nonHighfieldExtrasId.toString)
    		
    val withOtherNecessaries = withForeignKeys
    		.addValue(DbRetrieveMatch.monthName, otherData.month.toString)
    		.addValue(DbRetrieveMatch.dayName, otherData.day.toString)
    		.addValue(DbRetrieveMatch.opponentName, otherData.opponent)
    		.addValue(DbRetrieveMatch.highfieldBattedFirstName, BooleanHandling.boolToInt(otherData.highfieldBattedFirst).toString)
    		
    val withVenue = {
      if(otherData.venue == null) withOtherNecessaries
      else withOtherNecessaries.addValue(DbRetrieveMatch.venueName, otherData.venue)
    }
    
    val withMatchType = {
      if(otherData.matchType == null) withVenue
      else withVenue.addValue(DbRetrieveMatch.matchTypeName, otherData.matchType)
    }
    
    val withScorers = {
      if(otherData.scorers == null) withMatchType
      else withMatchType.addValue(DbRetrieveMatch.scorersName, otherData.scorers)
    }
    
    val withUmpires = {
      if(otherData.umpires == null) withScorers
      else withScorers.addValue(DbRetrieveMatch.umpiresName, otherData.umpires)
    }
    
    val id = DB.withConnection { implicit connection =>
      SQL(withUmpires.toString).executeInsert()
    }
    
    id.get
    
  }
  
  def formSaveMatchPlayers(
    positionToPlayerId: Map[Int, Long],
    positionToBowlingRecord: Map[Int, Long],
    positionToBattingRecord: Map[Int, Long],
    matchId: Long
  ): Seq[DbSaveMatchPlayer] = {
    
    def existsOrNull(position: Int, map: Map[Int, Long]): Long = {
      val optionLong = map.get(position)
      if(optionLong.isEmpty) DbRetrieveMatch.nullInt
      else optionLong.get
    }
    
    for(position <- (1 to 11)) yield {
      val batRecId = existsOrNull(position, positionToBattingRecord)
      val bowlRecId = existsOrNull(position, positionToBowlingRecord)
      val playerId = existsOrNull(position, positionToPlayerId)
      
      new DbSaveMatchPlayer(
        position,
        batRecId,
        bowlRecId,
        matchId,
        playerId
      )
      
    }
    
  }
  
  def save(): Long = {
    
    // Save all Bowling Records and grab id and batting position
    Logger.info("Saving highfield bowling records")
    val highfieldPositionBowlRecIdMap = DbSaveBowlingRecord.saveList(highfield.bowlRecs)
    Logger.info("Saving non-highfield bowling records")
    val nonHighfieldPositionBowlRecIdMap = DbSaveBowlingRecord.saveList(opposition.bowlRecs)
    
    // Save all Dismissals and grab bowlers batting positions and fielders batting positions
    Logger.info("Saving non-highfield dismissals")
    val (highfieldFielderPositionDisIdMap, nonHighfieldBatsmanPositionDisIdMap) = DbSaveDismissal.saveList(opposition.dismissals, highfieldPositionBowlRecIdMap)
    Logger.info("Saving highfield dismissals")
    val (nonHighfieldFielderPositionDisIdMap, highfieldBatsmanPositionDisIdMap) = DbSaveDismissal.saveList(highfield.dismissals, nonHighfieldPositionBowlRecIdMap)
    
    // Save all Batting positions and grab batting positions
    Logger.info("Saving highfield batting records")
    val highfieldPositionBatRecIdMap = DbSaveBattingRecord.saveList(highfield.batRecs, highfieldBatsmanPositionDisIdMap)
    Logger.info("Saving non-highfield batting records")
    val nonHighfieldPositionBatRecIdMap = DbSaveBattingRecord.saveList(opposition.batRecs, nonHighfieldBatsmanPositionDisIdMap)
    
    // Save (or not) the season and team and get their ids
    val seasonId = DbSaveSeason.handleSave(otherData.year)
    val teamId = DbSaveTeam.handleSave(otherData.team)
    
    // Save the extras and grab their Ids
    Logger.info("Saving highfield extras")
    val highfieldExtrasId = highfield.extras.save
    Logger.info("Saving non-highfield extras")
    val nonHighfieldExtrasId = opposition.extras.save
    
    // Save the match and grab it's id
    Logger.info("Saving match")
    val matchId = saveMatch(seasonId, teamId, highfieldExtrasId, nonHighfieldExtrasId)
    
    // Save (or not) each of the players
    Logger.info("Saving highfield players")
    val highfieldPositionPlayerIdMap = DbSavePlayer.saveList(highfield.players)
    Logger.info("Saving non-highfield players")
    val nonHighfieldPositionPlayerIdMap = DbSavePlayer.saveList(opposition.players)
    
    // Generate the match player savers
    val highfieldMatchPlayers = formSaveMatchPlayers(
      highfieldPositionPlayerIdMap,
      highfieldPositionBowlRecIdMap,
      highfieldPositionBatRecIdMap,
      matchId
    )
    
    val nonHighfieldMatchPlayers = formSaveMatchPlayers(
      nonHighfieldPositionPlayerIdMap,
      nonHighfieldPositionBowlRecIdMap,
      nonHighfieldPositionBatRecIdMap,
      matchId
    )
    
    // AAAND save them
    Logger.info("Saving highfield match players")
    val highfieldBattingPositionToMpIdMap = DbSaveMatchPlayer.saveList(highfieldMatchPlayers)
    Logger.info("Saving non-highfield match players")
    val nonHighfieldBattingPositionToMpIdMap = DbSaveMatchPlayer.saveList(nonHighfieldMatchPlayers)
    
    // Finally save all the fielding efforts
    Logger.info("Saving highfield fielding efforts")
    DbSaveFieldingEffort.saveList(highfieldBattingPositionToMpIdMap, highfieldFielderPositionDisIdMap)
    Logger.info("Saving non-highfield fielding efforts")
    DbSaveFieldingEffort.saveList(nonHighfieldBattingPositionToMpIdMap, nonHighfieldFielderPositionDisIdMap)
    
    matchId
    
  }
  
}