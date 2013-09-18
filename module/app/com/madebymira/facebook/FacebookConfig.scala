package com.madebymira.facebook

import play.api.mvc._

trait FacebookConfig {
    self: Controller =>

    val fbId: String
    val fbSecret: String
    val fbCallbackURL: String
    val fbScope: String
}