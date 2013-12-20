package controllers.resource

import play.api.libs.json.JsValue
import play.api.libs.json.Json

import utils.resource.Jsonable

class BattingRecord(
  player: models.abstracts.MatchPlayer
) extends Jsonable {
  
  lazy val toJson: JsValue = {
    
    Json.toJson(
     Map(
       BattingRecord.battingPositionName -> player.battingPosition,
       BattingRecord.runsName -> player.battingRecord.runs
     )
   )
    
  }
  
}

object BattingRecord {
  
  val resourceName: String = "batting_record"
    
  val highfieldBatRecName: String = "highfield_batting" 
  val nonHighfieldBatRecName: String = "opposition_batting"
    
  val battingPositionName = "position"
  val runsName = "runs"
    
  def transformList(batsmen: Seq[models.abstracts.MatchPlayer]): JsValue = {
    Json.toJson(batsmen.map(pl => new BattingRecord(pl).toJson))
  }
  
}