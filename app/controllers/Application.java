package controllers;

import java.util.Arrays;

import de.luma.breakout.communication.ObservableGame.GAME_STATE;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller  {
    
	private static StaticGameInstance gameInstance;
	
	public static StaticGameInstance getGame() {
		if (gameInstance == null) {
			gameInstance = new StaticGameInstance();
		}
		
		return gameInstance;
	}
		
    public static Result index() {
        return ok(views.html.index.render("Hello Play Framework"));
    }
    

    public static Result grid() {
    	if (getGame().gameController.getState() == GAME_STATE.RUNNING) {   // render playgrid
    		return ok("to do");
    	} else {  // render menu
    		List<MENU_ITEM> items = new 
    		return ok("kokoo"); //views.html.menu.render(Arrays.asList(getGame().menuItems)));
    	}
    }
    

    
}
