play2-auth-fb
=============
How to use this module
1, run: play publish-local => to public this module to local

2, add "play2-auth-fb-module" % "play2-auth-fb-module_2.10" % "1.0-SNAPSHOT" to Build.scala in your project (with 2.10 is your play version => re-check your play version if needed)

3, create play.plugins in /conf/ folder, add line 5000:com.madebymira.facebook.FacebookPlugin (5000 is priority)

4, add these lines to application.conf (and test.conf if needed), change these value properly
facebook.id=1385360048361554
facebook.secret=b5840274952d8653cf26e7e39263642e
facebook.callbackURL="http://localhost:9000/callback"

5, call getLoginUrl in FacebookPlugin to get URL to login with facebook

6, implement a callback function to handle callbackURL, facebook will return a code, call getFbUser(code) to get com.restfb.types.User info

7, Do your business with this user info