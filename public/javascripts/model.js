function rankedList(target, season, succfunc, errorfunc) {
  
  jsRoutes.controllers.ListOutput.rankedList(target, season).ajax({
    success: succfunc,
    error: errorfunc
  });
  
}

function matchQuery(year, teamId, succfunc, errorfunc) {
  
  jsRoutes.controllers.MatchOutput.matchQuery(year, teamId).ajax({
    success: succfunc,
    error: errorfunc
  });
  
}

function individualMatch(matchId, succfunc, errorfunc) {
  
  jsRoutes.controllers.MatchOutput.individualMatch(matchId).ajax({
    success: succfunc,
    error: errorfunc
  });
  
}

function playerQuery(nameQuery, succfunc, errorfunc) {
  
  jsRoutes.controllers.PlayerOutput.playerQuery(nameQuery).ajax({
    success: succfunc,
    error: errorfunc
  });
  
}

function playerProfile(playerId, succfunc, errorfunc) {
  
  jsRoutes.controllers.PlayerOutput.playerProfile(playerId).ajax({
    success: succfunc,
    error: errorfunc
  });
  
}

function allSeasons(succfunc, errorfunc) {
  
  jsRoutes.controllers.SeasonOutput.allSeasons().ajax({
    success: succfunc,
    error: errorfunc
  });
  
}

function allTeams(succfunc, errorfunc) {
  
  jsRoutes.controllers.TeamOutput.allTeams().ajax({
    success: succfunc,
    error: errorfunc
  });
  
}
