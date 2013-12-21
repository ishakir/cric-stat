package controllers

import controllers.resource.Team
import models.db.retrieve.DbRetrieveTeam
import play.api.libs.json.Json
import play.api.mvc.Controller
import utils.rest.CORSAction
import utils.rest.CricStatAction

object TeamOutput extends Controller {
  
  def allTeams() = CricStatAction { request =>
    
    // Get all the teams from the database
    val allEntries = DbRetrieveTeam.findAll()
    
    // Convert them all into Team Resources
    val allResources = allEntries.map(team => new Team(team))
    
    Ok(Json.toJson(allResources.map(team => team.toJson)))
    
  }
  
}