package de.luma.breakout.view.web.controllers;

import java.util.HashMap;
import java.util.Map;

import play.data.DynamicForm;
import play.data.Form;
import play.libs.F;
import play.libs.OpenID;
import play.mvc.Controller;
import play.mvc.Result;

import com.google.inject.Inject;

import de.luma.breakout.view.web.datalayer.UserDAO;
import de.luma.breakout.view.web.models.User;

/**
 * Manages all requests for user & account management.
 * @author Lukas
 *
 */
public class UserController extends Controller {

	/**
	 * Key for session() object which contains the name of the current user.
	 */
	public static final String SessionKey_UserName = "UserName";
	
	/**
	 * Key for session() object which contains the email (= ID) of the current user.
	 */
	public static final String SessionKey_Email = "Email";
	
	@Inject
	private UserDAO userDAO;


	// ##########################  LOGIN HANDLERS ###########################

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
	public Result login() {
		// redirect to index if already logged in
		if (!getActiveUser().equals("")) {
			return redirect(routes.Application.index());
		}

		return ok(de.luma.breakout.view.web.views.html.login.render(new User(), ""));
	}

	/**
	 * GET:  /logout
	 * Terminate a user session.
	 */
	public Result logout() {
		session().clear();
		return redirect(routes.UserController.login());
	}

	/**
	 * POST: /processLogin
	 * Processes a forms login.
	 */
	public Result processLogin() {
		// get form data from request
		Form<User> filledForm = DynamicForm.form(User.class).bindFromRequest();		
		User userModel = filledForm.get();

		// get user from DB
		User user = userDAO.getByEmail(userModel.getEmail());
		if (user == null) {
			return ok(de.luma.breakout.view.web.views.html.login.render(userModel, "User does not exist. Please register first."));
		}

		// check password
		if (userModel.getPassword() == null || !userModel.getPassword().equals(user.getPassword())) {
			return ok(de.luma.breakout.view.web.views.html.login.render(userModel, "Wrong password!"));
		}
 
		// allow login
		session().clear();
		session("UserName", user.getName()); 
		session("Email", user.getEmail());
		return redirect(routes.Application.index());
	}

	
	// ########################## REGISTRATION & PROFILE HANDLERS ###########################
	
	/**
	 * GET:  /register
	 * Show the user registration page.
	 */
	public Result register() {
		return ok(de.luma.breakout.view.web.views.html.register.render(new User(), "")); 
	}
	
	/**
	 * POST:  /register
	 * Handles a new user registration.
	 */
	public Result processRegister() {
		Form<User> filledForm = DynamicForm.form(User.class).bindFromRequest();		
		User user = filledForm.get();
		System.out.println("creating new player");
		userDAO.create(user);

		return redirect(routes.UserController.login());
	}
	
	/**
	 * GET:  /account
	 * Shows the user profile.
	 */
	@play.mvc.Security.Authenticated(Secured.class)
	public Result account() {
		User currentUser = userDAO.getByEmail(session(SessionKey_Email));
		return ok(de.luma.breakout.view.web.views.html.account.render(currentUser, "")); 
	}

	/**
	 * POST:  /account
	 * Handles updates to the user profile.
	 */
	@play.mvc.Security.Authenticated(Secured.class)
	public Result updateAccount() {
		User currentUser = userDAO.getByEmail(session(SessionKey_Email));
		
		Form<User> filledForm = DynamicForm.form(User.class).bindFromRequest();		
		User userModel = filledForm.get();
		
		currentUser.setName(userModel.getName());
		session(SessionKey_UserName, currentUser.getName());
		if (userModel.getPassword() != null && !userModel.getPassword().equals("")) {
			currentUser.setPassword(userModel.getPassword());
		}
		
		userDAO.update(currentUser);
		
		return ok(de.luma.breakout.view.web.views.html.account.render(currentUser, "Updated user account.")); 
	}
	
	/**
	 * GET:  /account/delete
	 * Deletes a user account.
	 */
	@play.mvc.Security.Authenticated(Secured.class)
	public Result deleteAccount() {
		User currentUser = userDAO.getByEmail(session(SessionKey_Email));
		userDAO.delete(currentUser);
		
		session().clear();
		
		return redirect(routes.UserController.login());
	}

	// ####################  HANDLERS FOR OPEN ID AUTHENTICATION ##################

	/**
	 * GET: /auth
	 * Show login page for OpenID authentication
	 */
	public Result openid_auth() {
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
		return redirect(routes.Application.index());
	}

}
