package controllers.resource

import controllers.processing.ProcessorPackage
import controllers.processing.PlayerProcessingFunctions.processPlayer

import models.RunOut
import models.Caught

import play.api.libs.json.JsValue
import play.api.libs.json.Json

import utils.resource.Jsonable

class ProfileFieldingRecord (
  performances: Seq[models.abstracts.MatchPlayer]
) extends Jsonable {
  
  lazy val toJson: JsValue = {
    Json.toJson(
      Map(
        ProfileFieldingRecord.catchesName -> catches,
        ProfileFieldingRecord.runoutsName -> runouts
      )
    )
  }
  
  lazy val catches: Int = processPlayer(
    performances,
    new ProcessorPackage(
      mp => mp.fieldingDismissals,
      (a: Int, dismissals: Seq[models.abstracts.Dismissal]) => dismissals.filter(dis => dis.dismissalType == Caught).size
    )
  )
  
  lazy val runouts: Int = processPlayer(
    performances,
    new ProcessorPackage(
      mp => mp.fieldingDismissals,
      (a: Int, dismissals: Seq[models.abstracts.Dismissal]) => dismissals.filter(dis => dis.dismissalType == RunOut).size
    )
  )
  
}

object ProfileFieldingRecord {
  
  val resourceName: String = "fielding"
  
  val catchesName: String = "catches"
  val runoutsName: String = "runouts"
  
}