package controllers

import play.api.mvc.Action
import play.api.mvc.Controller

object FrontEnd extends Controller {
  
  def index() = Action { request =>
    Ok(views.html.index())
  }
  
  def list() = Action { request =>
    Ok(views.html.list())
  }
  
  def matches() = Action { request =>
    Ok(views.html.matches())
  }
  
  def indmatch(id: String) = Action { request =>
    Ok(views.html.individualmatch(id)(request))
  }
  
  def players() = Action { request =>
    Ok(views.html.players())
  }
  
  def player(id: String) = Action { request =>
    Ok(views.html.player(id)(request))
  }
  
}