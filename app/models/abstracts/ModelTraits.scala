package models.abstracts

trait WithPublicKey {
  
  val id: Long
  
}

trait Deletable {
  
  def delete: Unit
  
}