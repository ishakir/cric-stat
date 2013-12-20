package controllers.parsing

import models.db.build.DbSaveBattingRecord
import play.api.libs.json.JsValue
import play.api.Logger

object BattingRecordParser {
  
  val highfieldBattingRecordsName = "highfield_batting"
  val nonHighfieldBattingRecordsName = "opposition_batting"
  
  val positionName = "position"
  val runsName = "runs"
  
  def getBattingRecords(matchJson: JsValue, highfield: Boolean): Seq[DbSaveBattingRecord] = {
    
    if(highfield) {
      Logger.info("Parsing highfield batting records")
    } else {
      Logger.info("Parsing non-highfield batting records")
    }
    
    val jsonKey = {
      if(highfield) highfieldBattingRecordsName
      else nonHighfieldBattingRecordsName
    }
    
    val asJsonArray = (matchJson \ jsonKey).as[Seq[JsValue]]
    
    asJsonArray.map(br => parseBattingRecord(br))
    
  }  
    
  def parseBattingRecord(brJson: JsValue): DbSaveBattingRecord = {
    
    val position = (brJson \ positionName).as[Int]
    val runs = (brJson \ runsName).as[Int]
    
    Logger.debug("Parsed batsman at position " + position + " who scored " + runs)
    
    new DbSaveBattingRecord(position, runs)
    
  }
  
}