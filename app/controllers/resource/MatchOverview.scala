package controllers.resource

import play.api.libs.json.JsValue
import play.api.libs.json.Json

import models.abstracts.Match

import utils.resource.LinkGenerator
import utils.resource.Jsonable

class MatchOverview(
  matchEntity: Match
) extends Jsonable {
  
  lazy val genuineLink: String = LinkGenerator.linkToMatch(matchEntity.id.toString)
  
  lazy val toJson: JsValue = {
    
    val link = new Link(MatchResource.resourceName, genuineLink)
    
    Json.toJson(
      Map(
        MatchOverview.opponentName -> Json.toJson(matchEntity.opponentName),
        Date.resourceName -> new Date(matchEntity.date).toJson,
        MatchOverview.wonName -> Json.toJson(matchEntity.highfieldWon),
        Link.resourceName -> link.toJson,
        MatchOverview.idName -> Json.toJson(matchEntity.id)
      )
    )
    
  }
  
}

object MatchOverview {
  
  val multipleResourceName: String = "matches"
    
  val opponentName = "opponent"
  val dateName = "date"
  val wonName = "won"
  val idName = "id"
  
}