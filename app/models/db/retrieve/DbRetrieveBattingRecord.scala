package models.db.retrieve

import anorm.~
import anorm.Id
import anorm.Pk
import anorm.RowParser
import anorm.SqlParser.get

import models.abstracts.BattingRecord
import models.abstracts.Dismissal
import models.abstracts.MatchPlayer

import utils.db.DbFinder
import utils.db.HasId

class DbRetrieveBattingRecord (
  iden: Pk[Long],
  val runs: Int,
  dismissalId: Int
) extends BattingRecord with HasId {
  
  val id: Long = iden.get
  
  val wasOut: Boolean = dismissal != null
  
  lazy val dismissal: Dismissal = if(dismissalId == DbRetrieveBattingRecord.nullInt) null
  								  else DbRetrieveDismissal.findById(dismissalId.toString)
  
  lazy val matchPlayer: MatchPlayer = {
    
    val playerList = DbRetrieveMatchPlayer.findFilteredByEqualsAttributes(
      Map(
        DbRetrieveMatchPlayer.battingRecordName -> id.toString
      )
    )
    
    // Should be only one
    if(playerList.size > 1) throw new IllegalStateException("More than one MatchPlayer for batting record")
    else if(playerList.size == 1) playerList.head
    else throw new IllegalStateException("Batting record has no MatchPlayer!")
    
  }
  
  override def toString(): String = {
    "Batting Record " + id + ": " + runs + " runs with dismissal " + dismissalId
  }
  
  
}

object DbRetrieveBattingRecord extends DbFinder[DbRetrieveBattingRecord]{
  
  // required by DbFinder
  val tableName: String = "BattingRecord"
  val idName: String = "batrec_id"
  
  // other fields
  val runsName: String = "runs"
  val dismissalIdName: String = "dismissal"
  
  val allFieldsParser: RowParser[DbRetrieveBattingRecord] = {
    get[Long](idName) ~ get[Int](runsName) ~ get[Option[Int]](dismissalIdName) map {
      case id ~ runs ~ dismissalId=>
        new DbRetrieveBattingRecord(Id(id), runs, optionToInt(dismissalId))
    }
  }
    
}