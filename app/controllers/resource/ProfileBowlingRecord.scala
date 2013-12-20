package controllers.resource

import controllers.processing.PlayerProcessingFunctions.processPlayer
import controllers.processing.ProcessorPackage

import play.api.libs.json.Json
import play.api.libs.json.JsValue

import utils.resource.Jsonable

class ProfileBowlingRecord(
  performances: Seq[models.abstracts.MatchPlayer]
) extends Jsonable {
  
  lazy val toJson: JsValue = {
    Json.toJson(
      Map(
        ProfileBowlingRecord.oversName -> Json.toJson(oversAndBalls),
        ProfileBowlingRecord.maidensName -> Json.toJson(maidens),
        ProfileBowlingRecord.runsName -> Json.toJson(runs),
        ProfileBowlingRecord.wicketsName -> Json.toJson(wickets),
        ProfileBowlingRecord.averageName -> Json.toJson(average),
        ProfileBowlingRecord.strikeRateName -> Json.toJson(strikeRate),
        ProfileBowlingRecord.economyRateName -> Json.toJson(economyRate)
      )
    )
  }
  
  lazy val overs: Int = processPlayer(
    performances,
    new ProcessorPackage(
      mp => mp.bowlingRecord,
      (total: Int, br: models.abstracts.BowlingRecord) => total + br.overs
    )
  )
  
  lazy val extraBalls: Int = processPlayer(
    performances,
    new ProcessorPackage(
      mp => mp.bowlingRecord,
      (total: Int, br: models.abstracts.BowlingRecord) => total + br.extraBalls
    )
  )
  
  lazy val totalBalls: Int = overs * 6 + extraBalls
  lazy val totalOvers: Int = totalBalls / 6
  lazy val totalExtraBalls: Int = totalBalls % 6
  lazy val oversAndBalls: String = totalOvers + "." + totalExtraBalls
  
  lazy val maidens: Int = processPlayer(
    performances,
    new ProcessorPackage(
      mp => mp.bowlingRecord,
      (total: Int, br: models.abstracts.BowlingRecord) => total + br.maidens
    )
  )
  
  lazy val runs: Int = processPlayer(
    performances,
    new ProcessorPackage(
      mp => mp.bowlingRecord,
      (total: Int, br: models.abstracts.BowlingRecord) => total + br.runs
    )
  )
  
  lazy val wickets: Int = processPlayer(
    performances,
    new ProcessorPackage(
      mp => mp.bowlingRecord,
      (total: Int, br: models.abstracts.BowlingRecord) => total + br.wickets
    )
  )
  
  lazy val average: String = if(wickets == 0) "N/A" else (runs.toFloat / wickets.toFloat).toString
  lazy val strikeRate: String = if(wickets == 0) "N/A" else ((overs * 6 + extraBalls).toFloat/wickets.toFloat).toString
  lazy val economyRate: String = if(totalBalls == 0) "N/A" else ((runs.toFloat / totalBalls.toFloat) * 6).toString
  
}

object ProfileBowlingRecord {
  
  val resourceName: String = "bowling"
  
  val oversName: String = "overs"
  val maidensName: String = "maidens"
  val runsName: String = "runs"
  val wicketsName: String = "wickets"
  val averageName: String = "average"
  val strikeRateName: String = "strike_rate"
  val economyRateName: String = "economy_rate"
    
}