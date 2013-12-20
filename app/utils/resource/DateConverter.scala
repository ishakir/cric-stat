package utils.resource

import java.util.Calendar
import play.api.Logger

object DateConverter {
  
  val mondayName: String = "Monday"
  val tuesdayName: String = "Tuesday"
  val wednesdayName: String = "Wednesday"
  val thursdayName: String = "Thursday"
  val fridayName: String = "Friday"
  val saturdayName: String = "Saturday"
  val sundayName: String = "Sunday"
  
  def intToString(i: Int) = {
    
    val string = i match {
      case Calendar.MONDAY => mondayName
      case Calendar.TUESDAY => tuesdayName
      case Calendar.WEDNESDAY => wednesdayName
      case Calendar.THURSDAY => thursdayName
      case Calendar.FRIDAY => fridayName
      case Calendar.SATURDAY => saturdayName
      case Calendar.SUNDAY => sundayName
    }
    
    Logger.debug("Converted int " + i + " to " + string)
    
    string
    
  }
  
  
}