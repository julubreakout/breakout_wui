package controllers;
import java.util.ArrayList;
import java.util.List;

import de.luma.breakout.communication.IGameObserver;
import de.luma.breakout.communication.ObservableGame.GAME_STATE;
import de.luma.breakout.communication.ObservableGame.MENU_ITEM;
import de.luma.breakout.communication.TextMapping;
import de.luma.breakout.controller.GameController;
import de.luma.breakout.controller.IGameController;


public class StaticGameInstance implements IGameObserver {
	
	public static class MenuItemParameter {
		public String text;
		public Integer index;
		
		public MenuItemParameter(String text, Integer index) {
			super();
			this.text = text;
			this.index = index;
		}				
	}

	public IGameController gameController;
	private MENU_ITEM[] menuItems;	
	public String menuTitle;
	
	public StaticGameInstance() {		
		gameController = new GameController();
		gameController.addObserver(this);
		
		gameController.initialize();		
	}
	
	@Override
	public void updateGameMenu(MENU_ITEM[] menuItems, String title) {
		this.menuItems = menuItems;
		this.menuTitle = title;
	}
	
	public List<MenuItemParameter> getMenuItems() {
		if (menuItems == null || menuItems.length == 0) {
			return new ArrayList<MenuItemParameter>(0);
		}		
		
		List<MenuItemParameter> menuList = new ArrayList<MenuItemParameter>();		
		for (MENU_ITEM menuItem : menuItems) {
			menuList.add(new MenuItemParameter(TextMapping.getTextForMenuEnum(menuItem), menuItem.ordinal()));			
		}
		
		return menuList;
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
