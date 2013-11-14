package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import de.luma.breakout.communication.ObservableGame.GAME_STATE;
import de.luma.breakout.communication.ObservableGame.MENU_ITEM;
import de.luma.breakout.controller.IGameController.PLAYER_INPUT;

public class Application extends Controller  {
    
	private static StaticGameInstance gameInstance;
	
	public static StaticGameInstance getGame() {
		if (gameInstance == null) {
			gameInstance = new StaticGameInstance();
			
		}
		
		return gameInstance;
	}
		
	
	/**
	 * Returns the main page layout
	 */
    public static Result index() {
    	return ok(views.html.index.render());
    }
    
    /**
     * Returns content for #PlayGrid div
     */
    public static Result playGrid() {
    	if (getGame().gameController.getState() == GAME_STATE.RUNNING) {   // render playgrid
    		return ok(views.html.gamegrid.render(getGame().getBricks(), getGame().getBalls()));
    	} else {  // render menu
    		return ok(views.html.menu.render(getGame().getMenuItems()));
    	}
    }
    
    /**
     * Processes a click on a menu button
     */
    public static Result selectmenu(String index) {
    	int itemIndex = Integer.valueOf(index);
    	getGame().gameController.processMenuInput(MENU_ITEM.values()[itemIndex]);
    	
    	return index();
    }
    
    /**
     * Processes a key event on the play grid
     */
    public static Result gameInput(String key) {
    	switch (key) {
		case "escape":
			getGame().gameController.processGameInput(PLAYER_INPUT.PAUSE);
			break;
		case "right":
			getGame().gameController.processGameInput(PLAYER_INPUT.RIGHT);
			break;
		case "left":
			getGame().gameController.processGameInput(PLAYER_INPUT.LEFT);
			break;
		default:
			break;
		}
    	return ok();
    }
  
}
