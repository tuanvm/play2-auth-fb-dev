package com.madebymira.facebook

import play.api.{ Logger, Application, Plugin, Routes }
import play.api.libs._
import play.api.libs.ws.WS
import com.restfb.DefaultFacebookClient
import com.restfb.types._
import scala.concurrent._
import scala.concurrent.duration._
import play.api.mvc._

trait FacebookConnect {
    self: FacebookConfig =>

    /**
     * Gets the authorize url.
     *
     * @return the authorize url
     */
    def getFbAuthorizeUrl: String = {
        return String.format("https://graph.facebook.com/oauth/authorize?scope=%s&client_id=%s&redirect_uri=%s", fbScope, fbId, fbCallbackURL);
    }

    /**
     * Gets the access token url.
     *
     * @param authCode the auth code
     * @return the access token url
     */
    def getFbAccessTokenUrl(authCode: String): String = {
        if (authCode == null || authCode.isEmpty()) return null;
        else return String.format("https://graph.facebook.com/oauth/access_token?client_id=%s&redirect_uri=%s&client_secret=%s&code=%s", fbId, fbCallbackURL, fbSecret, authCode);
    }

    /**
     * Gets the access token.
     *
     * @param authCode the auth code
     * @return the access token
     */
    def getFbAccessToken(authCode: String): String = {
        import scala.util.matching.Regex

        val duration = Duration(10, SECONDS)
        val future: Future[play.api.libs.ws.Response] = WS.url(getFbAccessTokenUrl(authCode)).get()
        val response = Await.result(future, duration)
        val accessTokenBody = response.body

        val regex = new Regex("access_token=(.*)&expires=(.*)")
        accessTokenBody match {
            case regex(accessToken, expires) => {
                return accessToken
            }
            case _ => {
                return null
            }
        }
    }

    /**
     * Gets the fb user.
     *
     * @param accessToken the access token
     * @return the fb user
     */
    def getFbUser(accessToken: String): User = {
        val facebookClient = new DefaultFacebookClient(accessToken)
        val fbUser = facebookClient.fetchObject("me", classOf[com.restfb.types.User])
        return fbUser
    }
}