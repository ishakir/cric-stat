package models.abstracts

abstract class Team extends WithPublicKey with Deletable {
  
  val name: String
  val matches: Seq[Match]
  
}