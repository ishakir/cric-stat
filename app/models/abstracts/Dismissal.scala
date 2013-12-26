package models.abstracts

import models.DismissalType

abstract class Dismissal extends WithPublicKey with Deletable {
  
  val battingRecord: BattingRecord
  val bowlingRecord: BowlingRecord
  
  val dismissalType: DismissalType
  val fielder: MatchPlayer
  
}