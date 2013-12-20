package controllers.parsing

import models.db.build.DbSaveExtras
import play.api.libs.json.JsValue
import play.api.Logger

object ExtrasParser {
  
  val highfieldExtrasName: String = "highfield_extras"
  val nonHighfieldExtrasName: String = "non_highfield_extras"
  
  val noBallsName: String = "no_balls"
  val widesName: String = "wides"
  val legByesName: String = "leg_byes"
  val byesName: String = "byes"
  val penaltyRunsName: String = "penalty_runs"
  val totalName: String = "total"
  
  def getExtras(matchJson: JsValue, highfield: Boolean): DbSaveExtras = {
    
    if(highfield) {
      Logger.info("Parsing highfield extras")
    } else {
      Logger.info("Parsing non-highfield extras")
    }
    
    val jsonKey = {
      if(highfield) highfieldExtrasName
      else nonHighfieldExtrasName
    }
    
    val extrasJson = (matchJson \ jsonKey)
    
    parseExtras(extrasJson)
    
  }
  
  def parseExtras(extrasJson: JsValue): DbSaveExtras = {
    
    val noBalls = (extrasJson \ noBallsName).as[Int]
    val wides = (extrasJson \ widesName).as[Int]
    val legByes = (extrasJson \ legByesName).as[Int]
    val byes = (extrasJson \ byesName).as[Int]
    val penaltyRuns = (extrasJson \ penaltyRunsName).as[Int]
    val total = (extrasJson \ totalName).as[Int]
    
    Logger.debug("Parsed Extras: ")
    Logger.debug("Wides: " + wides)
    Logger.debug("No Balls: " + noBalls)
    Logger.debug("Byes: " + byes)
    Logger.debug("Leg Byes: " + legByes)
    Logger.debug("Penalty Runs: " + penaltyRuns)
    Logger.debug("Total: " + total)
    
    new DbSaveExtras(
      noBalls, wides, legByes,
      byes, penaltyRuns, total
    )
    
  }
  
}