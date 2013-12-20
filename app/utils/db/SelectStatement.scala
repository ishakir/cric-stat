package utils.db

import play.api.Logger

class SelectStatement(
  tableName: String,
  columns: List[String],
  equalsParameters: Map[String, String],
  containsParameters: Map[String, String]) {

  def this(tableName: String) = this(tableName, List(), Map(), Map())

  def addSelectedColumn(column: String): SelectStatement = new SelectStatement(tableName, columns :+ column, equalsParameters, containsParameters)
  
  def addEqualsParameter(column: String, value: String): SelectStatement = addEqualsParameters(Map(column -> value))
  
  def addEqualsParameters(values: Map[String, String]): SelectStatement = new SelectStatement(tableName, columns, equalsParameters ++ values, containsParameters)
  
  def addContainsParameter(column: String, value: String): SelectStatement = addContainsParameters(Map(column -> value))
  
  def addContainsParameters(values: Map[String, String]): SelectStatement = new SelectStatement(tableName, columns, equalsParameters, containsParameters ++ values)
  
  override lazy val toString: String = {
    
    // start of statement
    val start = "SELECT "
    val columnCsv = if(columns.isEmpty) "*" else columns.mkString(",")
    val fromTable = " FROM " + tableName
    
    val where = if(equalsParameters.isEmpty && containsParameters.isEmpty) "" else " WHERE "
    
    // Any queries (currently only accepts AND, as we're looking at filtering)
    val equalsString = equalsParameters.toList.map {
      case (key, value) => key + "='" + value + "'"
    }.mkString(" AND ")
    
    val containsString = containsParameters.toList.map {
      case (key, value) => key + " LIKE " + "'%" + value + "%'"
    }.mkString(" AND ")
    
    val whereString = if(equalsString.length() != 0 && containsString.length() != 0) {
      equalsString + " AND " + containsString
    } else {
      equalsString + containsString
    }
    
    val finish = ";"
    
    val string = start + columnCsv + fromTable + where + whereString + finish
    
    Logger.info("Formed query '" + string + "'")
    
    string
    
  }
  
}