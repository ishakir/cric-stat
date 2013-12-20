package controllers

import controllers.resource.Season

import play.api.mvc.Controller
import play.api.libs.json.Json

import models.db.retrieve.DbRetrieveSeason

import utils.rest.CORSAction

object SeasonOutput extends Controller {
  
	def allSeasons() = CORSAction { request =>
	  
	  // Get all seasons from the database
	  val seasons = DbRetrieveSeason.findAll()
	  
	  // Convert them all into a resource
	  val allResources = seasons.map(season => new Season(season))
	  
	  Ok(Json.toJson(allResources.map(season => season.toJson)))
	  
	}

}