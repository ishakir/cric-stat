package models.abstracts

import utils.db.PerformancesQuery

abstract class Player extends WithPublicKey {
  
  val initial: String
  val surname: String
  
  val matches: Seq[Match]
  
  def performances(queryParams: PerformancesQuery): Seq[MatchPlayer]
  
  val isHighfield: Boolean
  
}