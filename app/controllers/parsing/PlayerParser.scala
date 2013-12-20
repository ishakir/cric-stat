package controllers.parsing

import models.db.build.DbSavePlayer
import play.api.libs.json.JsValue
import play.api.Logger

object PlayerParser {
  
  val highfieldPlayersName: String = "highfield_players"
  val nonHighfieldPlayersName: String = "opposition_players"
  
  def getPlayers(matchJson: JsValue, isHighfield: Boolean): Seq[DbSavePlayer] = {
    
    if(isHighfield) {
      Logger.info("Parsing highfield players")
    } else {
      Logger.info("Parsing non-highfield players")
    }
    
    val jsonKey = {
      if(isHighfield) highfieldPlayersName
      else nonHighfieldPlayersName
    }
    
    val playerJsons = (matchJson \ jsonKey).as[Array[JsValue]]
    
    playerJsons.map(pl => parsePlayer(pl, isHighfield))
    
  }
  
  def parsePlayer(playerJson: JsValue, isHighfield: Boolean): DbSavePlayer = {
    
    val batting_position = (playerJson \ "batting_pos").as[Int]
    
    val initial = (playerJson \ "initial").as[String]
    val surname = (playerJson \ "surname").as[String]
    
    Logger.debug("Found highfield player " + initial + "." + surname + " at position " + batting_position)
    
    new DbSavePlayer(batting_position, initial, surname, isHighfield)
    
  }
  
}