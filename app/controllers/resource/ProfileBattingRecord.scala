package controllers.resource

import controllers.processing.PlayerProcessingFunctions.processPlayer
import controllers.processing.ProcessorPackage

import play.api.libs.json.JsValue
import play.api.libs.json.Json

import utils.resource.Jsonable

class ProfileBattingRecord (
  performances: Seq[models.abstracts.MatchPlayer]
) extends Jsonable {
  
  lazy val toJson: JsValue = {
    Json.toJson(
      Map(
        ProfileBattingRecord.inningsName -> Json.toJson(innings),
        ProfileBattingRecord.runsName -> Json.toJson(runs),
        ProfileBattingRecord.notOutsName -> Json.toJson(notouts),
        ProfileBattingRecord.averageName -> Json.toJson(average)
      )
    )
  }
  
  lazy val innings: Int = processPlayer(
    performances,
    new ProcessorPackage(
      mp => mp.battingRecord,
      (total: Int, innings: models.abstracts.BattingRecord) => if(innings == null) total else total + 1
    )
  )
  
  lazy val runs: Int = processPlayer(
    performances,
    new ProcessorPackage(
      mp => mp.battingRecord,
      (total: Int, innings: models.abstracts.BattingRecord) => total + innings.runs
    )
  )
  
  lazy val notouts: Int = processPlayer(
    performances,
    new ProcessorPackage(
      mp => mp.battingRecord,
      (total: Int, innings: models.abstracts.BattingRecord) => if(innings.wasOut) total else total + 1
    )
  )
  
  lazy val average: String = if(innings - notouts == 0) "N/A" else (runs / (innings - notouts)).toString
  
}

object ProfileBattingRecord {
  
  val resourceName: String = "batting"
    
  val inningsName: String = "innings"
  val runsName: String = "runs"
  val notOutsName: String = "notouts"
  val averageName: String = "average"
  
}