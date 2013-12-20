package utils.resource

object LinkGenerator {
  
  // Constructing Paths
  val pathSeparator = "/"
  
  // Constructing Queries
  val queryStart = "?"
  val querySeparator = "#"
  val equals = "="
  
  // Roots
  val matchName = "match"
  val matchRoot = pathSeparator + matchName
  
  val playerName = "player"
  val playerRoot = pathSeparator + playerName
  
  val teamName = "team"
  val teamRoot = pathSeparator + teamName
  
  // Query Parameters
  val year = "year"
  val team = "teamId"
  val name = "nameQuery"
  
  def linkToMatches: String = matchRoot
  
  def linkToMatchesWithYear(yearVal: String): String = matchRoot + queryStart + year + equals + yearVal
  
  def linkToMatchesWithTeamId(teamId: String): String = matchRoot + queryStart + team + equals + teamId
  
  def linkToMatches(yearVal: String, teamId: String) = {
    matchRoot + queryStart + year + equals + yearVal + querySeparator + team + equals + teamId
  }
  
  def linkToMatch(matchId: String) = matchRoot + pathSeparator + matchId
  
  def linkToPlayers: String = playerRoot
  
  def linkToPlayer(nameQuery: String) = playerRoot + queryStart + name + equals + nameQuery
  
  def linkToPlayerProfile(playerId: String) = playerRoot + pathSeparator + playerId
  
  def linkToTeams: String = teamRoot
  
}