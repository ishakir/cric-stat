package utils.db

import play.api.Logger

class DeleteStatement(tableName: String, idName: String, id: String) {
  
  override lazy val toString = {
    
    val deleteFrom: String = "DELETE FROM "
    val where: String = " WHERE "
    val idString: String = idName+"='"+id+"'"
    val finish: String = ";"
    
    val query = deleteFrom + tableName + where + idString + finish
    
    Logger.info("Formed query '" + query + "'")
    
    query
    
  }
  
}