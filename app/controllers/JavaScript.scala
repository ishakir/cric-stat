package controllers

import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.Routes

object JavaScript extends Controller {
  
  def javascriptRoutes = Action { implicit request =>
    import routes.javascript._
    Ok(
      Routes.javascriptRouter("jsRoutes")(
        MatchInput.putMatch,
        MatchOutput.matchQuery,
        MatchOutput.individualMatch,
        PlayerOutput.playerQuery,
        PlayerOutput.playerProfile,
        ListOutput.rankedList,
        SeasonOutput.allSeasons,
        TeamOutput.allTeams
      )
    ).as("text/javascript") 
  }
  
}