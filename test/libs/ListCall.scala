package libs

abstract class ListTarget
object Runs extends ListTarget { override val toString: String = "runs" }
object BattingAverage extends ListTarget { override val toString: String = "batavg" }

object Wickets extends ListTarget { override val toString: String = "wickets" }
object BowlingAverage extends ListTarget { override val toString: String = "bowlavg" }
object StrikeRate extends ListTarget { override val toString: String = "strkrate" }
object EconomyRate extends ListTarget { override val toString: String = "econrate" }

object Catches extends ListTarget { override val toString: String = "catches" }
object RunOuts extends ListTarget { override val toString: String = "runouts"}

class ListCall(target: String, season: Option[Int]) {
  
  def this(target: ListTarget, season: Option[Int]) = this(target.toString(), season)
  
  
}