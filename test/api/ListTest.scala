package api

import org.junit.runner.RunWith
import org.specs2.mutable._
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.ws.WS
import java.io.File
import libs.SetupFunctions.memDBWithOneMatch

@RunWith(classOf[JUnitRunner])
class ListTest extends SpecificationWithJUnit {

  // Simple routes stuff
  "The list api" should {

    "allow a get with runs" in memDBWithOneMatch {
      await(WS.url("http://localhost:3333/api/list?target=runs").get).status must equalTo(OK)
    }
    
    "allow a get with wickets" in memDBWithOneMatch {
      await(WS.url("http://localhost:3333/api/list?target=wickets").get).status must equalTo(OK)
    }
    
    "allow a get with catches" in memDBWithOneMatch {
      await(WS.url("http://localhost:3333/api/list?target=catches").get).status must equalTo(OK)
    }
    
    "allow a get with runouts" in memDBWithOneMatch {
      await(WS.url("http://localhost:3333/api/list?target=runouts").get).status must equalTo(OK)
    }
    
    "allow a get with batting average" in memDBWithOneMatch {
      await(WS.url("http://localhost:3333/api/list?target=batavg").get).status must equalTo(OK)
    }
    
    "allow a get with bowling average" in memDBWithOneMatch {
      await(WS.url("http://localhost:3333/api/list?target=bowlavg").get).status must equalTo(OK)
    }
    
    "allow a get with strike rate" in memDBWithOneMatch {
      await(WS.url("http://localhost:3333/api/list?target=strkrate").get).status must equalTo(OK)
    }
    
    "allow a get with economy rate" in memDBWithOneMatch {
      await(WS.url("http://localhost:3333/api/list?target=econrate").get).status must equalTo(OK)
    }
    
    "not allow a get with bhyt" in memDBWithOneMatch {
      await(WS.url("http://localhost:3333/api/list?target=bhyt").get).status must equalTo(BAD_REQUEST)
    }
    
    "allow a get with season parameter" in memDBWithOneMatch {
      await(WS.url("http://localhost:3333/api/list?target=runs&season=2013").get).status must equalTo(OK)
    }
    
    "not allow a get with season not in db" in memDBWithOneMatch {
      await(WS.url("http://localhost:3333/api/list?target=runs&season=2012").get).status must equalTo(BAD_REQUEST)
    }
    
  }

}