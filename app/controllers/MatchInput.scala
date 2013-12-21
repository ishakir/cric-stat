package controllers

import controllers.parsing.MatchParser
import controllers.resource.MatchOverview
import models.abstracts.Match
import models.db.build.DbSaveMatch
import models.db.retrieve.DbRetrieveMatch
import play.api.libs.json.JsValue
import play.api.mvc.Action
import play.api.mvc.Controller
import utils.resource.Jsonable
import utils.rest.CricStatAction

object MatchInput extends Controller {
  
  def putMatch() = CricStatAction { request =>
    
    if(request.body.asJson.isEmpty) {
      BadRequest("No request body provided as JSON!")
    }
    
    // Get the pure Json
    val requestBodyAsJson: JsValue = request.body.asJson.get
    
    // Parse it
    val theMatch: DbSaveMatch = MatchParser.parseMatch(requestBodyAsJson)
    
    // Save it
    val matchId: Long = theMatch.save
    
    // Get it from the db
    val matchEntity: Match = DbRetrieveMatch.findById(matchId.toString)
    
    // And return the resource
    val matchOverview: Jsonable = new MatchOverview(matchEntity)
    
    Ok(matchOverview.toJson)
  }
  
}