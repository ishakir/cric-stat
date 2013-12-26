package models.abstracts

abstract class Season extends WithPublicKey with Deletable {
  
  def getYear(): Int
  val matches: Seq[Match]
  
}