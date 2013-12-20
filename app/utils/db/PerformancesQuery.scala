package utils.db

import models.db.retrieve.DbRetrieveMatch
import models.db.retrieve.DbRetrieveMatchPlayer

import play.api.Logger

class PerformancesQuery(playerId: Long, season: Option[String]) {
  
  override lazy val toString: String = {
    
    // If season is empty use the tried and tested method
    if(season.isEmpty) {
      val selectStatement = new SelectStatement(DbRetrieveMatchPlayer.tableName)
      val withParameter = selectStatement.addEqualsParameter(DbRetrieveMatchPlayer.playerName, playerId.toString)
      withParameter.toString
    }
    // Otherwise form an aliased query
    else {
      formAliasedQuery(playerId, season.get)
    }
    
  }
  
  def formAliasedQuery(playerId: Long, season: String) = {
    
    val select = "SELECT "
      
    val values = PerformancesQuery.matchPlayerTableAlias + ".* "
    
    val from = "FROM "
    
    val tableAliases: Map[String, String] = Map(
      DbRetrieveMatchPlayer.tableName -> PerformancesQuery.matchPlayerTableAlias,
      DbRetrieveMatch.tableName -> PerformancesQuery.matchTableAlias
    )
    
    val where = " WHERE "
    
    val optionsMap = Map(
        "mp.player" -> playerId.toString,
        "m.season" -> season
    )
    
    val tablesString = tableAliases.toList.map {
      case (tableName, alias) => tableName + " AS " + alias
    }.mkString(", ")
    
    val optionsString = optionsMap.toList.map {
      case(name, value) => name + "='" + value + "'"
    }.mkString(" AND ")
    
    val finish = ";"
      
    val query = select + values + from + tablesString + where + optionsString + finish;
    
    Logger.info("Formed query '" + query + "'")
    
    query
    
  }
  
  
}

object PerformancesQuery {
  
  val matchPlayerTableAlias: String = "mp"
  val matchTableAlias: String = "m"
  
}