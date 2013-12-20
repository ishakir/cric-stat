package controllers.resource

import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.libs.json.JsNull

import utils.resource.Jsonable

class Dismissal(
  dismissal: models.abstracts.Dismissal
) extends Jsonable {
  
  lazy val toJson: JsValue = {
    
    val bowler: JsValue = if(dismissal.bowlingRecord == null) {
      JsNull
    } else {
      Json.toJson(dismissal.bowlingRecord.matchPlayer.battingPosition)
    }
    
    val fielder: JsValue = if(dismissal.fielder == null) {
      JsNull
    } else {
      Json.toJson(dismissal.fielder.battingPosition)
    }
    
    Json.toJson(
      Map(
        Dismissal.batsmanName -> Json.toJson(dismissal.battingRecord.matchPlayer.battingPosition),
        Dismissal.howoutName -> Json.toJson(dismissal.dismissalType.toString.toLowerCase),
        Dismissal.bowlerName -> bowler,
        Dismissal.fielderName -> fielder
      )
    )
    
  }
  
}

object Dismissal {
  
  val resourceName: String = "dismissal"
  
  val highfieldDisName: String = "highfield_dismissals"
  val nonHighfieldDisName: String = "non_highfield_dismissals"
    
  val batsmanName: String = "batsman"
  val howoutName: String = "howout"
  val bowlerName: String = "bowler"
  val fielderName: String = "fielder"
  
  def transformList(batsmen: Seq[models.abstracts.MatchPlayer]): JsValue = {
    
    val withDismissals = batsmen.filter(mp => (mp.battingRecord.dismissal != null))
    val asResource = withDismissals.map(mp => new Dismissal(mp.battingRecord.dismissal).toJson)
    Json.toJson(asResource)
    
  }
  
}