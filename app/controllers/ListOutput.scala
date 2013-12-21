package controllers

import models.db.retrieve.DbRetrievePlayer
import controllers.resource.PlayerListResource
import controllers.resource.PlayerListResourceType
import utils.rest.CricStatAction
import models.abstracts.Player
import play.api.mvc.Controller
import utils.db.BooleanHandling
import controllers.processing.PlayerProcessingFunctions.processList
import controllers.processing.ProcessorPackage
import controllers.resource.Runs
import controllers.resource.Wickets
import controllers.resource.Catches
import controllers.resource.RunOuts
import controllers.resource.ProfileBattingRecord
import utils.db.PerformancesQuery
import controllers.resource.BattingAverage
import controllers.resource.ProfileBowlingRecord
import controllers.resource.BowlingAverage
import controllers.resource.EconomyRate
import controllers.resource.StrikeRate
import play.api.libs.json.Json
import utils.rest.InputValidation.seasonIsInDb

object ListOutput extends Controller {

  val runsOption: String = "runs"
  val wicketsOption: String = "wickets"
  val catchesOption: String = "catches"
  val runoutsOption: String = "runouts"
    
  val battingAverageOption: String = "batavg"
  val bowlingAverageOption: String = "bowlavg"
  val economyRateOption: String = "econrate"
  val strikeRateOption: String = "strkrate"
    
  val asList = List(
    runsOption, 
    wicketsOption, 
    catchesOption, 
    runoutsOption, 
    battingAverageOption, 
    bowlingAverageOption, 
    economyRateOption, 
    strikeRateOption
  )

  def rankedList(target: String, season: Option[String]) = CricStatAction { request =>
      
      if(!season.isEmpty && !seasonIsInDb(season.get)) {
        throw new IllegalArgumentException("Season "+season+" does not exist!")
      }
      
      val highfieldPlayers = highfieldPlayersQuery
      val resources: Seq[PlayerListResource] = getResources(target, season, highfieldPlayers)

      // Map that to Json
      Ok(Json.toJson(resources.map(ind => ind.toJson)))

  }
  
  def getResources(target: String, season: Option[String], highfieldPlayers: Seq[Player]): Seq[PlayerListResource] = {
    
    if (target.equals(runsOption)) {
        val players: Seq[Player] = highfieldPlayers
        val values: Seq[(Player, Int)] = processList(
          players, 
          season,
          ProcessorPackage.runs
        )
        mapToResources(values.map(st => (st._1, st._2.toString)), Runs)
      } else if (target.equals(wicketsOption)) {
        val players: Seq[Player] = highfieldPlayers
        val values: Seq[(Player, Int)] = processList(
          players,
          season,
          ProcessorPackage.wickets
        )
        mapToResources(values.map(st => (st._1, st._2.toString)), Wickets)
      } 
      // Filter step not right for fielding queerys
      else if(target.equals(catchesOption)) {
        val players: Seq[Player] = highfieldPlayers
        val values: Seq[(Player, Int)] = processList(
          players,
          season,
          ProcessorPackage.catches
        )
        mapToResources(values.map(st => (st._1, st._2.toString)), Catches)
      } else if(target.equals(runoutsOption)) {
        val players: Seq[Player] = highfieldPlayers
        val values: Seq[(Player, Int)] = processList(
          players,
          season,
          ProcessorPackage.runouts
        )
        mapToResources(values.map(st => (st._1, st._2.toString)), RunOuts)
      } else if(target.equals(battingAverageOption)) {
        val players: Seq[Player] = highfieldPlayers
        val battingRecords: Seq[(Player, ProfileBattingRecord)] = players.map(pl => (pl, new ProfileBattingRecord(pl.performances(new PerformancesQuery(pl.id, season)))))
        val filtered: Seq[(Player, ProfileBattingRecord)] = battingRecords.filter(st => !st._2.average.equals("N/A"))
        val doneAndSorted: Seq[(Player, Float)] = filtered.map(pl => (pl._1, pl._2.average.toFloat)).sortBy(pl => -pl._2)
        mapToResources(doneAndSorted.map(pl => (pl._1, pl._2.toString)), BattingAverage)
      } else if(target.equals(bowlingAverageOption)) {
        val players: Seq[Player] = highfieldPlayers
        val averages: Seq[(Player, String)] = players.map(pl => (pl, new ProfileBowlingRecord(pl.performances(new PerformancesQuery(pl.id, season))).average))
        mapToResources(averages, BowlingAverage)
      } else if(target.equals(economyRateOption)) {
        val players: Seq[Player] = highfieldPlayers
        val economies: Seq[(Player, String)] = players.map(pl => (pl, new ProfileBowlingRecord(pl.performances(new PerformancesQuery(pl.id, season))).economyRate))
        mapToResources(economies, EconomyRate)
      } else if(target.equals(strikeRateOption)) {
        val players: Seq[Player] = highfieldPlayers
        val strikes: Seq[(Player, String)] = players.map(pl => (pl, new ProfileBowlingRecord(pl.performances(new PerformancesQuery(pl.id, season))).strikeRate))
        mapToResources(strikes, StrikeRate)
      } else {
        throw new IllegalArgumentException("Possible targets are " + asList.mkString(",") + " not " + target)
      }
    
  }

  def highfieldPlayersQuery[T]: Seq[(Player)] = {
    
    // Get all the players in the Db
    DbRetrievePlayer.findFilteredByEqualsAttributes(
      Map(
        DbRetrievePlayer.isHighfieldName -> BooleanHandling.boolToInt(true).toString
      )
    )
    
  }
  
  def mapToResources(list: Seq[(Player, String)], theType: PlayerListResourceType): Seq[PlayerListResource] = {
    
    // Map to a list resource
    val resources: Seq[PlayerListResource] = list.map {
      case (player, value) => new PlayerListResource(player, theType, value.toString)
    }

    resources
    
  }

}