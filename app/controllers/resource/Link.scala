package controllers.resource

import play.api.libs.json.JsValue
import play.api.libs.json.Json

import utils.resource.Jsonable

class Link(
  rel: String,
  href: String
) extends Jsonable {
  
  val descriptorName = "rel"
  val linkName = "href"
  
  lazy val toJson: JsValue = {
    Json.toJson(
      Map(
        descriptorName -> rel,
        linkName -> href
      )
    )
  }
  
}

object Link {
  
  val resourceName = "link"
  
}