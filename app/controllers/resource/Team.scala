package controllers.resource

import play.api.libs.json.JsValue
import play.api.libs.json.Json

import utils.resource.LinkGenerator
import utils.resource.Jsonable

class Team(
  teamEntity: models.abstracts.Team
) extends Jsonable {
  
  lazy val genuineLinkToMatches: String = LinkGenerator.linkToMatchesWithTeamId(teamEntity.id.toString)
  
  lazy val toJson: JsValue = {
    
    val matchesLink = new Link(MatchOverview.multipleResourceName, genuineLinkToMatches);
    
    Json.toJson(
      Map(
        Team.nameName -> Json.toJson(teamEntity.name),
        Link.resourceName -> matchesLink.toJson
      )
    )
    
  }
  
}

object Team {
  
  val resourceName: String = "team"
  
  val nameName: String = "name"
  
}