package controllers.resource

import play.api.libs.json.JsValue
import play.api.libs.json.Json

import utils.resource.Jsonable

class BowlingRecord(
  player: models.abstracts.MatchPlayer
) extends Jsonable {
  
  lazy val toJson: JsValue = {
    
    val bowlingRecord = player.bowlingRecord
    
    Json.toJson(
      Map(
        BowlingRecord.battingPositionName -> player.battingPosition,
        BowlingRecord.fulloversName -> bowlingRecord.overs,
        BowlingRecord.extraBallsName -> bowlingRecord.extraBalls,
        BowlingRecord.maidensName -> bowlingRecord.maidens,
        BowlingRecord.runsName -> bowlingRecord.runs,
        BowlingRecord.wicketsName -> bowlingRecord.wickets
      )    
    )
  }
  
}

object BowlingRecord {
  
  val resourceName: String = "bowling_record"
    
  val highfieldBowlRecName: String = "highfield_bowling"
  val nonHighfieldBowlRecName: String = "opposition_bowling"
  
  val battingPositionName: String = "position"
  val fulloversName: String = "fullovers"
  val extraBallsName: String = "extraballs"
  val maidensName: String = "maidens"
  val runsName: String = "runs"
  val wicketsName: String = "wickets"
    
  def transformList(players: Seq[models.abstracts.MatchPlayer]): JsValue = {
    Json.toJson(players.map(pl => new BowlingRecord(pl).toJson))
  }
  
}