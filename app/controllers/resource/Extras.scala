package controllers.resource

import models.abstracts

import play.api.libs.json.JsValue
import play.api.libs.json.Json

import utils.resource.Jsonable

class Extras(
  extrasEntity: abstracts.Extras
) extends Jsonable {
  
  lazy val toJson: JsValue = {
    
    Json.toJson(
      Map(
        Extras.noBallsName -> Json.toJson(extrasEntity.noBalls),
        Extras.widesName -> Json.toJson(extrasEntity.wides),
        Extras.legByesName -> Json.toJson(extrasEntity.legByes),
        Extras.byesName -> Json.toJson(extrasEntity.byes),
        Extras.penaltyRunsName -> Json.toJson(extrasEntity.penaltyRuns),
        Extras.totalName -> Json.toJson(extrasEntity.total)
      )    
    )
    
  }
  
}

object Extras {
  
  val resourceName: String = "extras"
  
  val highfieldExtrasName: String = "highfield_extras"
  val nonHighfieldExtrasName: String = "non_highfield_extras"
  
  val noBallsName: String = "no_balls"
  val widesName: String = "wides"
  val legByesName: String = "leg_byes"
  val byesName: String = "byes"
  val penaltyRunsName: String = "penalty_runs"
  val totalName: String = "total"
  
}