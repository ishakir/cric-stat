package utils.db

import play.api.Logger

class InsertStatement(tableName: String, values: Map[String, String]) {
  
  def this(tableName: String) = this(tableName, Map())
  
  def addValue(column: String, value: String) = new InsertStatement(tableName, values ++ Map(column -> value))
  
  override lazy val toString = {
    
    require(!values.isEmpty)
    
    val start = "INSERT INTO " + tableName + " "
    
    val keys = values.keySet.toList
    val innerValues = keys.map(x => values.get(x).get)
    val quotedValues = innerValues.map(x => "'" + x + "'")
    
    val columns = "(" + keys.mkString(",") + ")"
    val valueList = "(" + quotedValues.mkString(",") + ")"
    
    val valuesString = " VALUES "
      
    val string = start + columns + valuesString + valueList
    
    Logger.debug("Formed query '" + string + "'")
    
    string
    
  }
  
}