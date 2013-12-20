package models

class MatchMetadata(
  val year: Int,
  val month: Int,
  val day: Int,
  val opponent: String,
  val venue: String,
  val matchType: String,
  val scorers: String,
  val umpires: String,
  val team: String,
  val highfieldBattedFirst: Boolean
)