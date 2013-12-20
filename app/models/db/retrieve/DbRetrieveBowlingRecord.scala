package models.db.retrieve

import anorm.~
import anorm.Id
import anorm.Pk
import anorm.RowParser
import anorm.SqlParser.get

import models.abstracts.Dismissal
import models.abstracts.MatchPlayer
import models.abstracts.BowlingRecord

import utils.db.DbFinder
import utils.db.HasId

class DbRetrieveBowlingRecord (
  iden: Pk[Long],
  val overs: Int,
  val extraBalls: Int,
  val maidens: Int,
  val runs: Int,
  val wickets: Int
) extends BowlingRecord with HasId {
  
  val id: Long = iden.get
  
  lazy val dismissals: Seq[Dismissal] = {
    DbRetrieveDismissal.findFilteredByEqualsAttributes(
      Map(
        DbRetrieveDismissal.bowlingRecordName -> id.toString  
      )
    )
  }
  
  lazy val matchPlayer: MatchPlayer = {
    
    val playerList = DbRetrieveMatchPlayer.findFilteredByEqualsAttributes(
      Map(
        DbRetrieveMatchPlayer.bowlingRecordName -> id.toString
      )
    )
    
    // Should be only one
    if(playerList.size > 1) throw new IllegalStateException("More than one MatchPlayer for bowling record")
    else if(playerList.size == 1) playerList.head
    else throw new IllegalStateException("Bowling record has no MatchPlayer!")
    
  }
  
  override def toString() = {
    "Bowling Record " + id + ": " + overs + "." + extraBalls + "-" + maidens + "-" + runs + "-" + wickets
  }
  
}

object DbRetrieveBowlingRecord extends DbFinder[DbRetrieveBowlingRecord] {
  
  // Required by DbFinder
  val tableName = "BowlingRecord"
  val idName = "bowlrec_id"
    
  // Other usefuls
  val oversName = "fullovers"
  val ballsName = "balls"
  val maidensName = "maidens"
  val runsName = "runs"
  val wicketsName = "wickets"
    
  val allFieldsParser: RowParser[DbRetrieveBowlingRecord] = {
    get[Long](idName) ~ get[Int](oversName) ~ get[Int](ballsName) ~ get[Int](maidensName) ~ get[Int](runsName) ~ get[Int](wicketsName) map {
      case id ~ overs ~ balls ~ maidens ~ runs ~ wickets =>
        new DbRetrieveBowlingRecord(Id(id), overs, balls, maidens, runs, wickets)
    }
  }
  
}