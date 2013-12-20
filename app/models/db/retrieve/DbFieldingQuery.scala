package models.db.retrieve

import anorm.~
import anorm.RowParser
import anorm.SqlParser.get
import anorm.SQL

import models.abstracts.Dismissal
import models.abstracts.MatchPlayer

import play.api.db.DB
import play.api.Play.current
import play.api.Logger

import utils.db.SelectStatement

object DbFieldingQuery {
  
  val tableName = "FielderDismissal"
  
  val matchPlayerName: String = "match_player"
  val dismissalName: String = "dismissal"
  
  val allFieldsParser: RowParser[(Int, Int)] = {
    get[Int](matchPlayerName) ~ get[Int](dismissalName) map {
      case mpId ~ dismissalId =>
        (mpId, dismissalId)
    }
  }
    
  def findDismissalsByFielder(matchPlayerId: String): Seq[Dismissal] = DB.withConnection { implicit connection =>
    
    Logger.info("Asked to find Dismissals for fielder " + matchPlayerId)
    
    val query = new SelectStatement(tableName).addEqualsParameter(matchPlayerName, matchPlayerId)
    val pairs = SQL(query.toString).as(allFieldsParser *)
    val dismissals = pairs.map {
      case(mp_id, dismissalId) => DbRetrieveDismissal.findById(dismissalId.toString)
    }
    
    Logger.info("Found " + dismissals.size + " results")
    
    dismissals
    
  }
  
  def findFielderByDismissal(dismissalId: String): MatchPlayer = DB.withConnection { implicit connection =>
    
    Logger.info("Asked to find Fielder for dismissal " + dismissalId)
    
    val query = new SelectStatement(tableName).addEqualsParameter(dismissalName, dismissalId)
    val pairs = SQL(query.toString).as(allFieldsParser *)
    
    if(pairs.size > 1) throw new IllegalStateException("Dismissal " + dismissalId + "has more than one Fielder!")
    else if(pairs.isEmpty) {
      Logger.info("Found that dismissal " + dismissalId + " has no fielder")
      null
    }
    else {
      val (mp_id, dismissalId) = pairs.head
      val matchPlayer = DbRetrieveMatchPlayer.findById(mp_id.toString)
      Logger.info("Found MatchPlayer with id " + matchPlayer.id)
      matchPlayer
    }
    
  }
  
}