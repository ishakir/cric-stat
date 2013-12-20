package unit

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

import utils.db.SelectStatement

@RunWith(classOf[JUnitRunner])
class SelectStatementTest extends Specification {
  
  val tableName = "myTable"
    
  val column1 = "column1"
  val column2 = "column2"
  val column3 = "column3"
  val column4 = "column4"
  
  val withTableName = new SelectStatement(tableName)
    
  "SelectStatement" should {
    
    "select all" in {
      val queryString = "SELECT * FROM myTable;"
      withTableName.toString must beEqualTo(queryString)
    }
    
    "select a column" in {
      val queryString = "SELECT column1 FROM myTable;"
      val newQuery = withTableName.addSelectedColumn(column1)
      newQuery.toString must beEqualTo(queryString)
    }
    
    "select two columns" in {
      val queryString = "SELECT column1,column2 FROM myTable;"
      val newQuery = withTableName.addSelectedColumn(column1).addSelectedColumn(column2)
      newQuery.toString must beEqualTo(queryString)
    }
    
    "add an equals condition" in {
      val queryString = "SELECT * FROM myTable WHERE column1='hello';"
      val newQuery = withTableName.addEqualsParameter(column1, "hello")
      newQuery.toString must beEqualTo(queryString)
    }
    
    "add two equals conditions sequentially" in {
      val queryString = "SELECT * FROM myTable WHERE column1='hello' AND column2='goodbye';"
      val newQuery = withTableName.addEqualsParameter(column1, "hello").addEqualsParameter(column2, "goodbye")
      newQuery.toString must beEqualTo(queryString)
    }
    
    "add two equals conditions in bulk" in {
      val queryString = "SELECT * FROM myTable WHERE column1='hello' AND column2='goodbye';"
      val parameterMap = Map(column1 -> "hello", column2 -> "goodbye")
      val newQuery = withTableName.addEqualsParameters(parameterMap)
      newQuery.toString must beEqualTo(queryString)
    }
    
    "add two columns and two equals conditions" in {
      val queryString = "SELECT column3,column4 FROM myTable WHERE column1='hello' AND column2='goodbye';"
      val parameterMap = Map(column1 -> "hello", column2 -> "goodbye")
      val withColumns = withTableName.addSelectedColumn(column3).addSelectedColumn(column4)
      val withParameters = withColumns.addEqualsParameters(parameterMap)
      withParameters.toString must beEqualTo(queryString)
    }
    
    "add a contains condition" in {
      val queryString = "SELECT * FROM myTable WHERE column3 LIKE '%john%';"
      val newQuery = withTableName.addContainsParameter(column3, "john")
      newQuery.toString must beEqualTo(queryString)
    }
    
    "add two contains conditions in bulk" in {
      val queryString = "SELECT * FROM myTable WHERE column3 LIKE '%john%' AND column4 LIKE '%simon%';"
      val parameterMap = Map(column3 -> "john", column4 -> "simon")
      val newQuery = withTableName.addContainsParameters(parameterMap)
      newQuery.toString must beEqualTo(queryString)
    }
    
    "add two contains conditions and two equals conditions" in {
      val queryString = "SELECT * FROM myTable WHERE column1='hello' AND column2='goodbye' AND column3 LIKE '%john%' AND column4 LIKE '%simon%';"
      val equalsMap = Map(column1 -> "hello", column2 -> "goodbye")
      val containsMap = Map(column3 -> "john", column4 -> "simon")
      val withEquals = withTableName.addEqualsParameters(equalsMap)
      val withContains = withEquals.addContainsParameters(containsMap) 
	  withContains.toString must beEqualTo(queryString)
    }
    
  }
  
}