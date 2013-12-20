package controllers.processing

import models.abstracts.MatchPlayer
import models.abstracts.BattingRecord
import models.abstracts.BowlingRecord
import models.abstracts.Dismissal
import models.Caught
import models.RunOut

class ProcessorPackage[T](
  val entityStep: MatchPlayer => T,
  val foldStep: (Int, T) => Int)

object ProcessorPackage {

  lazy val runs: ProcessorPackage[BattingRecord] = new ProcessorPackage(
    mp => mp.battingRecord,
    (a: Int, b: BattingRecord) => a + b.runs
  )

  lazy val wickets: ProcessorPackage[BowlingRecord] = new ProcessorPackage(
    mp => mp.bowlingRecord,
    (a: Int, b: BowlingRecord) => a + b.wickets
  )

  lazy val catches: ProcessorPackage[Seq[Dismissal]] = new ProcessorPackage(
    mp => mp.fieldingDismissals,
    (a: Int, dismissals: Seq[Dismissal]) => a + dismissals.filter(dis => dis.dismissalType == Caught).size
  )

  lazy val runouts: ProcessorPackage[Seq[Dismissal]] = new ProcessorPackage[Seq[Dismissal]](
    mp => mp.fieldingDismissals,
    (a: Int, dismissals: Seq[Dismissal]) => a + dismissals.filter(dis => dis.dismissalType == RunOut).size
  )

}