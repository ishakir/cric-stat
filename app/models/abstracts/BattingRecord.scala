package models.abstracts

abstract class BattingRecord extends WithPublicKey {
  
  val runs: Int
  val wasOut: Boolean
  
  val dismissal: Dismissal
  val matchPlayer: MatchPlayer
  
}