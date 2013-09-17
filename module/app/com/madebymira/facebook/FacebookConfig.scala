package com.madebymira.facebook

import play.api.mvc._

trait FacebookConfig {
    self: Controller =>

    val id: String
    val secret: String
    val callbackURL: String
    val scope: String
}