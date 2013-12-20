package controllers.resource

import play.api.libs.json.JsValue
import play.api.libs.json.Json

import utils.resource.LinkGenerator
import utils.resource.Jsonable

class Player(
  player: models.abstracts.Player
) extends Jsonable {
  
  lazy val genuineLink: String = LinkGenerator.linkToPlayerProfile(player.id.toString)
  
  lazy val toJson: JsValue = {
    
    val link = new Link(PlayerProfile.resourceName, genuineLink)
    
    Json.toJson(
      Map(
        Player.initialName -> Json.toJson(player.initial),
        Player.surnameName -> Json.toJson(player.surname),
        Player.linkName -> link.toJson,
        Player.idName -> Json.toJson(player.id),
        Player.isHighfieldName -> Json.toJson(player.isHighfield)
      )
    )
    
  }
  
}

object Player {
  
  val resourceName: String = "player"
  
  val initialName: String = "initial"
  val surnameName: String = "surname"
  val linkName: String = "link"
  val idName: String = "id"
  val isHighfieldName: String = "is_highfield"
  
}