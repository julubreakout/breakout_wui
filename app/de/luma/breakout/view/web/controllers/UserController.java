package de.luma.breakout.view.web.controllers;

import java.util.HashMap;
import java.util.Map;

import de.luma.breakout.data.user.User;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.F;
import play.libs.OpenID;
import play.mvc.Controller;
import play.mvc.Result;

public class UserController extends Controller {

	private static final String USER_NAME = "luma.webtech";
	private static final String USER_PW = "900150983cd24fb0d6963f7d28e17f72";  // = abc
	
	
	// ##########################  FORMS AUTHENTICATION HANDLERS ###########################
		
	/**
	 * Returns name/email of logged in user or empty string.
	 */
    public static String getActiveUser() {
        if(session("UserName") != null && !session("UserName").equals("")) {
          return session("UserName");
        }
        return "";
    }
	
    /**
     * GET: /login 
     * Shows login page
     */
	public static Result login() {
		// redirect to index if already logged in
		if (!getActiveUser().equals("")) {
			return redirect(routes.Application.socket_index());
		}
		
		return ok(de.luma.breakout.view.web.views.html.login.render(""));
	}
	
	/**
	 * GET:  /logout
	 * Terminate a user session.
	 */
	public static Result logout() {
		session().clear();
		return redirect(routes.UserController.login());
	}
	
	/**
	 * POST: /processLogin
	 * Processes a forms login.
	 */
	public static Result processLogin() {
		// get form data from request
		Form<User> filledForm = DynamicForm.form(User.class).bindFromRequest();		
		User user = filledForm.get();

		if (user.getUsername().equals(USER_NAME) && user.getPassword().equals(USER_PW)) {  // login is correct
			session().clear();
            session("UserName", user.getUsername());            
			return redirect(routes.Application.socket_index());
		}
		
		return ok(de.luma.breakout.view.web.views.html.login.render("Username or password are wrong."));
	}
	


	
	
	// ####################  HANDLERS FOR OPEN ID AUTHENTICATION ##################

	/**
	 * GET: /auth
	 * Show login page for OpenID authentication
	 */
	public static Result openid_auth() {
		String providerUrl = "https://www.google.com/accounts/o8/id";
		String returnToUrl = routes.UserController.openid_verify().absoluteURL(request());
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("Email", "http://schema.openid.net/contact/email");
		attributes.put("FirstName", "http://schema.openid.net/namePerson/first");
		attributes.put("LastName", "http://schema.openid.net/namePerson/last");
		F.Promise<String> redirectUrl = OpenID.redirectURL(providerUrl, returnToUrl, attributes);
		return redirect(redirectUrl.get());
	}
	
	/**
	 * GET: /verify
	 * Callback action for OpenID provider
	 */
	public Result openid_verify() {
		F.Promise<OpenID.UserInfo> userInfoPromise = OpenID.verifiedId();
		OpenID.UserInfo userInfo = userInfoPromise.get();
		session().clear();
		session("UserName", userInfo.attributes.get("Email"));
		return redirect(routes.Application.socket_index());
	}
	
}
