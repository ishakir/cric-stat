package models.abstracts

abstract class BattingRecord extends WithPublicKey with Deletable {
  
  val runs: Int
  val wasOut: Boolean
  
  val dismissal: Dismissal
  val matchPlayer: MatchPlayer
  
}