package models.abstracts

abstract class BowlingRecord extends WithPublicKey with Deletable {
  
  val matchPlayer: MatchPlayer
  
  val overs: Int
  val extraBalls: Int
  val maidens: Int
  val runs: Int
  val wickets: Int
  
  val dismissals: Seq[Dismissal]
  
}