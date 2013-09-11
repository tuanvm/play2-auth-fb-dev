package com.madebymira.facebook

import play.api.{ Logger, Application, Plugin, Routes }
import play.api.libs._
import play.api.libs.ws.WS
import com.restfb.DefaultFacebookClient
import com.restfb.types._
import scala.concurrent._
import scala.concurrent.duration._

/**
 * The Class FacebookPlugin.
 */
class FacebookPlugin(application: Application) extends Plugin {
    val FACEBOOK_ID: String = "facebook.id";
    val FACEBOOK_SECRET: String = "facebook.secret";
    val FACEBOOK_CALLBACK_URL: String = "facebook.callbackURL"

    lazy val id: String = application.configuration.getString(FACEBOOK_ID).getOrElse(null);
    lazy val secret: String = application.configuration.getString(FACEBOOK_SECRET).getOrElse(null);
    lazy val callbackURL: String = application.configuration.getString(FACEBOOK_CALLBACK_URL).getOrElse(null);

    /* (non-Javadoc)
     * @see play.api.Plugin#onStart()
     */
    override def onStart() {
        val configuration = application.configuration;
        // you can now access the application.conf settings, including any custom ones you have added
        //id
        //secret
        //callbackURL
        Logger.info("onStart id:" + id)
        Logger.info("FacebookPlugin has started");
    }

    /**
     * Gets the login url.
     *
     * @param scope the scope
     * @return the login url
     */
    def getLoginUrl(scope: String): String = {
        if ((scope != null) && (!scope.isEmpty())) {
            return String.format("https://graph.facebook.com/oauth/authorize?scope=%s&client_id=%s&redirect_uri=%s", scope, id, callbackURL);
        } else {
            Logger.info("get Login id:" + id)
            return String.format("https://graph.facebook.com/oauth/authorize?scope=email,user_location,publish_stream,offline_access,create_event&client_id=%s&redirect_uri=%s", id, callbackURL);
        }
    }

    /**
     * Gets the auth url.
     *
     * @param authCode the auth code
     * @return the auth url
     */
    def getAuthUrl(authCode: String): String = {
        if (authCode == null || authCode.isEmpty()) return null;
        else return String.format("https://graph.facebook.com/oauth/access_token?client_id=%s&redirect_uri=%s&client_secret=%s&code=%s", id, callbackURL, secret, authCode);
    }

    /**
     * Gets the fb user.
     *
     * @param code the code
     * @return the fb user
     */
    def getFbUser(code: String): User = {
        import scala.util.matching.Regex

        Logger.info(getAuthUrl(code))
        val duration = Duration(10, SECONDS)
        val future: Future[play.api.libs.ws.Response] = WS.url(getAuthUrl(code)).get()
        val response = Await.result(future, duration)
        val accessTokenBody = response.body

        val regex = new Regex("access_token=(.*)&expires=(.*)")
        accessTokenBody match {
            case regex(accessToken, expires) => {
                val facebookClient = new DefaultFacebookClient(accessToken)
                val fbUser = facebookClient.fetchObject("me", classOf[com.restfb.types.User])
                return fbUser
            }
            case _ => {
                return null
            }
        }
    }
}