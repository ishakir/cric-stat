package controllers.resource

import play.api.libs.json.JsValue
import play.api.libs.json.Json

import utils.resource.Jsonable

class Season(
  season: models.abstracts.Season
) extends Jsonable {
	
  lazy val toJson: JsValue = {
    
    Json.toJson(season.getYear)
    
  }
  
}