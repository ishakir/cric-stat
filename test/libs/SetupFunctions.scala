package libs

import play.api.test._
import play.api.test.Helpers._
import play.api.libs.ws.WS
import java.io.File

object SetupFunctions {
  
  def addHambledonGame(): Unit = {
      val hambledonGame = new MatchToPost(new File("data/2013/Hambledon.csv.json"))
      if(!hambledonGame.postIsOk("localhost", "3333")) {
        throw new IllegalStateException("ERROR: Post of Hambledon game failed")
      }
    }
    
    def intermediary[T](code: => T) = {
      addHambledonGame
      code
    }
    
    def memDBWithOneMatch[T](code: => T) =
      running(
        TestServer(
          3333, 
          FakeApplication(
            additionalConfiguration = Map(
              "db.default.driver" -> "org.h2.Driver",
              "db.default.url" -> "jdbc:h2:mem:test;MODE=MySQL"
            )
          )
        )
      )(
        intermediary(code)
      )
  
}