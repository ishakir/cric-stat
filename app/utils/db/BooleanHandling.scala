package utils.db

object BooleanHandling {
  
  def boolToInt(bool: Boolean): Int = {
    if(bool) 1
    else 0
  }
  
  def intToBool(int: Int): Boolean = {
    int == 1
  }
  
}