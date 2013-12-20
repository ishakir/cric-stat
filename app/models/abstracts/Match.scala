package models.abstracts

import java.util.Calendar

abstract class Match extends WithPublicKey {
  
  val season: Season
  val date: Calendar
  
  val team: Team
  val opponentName: String
  val location: String
  val matchType: String
  
  val scorers: String
  val umpires: String
  
  val highfieldWon: Boolean
  val highfieldBattedFirst: Boolean
  
  val highfieldExtras: Extras
  val nonHighfieldExtras: Extras
  
  val highfieldPlayers: Map[Int, Player]
  val nonHighfieldPlayers: Map[Int, Player]
  
  val highfieldBatsmen: Seq[MatchPlayer]
  val highfieldBowlers: Seq[MatchPlayer]
  
  val nonHighfieldBatsmen: Seq[MatchPlayer]
  val nonHighfieldBowlers: Seq[MatchPlayer]
  
}