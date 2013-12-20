package utils.rest

import play.api.mvc.Action
import play.api.mvc.Result
import play.api.mvc.Request
import play.api.mvc.AnyContent

object CORSAction {
	def apply(block: Request[AnyContent] => Result): Action[AnyContent] = {
      Action { request =>
        block(request).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }
  }