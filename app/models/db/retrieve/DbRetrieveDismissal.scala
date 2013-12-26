package models.db.retrieve

import anorm.~
import anorm.Id
import anorm.Pk
import anorm.RowParser
import anorm.SqlParser.get

import models.abstracts.BattingRecord
import models.DismissalType
import models.abstracts.Dismissal
import models.abstracts.MatchPlayer
import models.abstracts.BowlingRecord

import play.api.db.DB
import play.api.Play.current

import utils.db.DbFinder

class DbRetrieveDismissal(
  iden: Pk[Long],
  dismissalTypeNumber: Int,
  bowlingRecordId: Int
) extends Dismissal {
  
  val id: Long = iden.get
  
  lazy val battingRecord: BattingRecord = {
    
    val recordsList = DbRetrieveBattingRecord.findFilteredByEqualsAttributes(
      Map(
        DbRetrieveBattingRecord.dismissalIdName -> id.toString
      )
    )
    
    // There should be one in there
    if(recordsList.size > 1) throw new IllegalStateException("More than one batting record per dismissal!")
    else if(recordsList.size == 1) recordsList.head
    else throw new IllegalStateException("Dismissal has no Batting Record!")
    
  }
  
  lazy val fielder: MatchPlayer = DbFieldingQuery.findFielderByDismissal(id.toString)

  lazy val dismissalType: DismissalType = DismissalType.fromInt(dismissalTypeNumber)

  lazy val bowlingRecord: BowlingRecord = DbRetrieveBowlingRecord.findById(bowlingRecordId.toString)
  
  override def toString(): String = {
    "Dismissal " + id + ": " + dismissalType.toString + " for bowler " + bowlingRecordId
  }
  
  lazy val delete: Unit = DbRetrieveDismissal.deleteById(id.toString)
  
}

object DbRetrieveDismissal extends DbFinder[DbRetrieveDismissal] {
  
  // Required by DbFinder
  val tableName = "Dismissal"
  val idName = "dis_id"
    
  // Other Fields
  val dismissalTypeName = "dismissal_type"
  val bowlingRecordName = "bowling_record"
  
  val allFieldsParser: RowParser[DbRetrieveDismissal] = {
    get[Long](idName) ~ get[Int](dismissalTypeName) ~ get[Option[Int]](bowlingRecordName) map {
      case id ~ dismissalTypeNumber ~ bowlingRecordId =>
        new DbRetrieveDismissal(Id(id), dismissalTypeNumber, optionToInt(bowlingRecordId))
    }
  }

}