package controllers

import anorm.SQL
import play.api.mvc.Controller
import play.api.db.DB
import play.api.Play.current
import utils.rest.CricStatAction
import models.db.retrieve.DbRetrieveMatch
import models.abstracts.Match
import utils.db.MatchDeletion

object MatchRemoval extends Controller {
  
  def removeMatch(id: String) = CricStatAction { request =>
    
    // Find the match
    val theMatch: Match = DbRetrieveMatch.findById(id)
    
    // And delete it
    MatchDeletion.deleteMatch(theMatch)
    
    Ok
    
  }
  
}