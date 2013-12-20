package controllers.resource

import models.abstracts.Match

import play.api.libs.json.JsValue
import play.api.libs.json.Json

import utils.resource.Jsonable

class MatchResource(
  matchEntity: Match
) extends Jsonable {
    
  lazy val toJson: JsValue = {
    
    Json.toJson(
      Map(
        MatchResource.teamName -> Json.toJson(matchEntity.team.name),
        MatchResource.opponentName -> Json.toJson(matchEntity.opponentName),
        MatchResource.venueName -> Json.toJson(matchEntity.location),
        MatchResource.leagueName -> Json.toJson(matchEntity.matchType),
        MatchResource.highfieldBattedFirstName -> Json.toJson(matchEntity.highfieldBattedFirst),
        Date.resourceName -> new Date(matchEntity.date).toJson,
        MatchResource.umpiresName -> Json.toJson(matchEntity.umpires),
        MatchResource.scorersName -> Json.toJson(matchEntity.scorers),
        Extras.highfieldExtrasName -> new Extras(matchEntity.highfieldExtras).toJson,
        Extras.nonHighfieldExtrasName -> new Extras(matchEntity.nonHighfieldExtras).toJson,
        MatchPlayer.highfieldPlayersName -> MatchPlayer.transformMap(matchEntity.highfieldPlayers),
        MatchPlayer.nonHighfieldPlayersName -> MatchPlayer.transformMap(matchEntity.nonHighfieldPlayers),
        BattingRecord.highfieldBatRecName -> BattingRecord.transformList(matchEntity.highfieldBatsmen),
        BattingRecord.nonHighfieldBatRecName -> BattingRecord.transformList(matchEntity.nonHighfieldBatsmen),
        BowlingRecord.highfieldBowlRecName -> BowlingRecord.transformList(matchEntity.highfieldBowlers),
        BowlingRecord.nonHighfieldBowlRecName -> BowlingRecord.transformList(matchEntity.nonHighfieldBowlers),
        Dismissal.highfieldDisName -> Dismissal.transformList(matchEntity.highfieldBatsmen),
        Dismissal.nonHighfieldDisName -> Dismissal.transformList(matchEntity.nonHighfieldBatsmen)
      )
    )
    
  }
  
}

object MatchResource {
  
  val resourceName: String = "match"
  
  val teamName: String = "team"
  val opponentName: String = "opponent"
  val venueName: String = "venue"
  val leagueName: String = "league"
  
  val umpiresName: String = "umpires"
  val scorersName: String = "scorers"
  
  val highfieldBattedFirstName: String = "highfield_batted_first"
  
}