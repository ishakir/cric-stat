package controllers.resource

import play.api.libs.json.Json

import utils.resource.Jsonable

abstract class PlayerListResourceType

object Runs extends PlayerListResourceType { override val toString: String = "runs" }
object Wickets extends PlayerListResourceType { override val toString: String = "wickets" }
object Catches extends PlayerListResourceType { override val toString: String = "catches" }
object RunOuts extends PlayerListResourceType { override val toString: String = "runouts" }
object BattingAverage extends PlayerListResourceType { override val toString: String = "batavg" }
object BowlingAverage extends PlayerListResourceType { override val toString: String = "bowlavg" }
object EconomyRate extends PlayerListResourceType { override val toString: String = "econrate" }
object StrikeRate extends PlayerListResourceType { override val toString: String = "strkrate" }

class PlayerListResource(
  player: models.abstracts.Player,
  theType: PlayerListResourceType,
  value: String
) extends Jsonable {
  
  lazy val toJson = {
    
    Json.toJson(
      Map(
        PlayerListResource.playerName -> (new Player(player)).toJson,
        PlayerListResource.typeName -> Json.toJson(theType.toString),
        PlayerListResource.valueName -> Json.toJson(value)
      )
    )
    
  }
  
}

object PlayerListResource {
  
  val resourceName: String = "playerlistindividual"
  
  val playerName: String = "player"
  val typeName: String = "type"
  val valueName: String = "value"
    
}