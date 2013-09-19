This is your new Play 2.1 application
=====================================

This file will be packaged with your application, when using `play dist`.

How to use this module
1, run: play publish-local => to public this module to local

2, add "play2-auth-fb-module" % "play2-auth-fb-module_2.10" % "1.0-SNAPSHOT" to Build.scala in your project (with 2.10 is your play version => re-check your play version if needed)

3, create your facebook app, config callbackURL to your callback URL (example: http://localhost:9000/callback/fb)

4, add these lines to application.conf (and test.conf if needed), change these value properly
facebook.id=your app id
facebook.secret=your app secret
facebook.callbackURL="http://localhost:9000/callback/fb"
facebook.scope=(for example:email)

5, create social config trait (must extends Controller)
trait SocialConfig extends Controller with FacebookConfig {
    //Facebook configuration
    val fbId: String = Play.current.configuration.getString("facebook.id").getOrElse("")
    val fbSecret: String = Play.current.configuration.getString("facebook.secret").getOrElse("")
    val fbCallbackURL: String = Play.current.configuration.getString("facebook.callbackURL").getOrElse("")
    val fbScope: String = Play.current.configuration.getString("facebook.scope").getOrElse("")
  }

6, let your controller which handle login/register with facebook connect extends SocialConfig, and with FacebookConnect (becase SocialConfig extends Controller, so you no need to extends Controller anymore)
object SocialConnect extends SocialConfig with FacebookConnect{...}


7, click login/register with facebook button, redirect user to Redirect(getFbAuthorizeUrl)

8, implement a callback function to handle callbackURL, twitter will return a code or error (example: user denied), exchange code to get access token val accessToken = getFbAccessToken(code)

9. use this access token to get user info
val fbUser = getFbUser(accessToken) will return com.restfb.types.User object

10. do your business logic and logged user in
