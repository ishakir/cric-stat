package utils.parsing

import models.db.build.DbSaveBattingRecord
import models.db.build.DbSaveDismissal
import models.db.build.DbSavePlayer
import models.db.build.DbSaveExtras
import models.db.build.DbSaveBowlingRecord

class ParsedTeamInfo(
  val players: Seq[DbSavePlayer],
  val bowlRecs: Seq[DbSaveBowlingRecord],
  val dismissals: Seq[DbSaveDismissal],
  val batRecs: Seq[DbSaveBattingRecord],
  val extras: DbSaveExtras
)