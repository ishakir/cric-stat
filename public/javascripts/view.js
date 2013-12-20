/*
 *Stuff up here is probably data stuff
 */
function playerName(player) {
  return player.initial + ". " + player.surname;
}

function convertMonth(month) {
  var monthArray = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
  return monthArray[month - 1];
}

function addQualifierToDate(date) {
  
  var stDays = [1, 21, 31];
  var ndDays = [2, 22];
  var rdDays = [3, 23];
  
  if(jQuery.inArray(date, stDays) > -1) {
    return date + "st";
  } else if(jQuery.inArray(date, ndDays) > -1) {
    return date + "nd";
  } else if(jQuery.inArray(date, rdDays) > -1) {
    return date + "rd";
  } else {
    return date + "th";
  }
  
}

function getHighfieldTeamName(data) {
  return "Highfield " + data.team;
}

function getOppositionTeamName(data) {
  return data.opponent;
}

function getDismissalForPlayer(battingposition, dismissals) {
  for(i = 0; i < dismissals.length; i++) {
    if(dismissals[i].batsman == battingposition) {
      return dismissals[i];
    }
  }
  return null;
}

/*
 * Stuff down here is probably view stuff 
 */
function producePlayerLink(player) {
  
  var name = playerName(player);
  
  // TODO this is horrible and will not work permenantly
  var link = "http://localhost:9000/player/" + player.id;
  
  if(player.is_highfield) {
    var playerLink = document.createElement("a");
    playerLink.href = link;
    playerLink.innerHTML = name;
  
    return playerLink;
  } else {
    
    return name;
    
  }
  
  
}

function produceMatchLink(matchoverview) {
  
  var opponent = matchoverview.opponent;
  
  // TODO this is horrible and will not work permenantly
  var link = "http://localhost:9000/match/" + matchoverview.id;
  
  var matchLink = document.createElement("a");
  matchLink.href = link;
  matchLink.innerHTML = opponent;
  
  return matchLink;
  
}

function addPlayerToTable(player, table) {
  
  var playerLink = producePlayerLink(player);
    
  var rowCount = table.rows.length;
  var row = table.insertRow(rowCount);
  row.insertCell(0).appendChild(playerLink);

}

function addPlayersToTables(players, table1, table2, table3, table4) {
  
  var table_array = [table1, table2, table3, table4];
  
  // Split players in half
  for(var i = 0; i < players.length; i++) {
    addPlayerToTable(players[i], table_array[i % table_array.length]);
  }
  
}

function appendTableFromListData(table, data) {
  
  for(i = 0; i < data.length; i++) {
              
    var record = data[i];
    var player = record.player;

    var playerLink = producePlayerLink(player);
    var value = record.value;

    var rowCount = table.rows.length;

    var row = table.insertRow(rowCount);

    row.insertCell(0).appendChild(playerLink);
    row.insertCell(1).innerHTML = value;
              
  }
  
}

function writePlaceAndTime(venue, date, paragraph) {
  paragraph.innerHTML = "Played at "+venue+" on "+date.day+" "+ addQualifierToDate(date.date)+" "+convertMonth(date.month)+" "+date.year;
}

function writeVersus(team, opposition, header) {
  header.innerHTML = "Highfield "+team+" v "+opposition;
}

function getDismissalInfo(dismissal, bowlingPlayers) {
  
  if(dismissal == null) {
    return ["Not", "Out"];
  } else if(dismissal.howout == "lbw") {
    return ["lbw", playerName(bowlingPlayers[dismissal.bowler - 1].player)];
  } else if(dismissal.howout == "bowled") {
    return ["", "b. " + playerName(bowlingPlayers[dismissal.bowler - 1].player)];
  } else if(dismissal.howout == "caught") {
    return ["c. " + playerName(bowlingPlayers[dismissal.fielder - 1].player), "b. " + playerName(bowlingPlayers[dismissal.bowler - 1].player)];
  } else if(dismissal.howout == "runout") {
    var fielder = dismissal.fielder;
    if(fielder == null) {
      return ["Run Out", ""];
    } else {
      return ["Run Out", playerName(bowlingPlayers[dismissal.fielder - 1].player)];
    }
  } else {
    return ["Not", "Implemented"];
  }
  
}

function formBattingArray(battingplayers, bowlingplayers, dismissals, battingrecords) {
  
  var fullArray = [];
  
  for(var i = 0; i < battingplayers.length; i++) {
    
    var player = battingplayers[i];
  
    var playerDescription = producePlayerLink(player.player);
    var battingRecord = battingrecords[i];
  
    if(!battingRecord) {
      fullArray.push([playerDescription, "d.n.b", "", ""]);
    } else {
      
      var runs = battingRecord.runs;
      var dismissal = getDismissalForPlayer(i + 1, dismissals);
      var dismissalInfo = getDismissalInfo(dismissal, bowlingplayers);
  
      fullArray.push([playerDescription, dismissalInfo[0], dismissalInfo[1], runs]);
    }
    
  }
  
  return fullArray;
  
}

function populateBattingTable(table, matchData, highfield) {
  
  var battingArray;
  
  if(!highfield) {
    battingArray = formBattingArray(matchData.opposition_players, matchData.highfield_players, matchData.non_highfield_dismissals, matchData.opposition_batting);
  } else {
    battingArray = formBattingArray(matchData.highfield_players, matchData.opposition_players, matchData.highfield_dismissals, matchData.highfield_batting);
  }
  
  for(var i = 0; i < battingArray.length; i++) {
    
    var rowCount = table.rows.length;
    var row = table.insertRow(rowCount);
  
    if(highfield) {
      row.insertCell(0).appendChild(battingArray[i][0]);
    } else {
      row.insertCell(0).innerHTML = battingArray[i][0];
    }
    
    row.insertCell(1).innerHTML = battingArray[i][1];
    row.insertCell(2).innerHTML = battingArray[i][2];
    row.insertCell(3).innerHTML = battingArray[i][3];
  
  }
  
  
}

function formBowlingArray(players, bowlers) {
  
  var fullArray = [];
  
  for(var i = 0; i < bowlers.length; i++) {
    
    var bowler = bowlers[i];
  
    var playerDescription = producePlayerLink(players[bowler.position - 1].player);
    
    var overs;
    if(bowler.extraBalls == 0) {
      overs = bowler.fullovers;
    } else {
      overs = bowler.fullovers + "." + bowler.extraballs;
    }
    
    fullArray.push([playerDescription, overs, bowler.maidens, bowler.runs, bowler.wickets]);
    
  }
  
  return fullArray;
  
}

function populateBowlingTable(table, matchData, highfield) {
  
  var bowlingArray;
  
  if(!highfield) {
    bowlingArray = formBowlingArray(matchData.opposition_players, matchData.opposition_bowling);
  } else {
    bowlingArray = formBowlingArray(matchData.highfield_players, matchData.highfield_bowling);
  }
  
  for(var i = 0; i < bowlingArray.length; i++) {
    
    var rowCount = table.rows.length;
    var row = table.insertRow(rowCount);
    
    console.log(bowlingArray);
    
    if(highfield) {
      row.insertCell(0).appendChild(bowlingArray[i][0]);
    } else {
      row.insertCell(0).innerHTML = bowlingArray[i][0];
    }
    
    row.insertCell(1).innerHTML = bowlingArray[i][1];
    row.insertCell(2).innerHTML = bowlingArray[i][2];
    row.insertCell(3).innerHTML = bowlingArray[i][3];
    row.insertCell(4).innerHTML = bowlingArray[i][4];
  
  }
  
}