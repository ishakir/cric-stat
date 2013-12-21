package utils.rest

import models.db.retrieve.DbRetrieveSeason
import models.abstracts.Season

object InputValidation {
  
  def seasonIsInDb(season: String): Boolean = {
    val seasonEntities: Seq[Season] = DbRetrieveSeason.findAll()
    val seasons: Seq[Int] = seasonEntities.map(entity => entity.getYear)
    seasons.contains(season.toInt)
  }
  
}