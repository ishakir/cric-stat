package controllers

import controllers.resource.MatchOverview
import controllers.resource.MatchResource

import models.db.retrieve.DbRetrieveMatch

import play.api.mvc.Controller
import play.api.libs.json.Json

import utils.rest.CORSAction

object MatchOutput extends Controller {
  
  def matchQuery(year: Option[String], teamId: Option[String]) = CORSAction { request =>
    
    // Form the parameter map
    val equalsMap: Map[String, String] = Map()
    
    // Do we need to query for year
    val withYearEqualsMap: Map[String, String] = if(year.isEmpty) {
      equalsMap
    } else {
      equalsMap ++ Map(DbRetrieveMatch.seasonName -> year.get)
    }
    
    // Do we need to query for team
    val withTeamEqualsMap: Map[String, String] = if(teamId.isEmpty) {
      withYearEqualsMap
    } else {
      withYearEqualsMap ++ Map(DbRetrieveMatch.teamName -> teamId.get)
    }
    
    // Get all the matches from the database
    val allEntities = DbRetrieveMatch.findFilteredByEqualsAttributes(withTeamEqualsMap)
    
    // convert them into MatchOverviews
    val allResources = allEntities.map(entity => new MatchOverview(entity))
    
    Ok(Json.toJson(allResources.map(res => res.toJson)))
    
  }
  
  def individualMatch(matchId: String) = CORSAction { request =>
    
    // Get that match from the database
    val matchEntity = DbRetrieveMatch.findById(matchId)
    
    // From it form a match resource
    val matchResource = new MatchResource(matchEntity)
    
    Ok(matchResource.toJson)
    
  }

}