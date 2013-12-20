package controllers.resource

import play.api.libs.json.JsValue
import play.api.libs.json.Json

import utils.resource.Jsonable

class MatchPlayer(
    battingPosition: Int, 
    player: models.abstracts.Player
) extends Jsonable {
	
  lazy val toJson: JsValue = {
    Json.toJson(
      Map(
        MatchPlayer.battingPositionName -> Json.toJson(battingPosition),
        MatchPlayer.playerName -> new Player(player).toJson
      )
    )
  }
  
}

object MatchPlayer {
  
  val resourceName: String = "matchPlayer"
  
  val highfieldPlayersName: String = "highfield_players"
  val nonHighfieldPlayersName: String = "opposition_players"
  
  val battingPositionName: String = "batting_pos"
  val playerName: String = "player"
    
  def transformMap(players: Map[Int, models.abstracts.Player]): JsValue = {
    val asList = players.toList
    val sorted = asList.sortBy {
      case(battingPosition, player) => battingPosition
    }
    val resources = sorted.map {
      case(battingPosition, player) => new MatchPlayer(battingPosition, player)
    }
    
    Json.toJson(resources.map(res => res.toJson))
    
  }
  
}