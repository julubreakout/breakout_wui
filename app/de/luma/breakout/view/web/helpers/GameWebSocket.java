package de.luma.breakout.view.web.helpers;

import java.awt.Dimension;
import java.util.List;
import java.util.Set;

import play.api.templates.Html;
import play.libs.F.Callback;
import play.libs.F.Callback0;
import play.mvc.WebSocket;
import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;

import com.google.gson.Gson;

import de.luma.breakout.communication.GAME_STATE;
import de.luma.breakout.communication.MENU_ITEM;
import de.luma.breakout.communication.PLAYER_INPUT;
import de.luma.breakout.communication.messages.AddObserverMessage;
import de.luma.breakout.communication.messages.DetachObserverMessage;
import de.luma.breakout.communication.messages.GameInputMessage;
import de.luma.breakout.communication.messages.LoadLevelMessage;
import de.luma.breakout.communication.messages.MenuInputMessage;
import de.luma.breakout.communication.messages.ShowMenuMessage;
import de.luma.breakout.communication.messages.UpdateGameFrameMessage;
import de.luma.breakout.communication.messages.UpdateGameStateMessage;
import de.luma.breakout.data.objects.IBall;
import de.luma.breakout.data.objects.IBrick;
import de.luma.breakout.view.web.AppGlobal;
import de.luma.breakout.view.web.datalayer.UserDAO;
import de.luma.breakout.view.web.models.Highscore;
import de.luma.breakout.view.web.models.User;

/**
 * Manages a WebSocket connection to a client.
 * Receives events from webbrowser and send game content to the client.
 */
public class GameWebSocket extends WebSocket<String> {
	
	/**
	 * Proxy class which creates an Actor for the websocket instance (which cannot extend UntypedActor)
	 * and forwards messages from the GameController to the WebSocket instance.
	 * @author lueckert
	 *
	 */
	private static class WebsocketActorProxy extends UntypedActor {
		
		private ActorRef gameInstance;
		private GameWebSocket websocketInstance;

		public WebsocketActorProxy(GameWebSocket websocketInstance, ActorRef gameInstance) {
			super();
			this.websocketInstance = websocketInstance;
			this.gameInstance = gameInstance;
		}
		
		@Override
		public void onReceive(Object msg) throws Exception {
			if (getSender().equals(getSelf())) {
				// message from WebSocket to be forwarded to the GameController actor
				gameInstance.tell(msg, getSelf());
			}
			else {
				// message from GameController to be forwareded to the WebSocket instance
				websocketInstance.onReceive(msg);
			}
		}

	}

	
	private Gson gson;
	private play.mvc.WebSocket.In<String> in;
	private play.mvc.WebSocket.Out<String> out;
	private User user;
	
	private ActorRef controllerProxy;
	private Set<UserDAO> userDAO;
	private IWebSocketObserver observer;
	
	public GameWebSocket(final ActorRef gameController, User user, Set<UserDAO> userDAO) {
		super();
		
		this.gson = new Gson();
		this.user = user;
		this.userDAO = userDAO;
		
		// Create instance of own Actor proxy
		Props props = new Props(new UntypedActorFactory() {
			@Override
			public Actor create() throws Exception {
				return new WebsocketActorProxy(GameWebSocket.this, gameController);
			}
		});
	
		this.controllerProxy = AppGlobal.getActorSystem().actorOf(props);
	}
	
	
	/**
	 * Is called when messages from the GameController are received. 
	 * Maps the messages to the observable events of the Game Controller.
	 */
	public void onReceive(Object msg)  {
		if (msg instanceof ShowMenuMessage) {
			ShowMenuMessage menuMsg = (ShowMenuMessage)msg;
			updateGameMenu(menuMsg.getMenuItems(), menuMsg.getTitle());
			
		} else if (msg instanceof UpdateGameFrameMessage) {
			UpdateGameFrameMessage frameMsg = (UpdateGameFrameMessage)msg;
			updateGameFrame(frameMsg.getGameState(), frameMsg.getBricks(), frameMsg.getSlider(), frameMsg.getBalls(), frameMsg.getGridSize(), frameMsg.getScore());
			
		} else if (msg instanceof UpdateGameStateMessage) {
			UpdateGameStateMessage stateMsg = (UpdateGameStateMessage)msg;
			updateGameState(stateMsg.getState(), stateMsg.getScore(), stateMsg.getLevelList());
		}
	}
	
	/**
	 * Notifies client of changed game state (JSON-formatted)
	 */
	public void updateGameState(GAME_STATE state, int score, List<String> levelList) {
		if (out == null)
			return;
		
		out.write("STATE:" + state);
		
		// level selection Menu
		if (state == GAME_STATE.MENU_LEVEL_SEL) {			
			out.write("LEVEL:" + gson.toJson(levelList));
		}
		
		
		// save new highscore
		if (state == GAME_STATE.MENU_WINGAME || state == GAME_STATE.MENU_GAMEOVER) {
			Highscore highscore = new Highscore(AppGlobal.GameName, user.getName(), score);

			// post highscore to public server in a separate thread
			HighscorePoster poster = AppGlobal.getInjector().getInstance(HighscorePoster.class);
			poster.setHighscore(highscore);
			poster.run();
			// save highscore internally
			user.setHighscore(score);
		//	userDAO.update(user);
			for(UserDAO db : userDAO){
				db.update(user);
			}
		}
	}

	/**
	 * Sends menu items to client (JSON-formatted)
	 */
	public void updateGameMenu(MENU_ITEM[] menuItems, String title) {	
		if (out == null)
			return;
		
		out.write("MENU:" + gson.toJson(HtmlHelper.getMenu(menuItems, title)));
	}

	/**
	 * Sends the rendered play grid to the client.  (HTML)
	 */
	public void updateGameFrame(GAME_STATE state, List<IBrick> bricks, IBrick slider, List<IBall> balls, Dimension gridSize, int score) {
		if (out == null || state != GAME_STATE.RUNNING) {
			return;
		}

		Html playGrid = de.luma.breakout.view.web.views.html.gamegrid.render(
				gridSize.width,
				gridSize.height,
				HtmlHelper.getBricks(bricks, slider), 
				HtmlHelper.getBalls(balls),
				score,
				user.getHighscore());
		
		out.write("GRID:" + playGrid.body());	
	}


	/**
	 * Handles incoming events from webbrowser
	 */
	@Override
	public void onReady(play.mvc.WebSocket.In<String> in, play.mvc.WebSocket.Out<String> out) {
		this.out = out;
		this.in = in;
		
		// handler for incoming messages
		in.onMessage(new Callback<String>() {
			@Override
			public void invoke(String event) throws Throwable {
				// process key Input
				if (event.startsWith("key:"))  {
					String key = event.substring(event.indexOf(":") +1);
					GameWebSocket.this.controllerProxy.tell(new GameInputMessage(PLAYER_INPUT.valueOf(key)), GameWebSocket.this.controllerProxy);					
				}
				
				// process menu Input
				if (event.startsWith("menu:")) {			
					Integer index =  Integer.valueOf(event.substring(event.indexOf(":") +1));
					GameWebSocket.this.controllerProxy.tell(new MenuInputMessage(MENU_ITEM.values()[index]), GameWebSocket.this.controllerProxy);	
				}
				
				// process level select
				if (event.startsWith("level:")) {
					String file = event.substring(event.indexOf(":") +1);
					GameWebSocket.this.controllerProxy.tell(new LoadLevelMessage(file), GameWebSocket.this.controllerProxy);
					GameWebSocket.this.controllerProxy.tell(new MenuInputMessage(MENU_ITEM.MNU_CONTINUE), GameWebSocket.this.controllerProxy);
				}
			}			
		});
		
		// handler when browser/tab is closed
		in.onClose(new Callback0() {
			@Override
			public void invoke() throws Throwable {
				// remove this websocket from the gamecontrollers observer list
				GameWebSocket.this.controllerProxy.tell(new DetachObserverMessage(), GameWebSocket.this.controllerProxy);
				
				// let the application controller remove itself as observer of this websocket
				if (observer != null)
					observer.onClosed(user.getEmail());
			}
		});
		
		// Send init message to game controller
		System.out.println("Websocket connecting to controller actor:" + controllerProxy.toString());
		this.controllerProxy.tell(new AddObserverMessage(), controllerProxy);
	}
	
	public IWebSocketObserver getObserver() {
		return observer;
	}

	public void setObserver(IWebSocketObserver observer) {
		this.observer = observer;
	}

	
}
