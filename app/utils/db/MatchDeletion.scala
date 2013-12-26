package utils.db

import models.abstracts.Match
import models.db.retrieve.DbFieldingQuery

object MatchDeletion {
  
  def deleteMatch(theMatch: Match) = {
    
    // We need to handle deletion of all the match players first
    // this is (unfortunately) rather non-trivial
    val allPlayers = 
      (theMatch.highfieldPlayers.values ++ theMatch.nonHighfieldPlayers.values)
    
    // We need to capture a lot of stuff
    val allBattingRecords = 
      (theMatch.highfieldBatsmen ++ theMatch.nonHighfieldBatsmen)
      .map(bat => bat.battingRecord)
      
    val allBowlingRecords = 
      (theMatch.highfieldBowlers ++ theMatch.nonHighfieldBowlers)
      .map(bowl => bowl.bowlingRecord)
      
    val allDismissals = allBattingRecords
      .filter(batrec => batrec.dismissal != null)
      .map(batrec => batrec.dismissal)
      
    val season = theMatch.season
    val team = theMatch.team
    val highfieldExtras = theMatch.highfieldExtras
    val nonHighfieldExtras = theMatch.nonHighfieldExtras
    
    // The very bottom of the pile is the "FielderDismissals", get's find
    // all the dismissals and delete the "FielderDismissals"
    allDismissals.foreach(dis => DbFieldingQuery.deleteByDismissal(dis.id.toString))
    
    // Now delete all the match players
    (theMatch.highfieldMatchPlayers ++ theMatch.nonHighfieldMatchPlayers)
      .foreach(mp => mp.delete)
      
    // Delete the match
    theMatch.delete
    highfieldExtras.delete
    nonHighfieldExtras.delete
    
    if(season.matches.length == 0) season.delete
    if(team.matches.length == 0) team.delete
      
    // Now delete all the other artifacts in the only logical order
    allBattingRecords.foreach(batrec => batrec.delete)
    allDismissals.foreach(dis => dis.delete)
    allBowlingRecords.foreach(bowlrec => bowlrec.delete)
    
    // If any of the players have no matches, delete them
    allPlayers.foreach(player =>
      if (player.performances(new PerformancesQuery(player.id, None)) == 0) {
        player.delete
      }
    )
    
  }
  
}