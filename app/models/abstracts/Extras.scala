package models.abstracts

abstract class Extras extends WithPublicKey with Deletable {
  
  val noBalls: Int
  val wides: Int
  val legByes: Int
  val byes: Int
  val penaltyRuns: Int
  val total: Int
  
}