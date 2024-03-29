# Database schema

# --- !Ups

CREATE TABLE Season (
  year integer NOT NULL,
  PRIMARY KEY (year)
);

CREATE TABLE Team (
  team_id int NOT NULL AUTO_INCREMENT,
  name varchar(30) NOT NULL,
  PRIMARY KEY (team_id)
);

CREATE TABLE Extras (
  extras_id int NOT NULL AUTO_INCREMENT,
  noballs int NOT NULL,
  wides int NOT NULL,
  legbyes int NOT NULL,
  byes int NOT NULL,
  penalty_runs int NOT NULL,
  total int NOT NULL,
  PRIMARY KEY (extras_id)
);

CREATE TABLE Match (
  match_id int NOT NULL AUTO_INCREMENT,
  team int NOT NULL,
  season int NOT NULL,
  month int NOT NULL,
  day int NOT NULL,
  opponent varchar(30) NOT NULL,
  venue varchar(30),
  matchType varchar(30),
  scorers varchar(30),
  umpires varchar(30),
  highfield_extras int NOT NULL,
  non_highfield_extras int NOT NULL,
  highfield_batted_first int NOT NULL,
  PRIMARY KEY (match_id),
  FOREIGN KEY (season) REFERENCES Season(year),
  FOREIGN KEY (team) REFERENCES Team(team_id),
  FOREIGN KEY (highfield_extras) REFERENCES Extras(extras_id),
  FOREIGN KEY (non_highfield_extras) REFERENCES Extras(extras_id)
);

CREATE TABLE BowlingRecord (
  bowlrec_id int NOT NULL AUTO_INCREMENT,
  fullovers int NOT NULL,
  balls int NOT NULL,
  maidens int NOT NULL,
  runs int NOT NULL,
  wickets int NOT NULL,
  PRIMARY KEY (bowlrec_id)
);

CREATE TABLE Dismissal (
  dis_id int NOT NULL AUTO_INCREMENT,
  dismissal_type int NOT NULL,
  bowling_record int,
  PRIMARY KEY (dis_id),
  FOREIGN KEY (bowling_record) REFERENCES BowlingRecord(bowlrec_id)
);

CREATE TABLE BattingRecord (
  batrec_id int NOT NULL AUTO_INCREMENT,
  runs int NOT NULL,
  dismissal int,
  PRIMARY KEY (batrec_id),
  FOREIGN KEY (dismissal) REFERENCES Dismissal(dis_id)
);

CREATE TABLE Player (
  player_id int NOT NULL AUTO_INCREMENT,
  name varchar(20) NOT NULL,
  init varchar(20) NOT NULL,
  is_highfield int NOT NULL,
  PRIMARY KEY (player_id)
);

CREATE TABLE MatchPlayer (
  mp_id int NOT NULL AUTO_INCREMENT,
  batting_record int,
  bowling_record int,
  match int NOT NULL,
  batting_position int NOT NULL,
  player int NOT NULL,
  FOREIGN KEY (match) REFERENCES Match(match_id),
  FOREIGN KEY (batting_record) REFERENCES BattingRecord(batrec_id),
  FOREIGN KEY (bowling_record) REFERENCES BowlingRecord(bowlrec_id),
  FOREIGN KEY (player) REFERENCES Player(player_id),
  PRIMARY KEY (mp_id)
);

CREATE TABLE FielderDismissal (
  match_player int NOT NULL,
  dismissal int NOT NULL,
  FOREIGN KEY (match_player) REFERENCES MatchPlayer(mp_id),
  FOREIGN KEY (dismissal) REFERENCES Dismissal(dis_id)
);

# -- !Downs

DROP TABLE Season;
DROP TABLE Match;
DROP TABLE Team;
DROP TABLE BowlingRecord;
DROP TABLE Dismissal;
DROP TABLE BattingRecord;
DROP TABLE Player;
DROP TABLE MatchPlayer;
DROP TABLE FielderDismissal;
