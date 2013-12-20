package controllers.processing

import models.abstracts.MatchPlayer
import models.abstracts.Player

import utils.db.PerformancesQuery

object PlayerProcessingFunctions {
  
  def processList[T](list: Seq[Player], season: Option[String], funcs: ProcessorPackage[T]): Seq[(Player, Int)] = {
    val playerAndPerf: Seq[(Player, Seq[MatchPlayer])] = list.map(player => (player, player.performances(new PerformancesQuery(player.id, season))))
    val playerWithInt: Seq[(Player, Int)] = playerAndPerf.map { case (player, performances) => (player, processPlayer(performances, funcs)) }
    val filtered: Seq[(Player, Int)] = playerWithInt.filter { case (player, int) => int != 0}
    val sorted: Seq[(Player, Int)] = filtered.sortBy { case (player, int) => -int}
    sorted
  }
  
  def processPlayer[T](performances: Seq[MatchPlayer], funcs: ProcessorPackage[T]): Int = {
    
    val entities: Seq[T] = performances.map(perf => funcs.entityStep(perf))
    val filtered: Seq[T] = entities.filter(item => item != null)
    filtered.foldLeft(0)(funcs.foldStep)
    
  }
  
}