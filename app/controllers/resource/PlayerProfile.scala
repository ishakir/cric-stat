package controllers.resource

import play.api.libs.json.JsValue
import play.api.libs.json.Json

import utils.resource.Jsonable

class PlayerProfile(
  initial: String,
  name: String,
  performances: Seq[models.abstracts.MatchPlayer]
) extends Jsonable {
  
  lazy val toJson: JsValue = {
    Json.toJson(
      Map(
        Player.initialName -> Json.toJson(initial),
        Player.surnameName -> Json.toJson(name),
        ProfileBattingRecord.resourceName -> new ProfileBattingRecord(performances).toJson,
        ProfileBowlingRecord.resourceName -> new ProfileBowlingRecord(performances).toJson,
        ProfileFieldingRecord.resourceName -> new ProfileFieldingRecord(performances).toJson
      )
    )
  }
  
}

object PlayerProfile {
  
  val resourceName: String = "profile"
  
}