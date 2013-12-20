package libs

import java.io.File
import play.api.libs.json.JsValue
import scala.io.Source
import play.api.libs.json.Json
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.ws.WS
import play.api.libs.ws.Response

class MatchToPost(file: File) {
  
  assert(file.exists(), "ERROR: File provided to MatchToPost doesn't exist")
  
  lazy val jsonContent: JsValue = {
    
    // Get file content as string
    val fileAsString = Source.fromFile(file).mkString
    
    // Parse it into our JSON format
    Json.parse(fileAsString)
    
  }
  
  def post(ip: String, port: String): Response = {
    await(WS.url("http://"+ip+":"+port+"/api/match").post(jsonContent))
  }
  
  def postIsOk(ip: String, port: String): Boolean = {
    post(ip, port).status == OK
  }
  
}