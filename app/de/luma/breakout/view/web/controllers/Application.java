package de.luma.breakout.view.web.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import play.api.Play;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import akka.actor.ActorRef;
import akka.actor.Props;

import com.google.inject.Inject;

import de.luma.breakout.controller.GameController;
import de.luma.breakout.controller.IGameController;
import de.luma.breakout.controller.GameController.GameControllerActor;
import de.luma.breakout.view.web.AppGlobal;
import de.luma.breakout.view.web.datalayer.UserDAO;
import de.luma.breakout.view.web.helpers.GameWebSocket;
import de.luma.breakout.view.web.helpers.GamepadWebSocket;
import de.luma.breakout.view.web.helpers.IWebSocketObserver;
import de.luma.breakout.view.web.helpers.Secured;
import de.luma.breakout.view.web.models.User;

/**
 * Main controller of Play application 
 */
public class Application extends Controller  {
	
	private static class GameSession {
		public ActorRef gameController;
		public int clientCount;
		
		public GameSession(ActorRef controller)  {
			this.gameController = controller;
			clientCount = 1;
		}
	}
	
	
	private Map<String, GameSession> activeGames;

	private Set<UserDAO> userDAO;

	@Inject
	public Application(Set<UserDAO> users) {
		activeGames = new HashMap<String, GameSession>();
		this.userDAO = users;
	}

	// #################### ACTIONS FOR WEBSOCKET VERSION ##########################

	/**
	 * GET: /index
	 * Returns main page layout of the game
	 */
	@play.mvc.Security.Authenticated(Secured.class)
	public Result index() {
		return ok(de.luma.breakout.view.web.views.html.socket_index.render(UserController.getActiveUser()));
	}

	/**
	 * GET: /socket_connect
	 * Initializes a new WebSocket connection to the running game
	 */
	@play.mvc.Security.Authenticated(Secured.class)
	public WebSocket<String> socket_connect() {
		String activeUser = session(UserController.SessionKey_Email);
		User user = userDAO.iterator().next().getByEmail(activeUser);
		ActorRef gameInstance = null;
		
		if (activeGames.containsKey(activeUser)) {   // re-use running a game?
			GameSession session = activeGames.get(activeUser);
			session.clientCount++;
			gameInstance = session.gameController;
			
		} else {
			
			// create a new game(controller) instance
			if (Play.current().path().getAbsolutePath().startsWith("/app")) {
				gameInstance = AppGlobal.getActorSystem().actorOf(Props.create(GameControllerActor.class, "/app/"));		
			} else {
				gameInstance = AppGlobal.getActorSystem().actorOf(Props.create(GameControllerActor.class, ""));
			}
			
			GameSession session = new GameSession(gameInstance);
			activeGames.put(activeUser, session);
		}
		
		// add a websocket wrapper to the controller
		GameWebSocket websocket = new GameWebSocket(gameInstance, user, userDAO);
		
		// listen to the close event of the websocket to delete the game session after the last client left the game
		websocket.setObserver(new IWebSocketObserver() {
			
			@Override
			public void onClosed(String id) {
				GameSession session = activeGames.get(id);
				session.clientCount--;
				
				if (session.clientCount == 0) {
					activeGames.remove(id);
				}
			}
		});
		return websocket;
	}
	
	
	/**
	 * GET: /gamepad
	 * Returns a gamepad to control an other game session.
	 */
	@play.mvc.Security.Authenticated(Secured.class)
	public Result gamepad() {
		return ok(de.luma.breakout.view.web.views.html.gamepad.render());
	}
	
	/**
	 * GET: /gamepad_connect
	 * Initializes a new WebSocket connection to control a running game.
	 */
	@play.mvc.Security.Authenticated(Secured.class)
	public WebSocket<String> gamepad_connect() {
		String activeUser = session(UserController.SessionKey_Email);

		if (activeGames.containsKey(activeUser)) {   // has user already running a game?
			GameSession session = activeGames.get(activeUser);
			return new GamepadWebSocket(session.gameController);
			
		} else {
			return null;
		}
	}

}
