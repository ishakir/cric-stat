package controllers.resource

import java.util.Calendar

import play.api.libs.json.JsValue
import play.api.libs.json.Json

import utils.resource.Jsonable
import utils.resource.DateConverter

class Date(
  date: Calendar
) extends Jsonable {
  
  lazy val toJson: JsValue = {
    
    Json.toJson(
      Map(
        Date.dayName -> DateConverter.intToString(date.get(Calendar.DAY_OF_WEEK)),
        Date.dateName -> date.get(Calendar.DAY_OF_MONTH).toString,
        Date.monthName -> date.get(Calendar.MONTH).toString,
        Date.yearName -> date.get(Calendar.YEAR).toString
      )
    )
    
  }
  
}

object Date {
  
  val resourceName: String = "date"
  
  val dateName = "date"
  val monthName = "month"
  val yearName = "year"
  val dayName = "day"
  
}