package models.abstracts

import models.DismissalType

abstract class Dismissal extends WithPublicKey {
  
  val battingRecord: BattingRecord
  val bowlingRecord: BowlingRecord
  
  val dismissalType: DismissalType
  val fielder: MatchPlayer
  
}