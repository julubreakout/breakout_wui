package de.luma.breakout.view.web.controllers;

import play.api.Play;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;

import com.google.inject.Inject;

import de.luma.breakout.controller.GameController;
import de.luma.breakout.controller.IGameController;
import de.luma.breakout.view.web.datalayer.UserDAO;
import de.luma.breakout.view.web.models.User;

/**
 * Main controller of Play application 
 */
public class Application extends Controller  {

	@Inject
	private UserDAO userDAO;

	public Application() {
		
	}

	// #################### ACTIONS FOR WEBSOCKET VERSION ##########################

	/**
	 * GET: /index
	 * Returns main page layout of the game
	 */
	@play.mvc.Security.Authenticated(Secured.class)
	public Result index() {
		return ok(de.luma.breakout.view.web.views.html.socket_index.render());
	}

	/**
	 * GET: /socket_connect
	 * Initializes a new WebSocket connection to the running game
	 */
	@play.mvc.Security.Authenticated(Secured.class)
	public WebSocket<String> socket_connect() {
		User user = userDAO.getByEmail(session(UserController.SessionKey_Email));
		
		IGameController gameController;
		if (Play.current().path().getAbsolutePath().startsWith("/app")) {
			gameController = new GameController("/app/");			
		} else {
			gameController = new GameController("");
		}

		gameController.initialize();
		GameWebSocket gameInstance = new GameWebSocket(gameController, user);
		
		//gameInstances.put(user.getEmail(), gameInstance);
		
		return gameInstance;
	}

	public Result test() {  	
		return ok(Play.current().path().getAbsolutePath());
	}
}
