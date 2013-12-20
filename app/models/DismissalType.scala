package models

abstract class DismissalType { 
  def hasFielder(): Boolean
  def hasBowler(): Boolean
}

trait DismissalWithFielder extends DismissalType { def hasFielder(): Boolean = true }
trait DismissalWithoutFielder extends DismissalType { def hasFielder(): Boolean = false }

trait DismissalWithBowler extends DismissalType { def hasBowler(): Boolean = true }
trait DismissalWithoutBowler extends DismissalType { def hasBowler(): Boolean = false }

case object Caught extends DismissalWithFielder with DismissalWithBowler
case object Stumped extends DismissalWithFielder with DismissalWithBowler
case object RunOut extends DismissalWithFielder with DismissalWithoutBowler

case object Bowled extends DismissalWithoutFielder with DismissalWithBowler
case object LBW extends DismissalWithoutFielder with DismissalWithBowler
case object Retired extends DismissalWithoutFielder with DismissalWithoutBowler
case object TimedOut extends DismissalWithoutFielder with DismissalWithoutBowler
case object HandledBall extends DismissalWithoutFielder with DismissalWithBowler
case object HitTwice extends DismissalWithoutFielder with DismissalWithBowler
case object HitWicket extends DismissalWithoutFielder with DismissalWithBowler
case object ObstructingTheField extends DismissalWithoutFielder with DismissalWithoutBowler

object DismissalType {
  
  def fromString(string: String) = {
    string.toLowerCase match {
      case "caught" => Caught
      case "stumped" => Stumped
      case "runout" => RunOut
      case "bowled" => Bowled
      case "lbw" => LBW
      case "retired" => Retired
      case "timedout" => TimedOut
      case "handledball" => HandledBall
      case "hittwice" => HitTwice
      case "hitwicket" => HitWicket
      case "obstructingthefield" => ObstructingTheField
    }
  }
  
  def fromInt(int: Int): DismissalType = {
    int match {
      case 0 => Caught
      case 1 => Stumped
      case 2 => RunOut
      case 3 => Bowled
      case 4 => LBW
      case 5 => Retired
      case 6 => TimedOut
      case 7 => HandledBall
      case 8 => HitTwice
      case 9 => HitWicket
      case 10 => ObstructingTheField
    }
  }
  
  def toInt(dis: DismissalType): Int = {
    dis match {
      case Caught => 0
      case Stumped => 1
      case RunOut => 2
      case Bowled => 3
      case LBW => 4
      case Retired => 5
      case TimedOut => 6
      case HandledBall => 7
      case HitTwice => 8
      case HitWicket => 9
      case ObstructingTheField => 10
    }
  }
  
}