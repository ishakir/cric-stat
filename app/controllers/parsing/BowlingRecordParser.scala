package controllers.parsing

import models.db.build.DbSaveBowlingRecord
import play.api.libs.json.JsValue
import play.api.Logger

object BowlingRecordParser {
  
  val highfieldBowlingRecordsName = "highfield_bowling"
  val nonHighfieldBowlingRecordsName = "opposition_bowling"
  
  val positionName = "position"
    
  val fulloversName = "fullovers"
  val extraBallsName = "extraballs"
  val maidensName = "maidens"
  val runsName = "runs"
  val wicketsName = "wickets"
  
  def getBowlingRecords(matchJson: JsValue, highfield: Boolean): Seq[DbSaveBowlingRecord] = {
    
    if(highfield) {
      Logger.info("Parsing highfield bowling records")
    } else {
      Logger.info("Parsing non highfield bowling records")
    }
    
    val jsonKey = {
      if(highfield) highfieldBowlingRecordsName
      else nonHighfieldBowlingRecordsName
    }
    
    val asJsonArray = (matchJson \ jsonKey).as[Seq[JsValue]]
    
    asJsonArray.map(br => parseBowlingRecord(br))
    
  }
  
  def parseBowlingRecord(brJson: JsValue): DbSaveBowlingRecord = {
    
    val position = (brJson \ positionName).as[Int]
    
    val fullovers = (brJson \ fulloversName).as[Int]
    val extraBalls = (brJson \ extraBallsName).as[Int]
    val maidens = (brJson \ maidensName).as[Int]
    val runs = (brJson \ runsName).as[Int]
    val wickets = (brJson \ wicketsName).as[Int]
    
    Logger.debug("Parsed bowling record for player number: " + position)
    Logger.debug(fullovers+"."+extraBalls+"-"+maidens+"-"+runs+"-"+wickets)
    
    new DbSaveBowlingRecord(
      position,
      fullovers,
      extraBalls,
      maidens,
      runs,
      wickets
    )
    
  }
  
}