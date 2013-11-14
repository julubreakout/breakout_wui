package controllers;
import java.util.ArrayList;
import java.util.List;

import de.luma.breakout.communication.IGameObserver;
import de.luma.breakout.communication.ObservableGame.GAME_STATE;
import de.luma.breakout.communication.ObservableGame.MENU_ITEM;
import de.luma.breakout.communication.TextMapping;
import de.luma.breakout.controller.GameController;
import de.luma.breakout.controller.IGameController;
import de.luma.breakout.data.objects.IBall;
import de.luma.breakout.data.objects.IBrick;
import de.luma.breakout.data.objects.impl.MovingBrick;


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
	
	public static class HtmlBrick {
		public int x,y,width,height;
		public String color;
		
		public HtmlBrick(int x, int y, int width, int height) {
			this.x = x; 
			this.y = y;
			this.width = width;
			this.height = height;
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
	
	/**
	 * Returns the list of bricks.
	 */
	public List<HtmlBrick> getBricks() {
		List<HtmlBrick> bricks = new ArrayList<HtmlBrick>(gameController.getBricks().size());
		
		// add normal bricks
		for(IBrick brick : gameController.getBricks()) {
			HtmlBrick htmlBrick = new HtmlBrick(brick.getX(), brick.getY(), brick.getWidth(), brick.getHeight());
			
			if (brick instanceof MovingBrick) {
				htmlBrick.color = "red";
			} else {
				htmlBrick.color = "blue";
			}
			
			bricks.add(htmlBrick);
		}
		
		// add slider
		IBrick slider = gameController.getSlider();
		HtmlBrick htmlSlider = new HtmlBrick(slider.getX(), slider.getY(), slider.getWidth(), slider.getHeight());
		htmlSlider.color = "green";
		bricks.add(htmlSlider);
		return bricks;
	}
	
	
	public List<HtmlBrick> getBalls() {
		List<HtmlBrick> balls = new ArrayList<HtmlBrick>(gameController.getBalls().size());

		for(IBall ball : gameController.getBalls()) {
			
			int radius = (int)ball.getRadius();
			HtmlBrick htmlBrick = new HtmlBrick((int)ball.getX() - radius/2, (int)ball.getY() - radius/2, (int)ball.getRadius(), (int)ball.getRadius());
			balls.add(htmlBrick);
		}
		return balls;
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
