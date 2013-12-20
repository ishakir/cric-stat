package utils.resource

import play.api.libs.json.JsValue

trait Jsonable {

  val toJson: JsValue
  
}