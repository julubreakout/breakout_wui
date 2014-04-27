package de.luma.breakout.view.web.controllers;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.api.Play;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;

import com.google.gson.Gson;
import com.google.inject.Inject;

import de.luma.breakout.communication.GAME_STATE;
import de.luma.breakout.communication.MENU_ITEM;
import de.luma.breakout.controller.GameController;
import de.luma.breakout.controller.IGameController;
import de.luma.breakout.controller.IGameController.PLAYER_INPUT;
import de.luma.breakout.view.web.datalayer.UserDAO;
import de.luma.breakout.view.web.models.User;

/**
 * Main controller of Play application 
 */
public class Application extends Controller  {

	@Inject
	private UserDAO userDAO;

	private Map<String, GameWebSocket> gameInstances; 
	
	public Application() {
		this.gameInstances = new HashMap<String, GameWebSocket>();
	}

	//	/**
	//	 * Initializes the global game instance.
	//	 */
	//	private static IGameController getGameController() {
	//		if (gameController == null) {
	//			
	//			
	//			// Open Swing GUI of game
	////			MainWindow mainWindow = new MainWindow(gameController);                
	////			gameController.addObserver(mainWindow.getBpaGameView2D());
	////			mainWindow.setVisible(true);
	//            
	//        	gameController.initialize();	
	//		}
	//		return gameController;
	//	}



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


	// ########################## ACTIONS FOR AJAX-POLLING VERSION  ###########################
	//		
	//	/**
	//	 * GET: /index
	//	 * Returns AJAX-based main page layout of the game
	//	 */
	//	@play.mvc.Security.Authenticated(Secured.class)
	//    public static Result index() {
	//    	return ok(de.luma.breakout.view.web.views.html.index.render());
	//    }
	//    
	//	
	//    /** 
	//     * GET: /playGrid
	//     * Returns content for #PlayGrid div  (game, level select or menu)
	//     */
	//    public static Result playGrid() {
	//   	
	//    	if (getGameController().getState() == GAME_STATE.RUNNING) {   // render playgrid (game is running)
	//    		return ok(de.luma.breakout.view.web.views.html.gamegrid.render(
	//    				getGameController().getGridSize().width,
	//    				getGameController().getGridSize().height,
	//    				HtmlHelper.getBricks(gameController), HtmlHelper.getBalls(gameController)));
	//    		
	//    	} else if (getGameController().getState() == GAME_STATE.MENU_LEVEL_SEL) { // render level selection dialog
	//    		return getLevels();
	//    		
	//    	} else {  // render menu items
	//    		return ok(de.luma.breakout.view.web.views.html.menu.render(HtmlHelper.getMenu(
	//    				gameController.getGameMenu().getMenuItems(),
	//    				gameController.getGameMenu().getTitle())));
	//    	}
	//    }
	//   
	//    /**
	//     * Returns JSON-formatted level list.
	//     */
	//    private static Result getLevels() {       	
	//    	Gson gson = new Gson();
	//    	System.out.println("call get LevelList");
	//    	List<String> levels = getGameController().getLevelList();
	//    	System.out.println("levels count: " + levels.size());
	//    	
	//    	
	//    	String json = gson.toJson(levels);
	//    	response().setContentType("Application.levellist");
	// 		return ok(json); 
	//    }
	//    
	//    /**
	//     * GET: /loadLevel?file=[FileName]
	//     * Loads the specified level file
	//     */
	//    public static Result loadLevel(String file) {
	//    	getGameController().loadLevel(new File(file));
	//    	getGameController().processMenuInput(MENU_ITEM.MNU_CONTINUE);
	//    	return ok();
	//    }
	//    
	//    
	//    /**
	//     * GET: /selectmenu?index=[MenuItemIndex]
	//     * Processes a click on a menu button from AJAX-based view
	//     */
	//    public static Result selectmenu(String index) {
	//    	int itemIndex = Integer.valueOf(index);
	////    	
	//    	MENU_ITEM item = MENU_ITEM.values()[itemIndex];
	////    	return ok( item + " index: " + itemIndex);
	////    	
	//    	getGameController().processMenuInput(item);
	//    	
	//    	return index();
	//    }
	//    
	//    /**
	//     * GET: /gameInput?key=[KeyCode]
	//     * Processes a key event on the play grid
	//     */
	//    public static Result gameInput(String key) {
	//    	if (key == null) {
	//    		return ok();
	//    	}
	//    	
	//    	if (key.equals("escape")) {
	//			getGameController().processGameInput(PLAYER_INPUT.PAUSE);
	//    	} else if (key.equals("right")) {
	//			getGameController().processGameInput(PLAYER_INPUT.RIGHT);
	//			getGameController().processGameInput(PLAYER_INPUT.RIGHT);
	//		} else if (key.equals("left")) {
	//			getGameController().processGameInput(PLAYER_INPUT.LEFT);
	//			getGameController().processGameInput(PLAYER_INPUT.LEFT);
	//		}		
	//    	return ok();
	//    }

	public Result test() {
		//User u = new User();
		//    	u.setName("Wolfgang");
		//    	u.setEmail("a@b.com");
		//    	AppGlobal.getInjector().getInstance(UserDAO.class).create(u);
		//    	
		return ok(Play.current().path().getAbsolutePath());
	}
}
