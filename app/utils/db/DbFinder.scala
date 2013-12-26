package utils.db

import anorm.RowParser
import anorm.SQL
import models.abstracts.WithPublicKey
import play.api.Logger
import play.api.Play.current
import play.api.db.DB

abstract class DbFinder[T >: Null <: WithPublicKey] {
  
  val tableName: String
  val idName: String
  
  val allFieldsParser: RowParser[T]
  
  val nullInt = -1
  
  def optionToObject[S >: Null](retrieved: Option[S]): S = {
    if(!retrieved.isEmpty) retrieved.get
    else null
  }
  
  def optionToInt(retrieved: Option[Int]): Int = {
    if(!retrieved.isEmpty) retrieved.get
    else nullInt
  }
  
  def findFilteredByEqualsAttributes(attributes: Map[String, String]): Seq[T] = findFilteredByAttributes(attributes, Map())
  
  def findFilteredByContainsAttributes(attributes: Map[String, String]): Seq[T] = findFilteredByAttributes(Map(), attributes)
  
  def findFilteredByAttributes(equalsAttributes: Map[String, String], containsAttributes: Map[String, String]): Seq[T] = DB.withConnection { implicit connection =>
    
    Logger.info("Asked to get from table " + tableName + " with equals attributes " + equalsAttributes + " with contains attributes "+ containsAttributes)
    
    val query: SelectStatement = new SelectStatement(tableName).addEqualsParameters(equalsAttributes).addContainsParameters(containsAttributes)
    val results = SQL(query.toString).as(allFieldsParser *)
    
    Logger.info("Found results with id: " + results.map(x => x.id))
    
    results
    
  }
  
  def findById(id: String): T = DB.withConnection { implicit connection =>
    
    Logger.info("Asked to get from table " + tableName + " with id " + id)
    
    val query: SelectStatement = new SelectStatement(tableName).addEqualsParameter(idName, id)
    val results: Seq[T] = SQL(query.toString).as(allFieldsParser *)
    
    // Id must be unique or non-existant
    if(!results.isEmpty) {
      val result = results.head
      Logger.info("Found result with id " + result.id)
      result
    } else {
      Logger.info("Found no result")
      null
    }
    
  }
  
  def deleteById(id: String): Unit = DB.withConnection { implicit connection =>
    
    Logger.info("Asked to delete from table " + tableName + " with id " + id)
    
    val query: DeleteStatement = new DeleteStatement(tableName, idName, id)
    SQL(query.toString).executeUpdate
    
  }
  
  def findAll(): Seq[T] = DB.withConnection { implicit connection =>
      
      Logger.info("Asked to get all from table " + tableName)
      
      val query: SelectStatement = new SelectStatement(tableName)
      val results = SQL(query.toString).as(allFieldsParser *)
      
      Logger.info("Found results with ids: " + results.map(x => x.id))
      
      results
  }
  
}