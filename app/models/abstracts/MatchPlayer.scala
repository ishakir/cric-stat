package models.abstracts

abstract class MatchPlayer extends WithPublicKey with Deletable {
  
  val battingPosition: Int
  
  val player: Player
  val matchIn: Match
  
  val battingRecord: BattingRecord
  val bowlingRecord: BowlingRecord
  
  val fieldingDismissals: Seq[Dismissal]
  
}