package controllers;
import de.luma.breakout.communication.IGameObserver;
import de.luma.breakout.communication.ObservableGame.GAME_STATE;
import de.luma.breakout.communication.ObservableGame.MENU_ITEM;
import de.luma.breakout.controller.GameController;
import de.luma.breakout.controller.IGameController;


public class StaticGameInstance implements IGameObserver {

	public IGameController gameController;
	public MENU_ITEM[] menuItems;
	public String menuTitle;
	
	public StaticGameInstance() {
		gameController = new GameController();
	}
	
	@Override
	public void updateGameMenu(MENU_ITEM[] menuItems, String title) {
		this.menuItems = menuItems;
		this.menuTitle = title;
	}

	@Override
	public void updateRepaintPlayGrid() {
		// nothing to do
	}

	@Override
	public void updateGameState(GAME_STATE state) {
		// nothing to do
	}

	@Override
	public void updateGameFrame() {
		// nothing to do
	}

	@Override
	public void updateOnResize() {
		// nothing to do
	}
	
}
