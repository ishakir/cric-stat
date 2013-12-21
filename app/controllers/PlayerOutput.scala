package controllers

import controllers.resource.Player
import controllers.resource.PlayerProfile
import models.db.retrieve.DbRetrievePlayer
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.libs.json.Json
import utils.db.BooleanHandling
import utils.db.PerformancesQuery
import utils.resource.Jsonable
import utils.rest.CricStatAction

object PlayerOutput extends Controller {
  
  // TODO sort this out, as you have to get capitalization right
  def playerQuery(nameQuery: Option[String]) = CricStatAction {request =>
    
    val equalsMap: Map[String, String] = Map(DbRetrievePlayer.isHighfieldName -> BooleanHandling.boolToInt(true).toString)
    
    val containsMap: Map[String, String] = if(nameQuery.isEmpty) {
      Map()
    } else {
      Map(DbRetrievePlayer.nameName -> nameQuery.get)
    }
    
    def allHighfieldEntities = DbRetrievePlayer.findFilteredByAttributes(
      equalsMap,
      containsMap
    )
    
    // convert them into the Player resource
    val allResources = allHighfieldEntities.map(entity => new Player(entity))
    
    Ok(Json.toJson(allResources.map(res => res.toJson)))
    
  }
  
  def playerProfile(playerId: String, season: Option[String]) = CricStatAction { request => 
  	
    // Get the relevant player from the db
    val player: models.abstracts.Player = DbRetrievePlayer.findById(playerId)
    
    // Get the performances
    val performances: Seq[models.abstracts.MatchPlayer] = player.performances(new PerformancesQuery(player.id, season))
    
    // Create the player profile
    val profile: Jsonable = new PlayerProfile(player.initial, player.surname, performances)
    
    Ok(profile.toJson)
    
  }
  
}