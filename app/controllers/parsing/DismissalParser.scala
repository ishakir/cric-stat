package controllers.parsing

import models.db.build.DbSaveDismissal
import models.DismissalType.fromString
import play.api.libs.json.JsValue
import play.api.Logger

object DismissalParser {
  
  val highfieldDismissalsName = "highfield_dismissals"
  val nonHighfieldDismissalsName = "opposition_dismissals"
  
  val batsmanName = "batsman"
  val howoutName = "howout"
  val bowlerName = "bowler"
  val fielderName = "fielder"
  
  def getDismissals(matchJson: JsValue, highfield: Boolean): Seq[DbSaveDismissal] = {
    
    if(highfield) {
      Logger.info("Parsing highfield dismissals")
    } else {
      Logger.info("Parsing non-highfield dismissals")
    }
    
    val jsonKey = {
      if(highfield) highfieldDismissalsName
      else nonHighfieldDismissalsName
    }
    
    val asJsonArray = (matchJson \ jsonKey).as[Seq[JsValue]]
    
    asJsonArray.map(br => parseDismissal(br))
    
  }
  
  def parseDismissal(disJson: JsValue): DbSaveDismissal = {
    
    val battingPosition = (disJson \ batsmanName).as[Int]
    val howout = (disJson \ howoutName).as[String]
    val bowlerPosition = (disJson \ bowlerName).as[Int]
    val fielderPosition = (disJson \ fielderName).as[Int]
    
    Logger.debug("Parsed dismissal of batsman no " + battingPosition)
    Logger.debug("They were " + howout + " with bowler no " + bowlerPosition + " and fielder no " + fielderPosition)
    
    new DbSaveDismissal(
        fromString(howout),
        bowlerPosition,
        battingPosition,
        fielderPosition
    )
    
  }
  
}