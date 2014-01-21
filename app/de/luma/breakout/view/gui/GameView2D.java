package de.luma.breakout.view.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import de.luma.breakout.communication.GAME_STATE;
import de.luma.breakout.communication.IGameObserver;
import de.luma.breakout.communication.MENU_ITEM;
import de.luma.breakout.communication.TextMapping;
import de.luma.breakout.controller.IGameController;
import de.luma.breakout.data.objects.IBall;
import de.luma.breakout.data.objects.IBrick;
import de.luma.breakout.data.objects.impl.Ball;


/**
 * Class to Display normal Game and Editor Gamegrid.
 * @author mabausch
 *
 */
@SuppressWarnings("serial")
public class GameView2D extends JPanel implements IGameObserver {	

	private class GameView2DMouseListener extends MouseInputAdapter  {
		
		private static final int DEFAULT_BRICK_WIDTH = 50;
		private static final int DEFAULT_BRICK_HEIGHT = 20;
		private static final int DEFAULT_BALL_DERIVATE = 5;

		/**
		 * (non-Javadoc)
		 * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			// ignore mouse actions outside the game area
			Dimension gridSize = getController().getGridSize();
			if (e.getX() > gridSize.getWidth() || e.getY() > gridSize.getHeight()) {
				return;
			}
			
			GameView2D.this.requestFocusInWindow();

			if (getController().getState() == GAME_STATE.RUNNING && getController().getCreativeMode()) {

				switch (e.getButton()) {

				// left mouse, create brick
				case MouseEvent.BUTTON1:		
					if (brickPreview == null) {
						brickPreview = new Rectangle(e.getX(), e.getY(), DEFAULT_BRICK_WIDTH, DEFAULT_BRICK_HEIGHT);
					}
					break;

				// right mouse, create ball
				case MouseEvent.BUTTON3:		
					if (createdBall == null) {
						createdBall = new Ball(e.getX(), e.getY(), 0, 0, DEFAULT_BALL_DERIVATE);
						getController().addBall(createdBall);
					}
					break;
				}
			}
		}

		/**
		 * (non-Javadoc)
		 * @see java.awt.event.MouseAdapter#mouseDragged(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseDragged(MouseEvent e) {
			super.mouseDragged(e);

			// brick is being created
			if (brickPreview != null) {
				brickPreview.width = e.getX() - brickPreview.x;
				brickPreview.height = e.getY() - brickPreview.y;

				// ball is being created
			} else if (createdBall != null) {
				createdBall.setSpeedX((e.getX() - createdBall.getX()) / VECTOR_LENGTH);
				createdBall.setSpeedY((e.getY() - createdBall.getY()) / VECTOR_LENGTH);
			}
		}

		/**
		 * (non-Javadoc)
		 * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseReleased(MouseEvent e) {
			// creating new game object is finished
			switch (e.getButton()) {
			// left mouse
			case MouseEvent.BUTTON1:		
				if (brickPreview != null && newBrickClassName != null) {					
					Class<?> classObj;
					try {
						classObj = this.getClass().getClassLoader().loadClass(newBrickClassName);
						IBrick obj = (IBrick) classObj.newInstance();

						obj.setX(brickPreview.x);
						obj.setY(brickPreview.y);
						obj.setWidth(brickPreview.width);
						obj.setHeight(brickPreview.height);

						getController().addBrick(obj);
						brickPreview = null;

					} catch (ClassNotFoundException e1) {
						JOptionPane.showMessageDialog(GameView2D.this, "Could not create brick: class not found!");					
					} catch (InstantiationException e1) {
						JOptionPane.showMessageDialog(GameView2D.this, "Could not create brick: class not creatable!");						
					} catch (IllegalAccessException e1) {
					}						
				}
				break;
			// right mouse
			case MouseEvent.BUTTON3:		
				createdBall = null;
				break;
			}
		}

	}

	private class GameView2DKeyListener implements KeyListener {
		/**
		 * (non-Javadoc)
		 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
		 */
		public void keyPressed(KeyEvent e) {				
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				leftKeyPressed = true;
				break;
			case KeyEvent.VK_RIGHT:
				rightKeyPressed = true;
				break;
			case KeyEvent.VK_ESCAPE:
				getController().processGameInput(IGameController.PLAYER_INPUT.PAUSE);
				break;
			case KeyEvent.VK_UP:
				selectPreviousMenuItem();
				break;
			case KeyEvent.VK_DOWN:
				selectNextMenuItem();
				break;
			case KeyEvent.VK_ENTER:
				selectCurrentMenuItem();
				break;
			}
		}

		/**
		 * (non-Javadoc)
		 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
		 */
		public void keyReleased(KeyEvent e) {  
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				leftKeyPressed = false;
				break;
			case KeyEvent.VK_RIGHT:
				rightKeyPressed = false;
				break;
			}
		}

		/**
		 * (non-Javadoc)
		 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
		 */
		public void keyTyped(KeyEvent e) { 	}
	}

	// Menu Variables for printing
	private MENU_ITEM[] menuItems;
	private String menuTitle;
	private int selectedItem = 0;

	// brick or ball that is being created while in level editor mode
	private Rectangle brickPreview;
	private String newBrickClassName;
	private IBall createdBall;

	// length of ball speed vector
	private final static int VECTOR_LENGTH = 20;
	private static final int TOOLBAR_AREA_WIDTH = 220;
	private static final int PADDING_20 = 20;
	private static final int PADDING_SCALE = 3;
	private static final int TEXT_HIGHT_SCALE = 75;

	// key input
	private KeyListener keyListener;	
	private boolean leftKeyPressed = false;
	private boolean rightKeyPressed = false;

	// Grafikal components
	private BpaEditorToolbar bpaEditorComps;
	private BpaLevelSelection bpaLevelSelect;
	private IGuiManager guiManager;

	/**
	 * 
	 */
	public GameView2D(IGuiManager resources) {
		super();		
		this.guiManager = resources;
		this.setFocusable(true);
		
		
		this.setPreferredSize(new Dimension(IGuiManager.MENU_X_Y_RANGE, IGuiManager.MENU_X_Y_RANGE));
		this.addKeyListener(getGameKeyListener());
		MouseInputAdapter mouseHandler = new GameView2DMouseListener();
		this.addMouseListener(mouseHandler);
		this.addMouseMotionListener(mouseHandler);	
		this.setLayout(new BorderLayout());			
		
		this.newBrickClassName =getController().getBrickClasses().get(0).getClass().getName();
	}

	/**
	 * (non-Javadoc)
	 * @see de.luma.breakout.communication.IGameObserver#updateRepaintPlayGrid()
	 */
	@Override
	public void updateRepaintPlayGrid() {	
		repaint();		
	}

	/**
	 * (non-Javadoc)
	 * @see de.luma.breakout.communication.IGameObserver#updateGameFrame()
	 */
	@Override
	public void updateGameFrame() {
		if (leftKeyPressed) {
			getController().processGameInput(IGameController.PLAYER_INPUT.LEFT);
		}
		if (rightKeyPressed) {
			getController().processGameInput(IGameController.PLAYER_INPUT.RIGHT);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see de.luma.breakout.communication.IGameObserver#updateGameState(de.luma.breakout.communication.ObservableGame.GAME_STATE)
	 */
	@Override
	public void updateGameState(GAME_STATE state) {

		// remove editor toolbar if still there
		removeEditorToolbar();
		removeLevelSelection();

		switch (state) {
		case MENU_GAMEOVER:
		case MENU_MAIN:
		case MENU_WINGAME:
		case PAUSED:
			this.setPreferredSize(new Dimension(IGuiManager.MENU_X_Y_RANGE, IGuiManager.MENU_X_Y_RANGE));
			this.invalidate();
			guiManager.updateLayout();
			break;
		case MENU_LEVEL_SEL:
			this.setPreferredSize(new Dimension(IGuiManager.MENU_X_Y_RANGE, IGuiManager.MENU_X_Y_RANGE));
			startLevelSelection();
			this.invalidate();
			guiManager.updateLayout();
			
			break;
		case RUNNING:
			updateOnResize();
			
			// editor mode
			if (getController().getCreativeMode()) {
				startEditorMode();
			}
			break;

		case KILLED:
			guiManager.kill();		
			break;
		}
	}
	
	private void startLevelSelection() {
		addBpaLevelSelection();
		bpaLevelSelect.loadLevels();
	}
	
	private void startEditorMode() {
		// add editor visual components
		addEditorToolbar();
		
		// create new Slider
		if (getController().getSlider() == null) {
			bpaEditorComps.resetLevel();
		}
	}

	/**
	 * (non-Javadoc)
	 * @see de.luma.breakout.communication.IGameObserver#updateGameMenu(de.luma.breakout.communication.ObservableGame.MENU_ITEM[], java.lang.String)
	 */
	@Override
	public void updateGameMenu(MENU_ITEM[] menuItems, String title) {
		this.selectedItem = 0;
		// make checkstyle happy
		this.menuItems = menuItems.clone();  
		this.menuTitle = title;
		repaint();
	}

	/**
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		this.paintComponents(g);
		this.paintBorder(g);
		this.paintChildren(g);
	}

	/**
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paintComponents(Graphics g) {


		if (!this.isFocusOwner() && !getController().getCreativeMode()) {
			this.requestFocusInWindow();
		}

		Graphics2D g2d = (Graphics2D) g;

		if (getController() == null) {
			return;
		}

		// Paint Running Game
		if (getController().getState() == GAME_STATE.RUNNING) {
			paintGame(g2d);			

			// Editor
			if (getController().getCreativeMode()) {
				paintEditor(g2d);
			}			

			// Paint Menu
		} else if (getController().getState() == GAME_STATE.MENU_LEVEL_SEL) {	
			super.paintComponents(g);
		} else {
			paintMenu(g2d);			
		}


	}

	private void paintEditor(Graphics2D g) {
		// paint save and load Button
		Image menuBackg = guiManager.getGameImage("resources/menu_background.png");

		g.drawImage(menuBackg, this.getWidth() - TOOLBAR_AREA_WIDTH, 0, menuBackg.getWidth(null), this.getHeight(), this);


		if (brickPreview != null) {
			g.setColor(Color.RED);
			g.drawRect(brickPreview.x, brickPreview.y, brickPreview.width, brickPreview.height);	
		}
	}

	private void paintMenu(Graphics2D g) {
		if (menuItems == null) { 
			return;
		}

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// background black
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());

		Image img = guiManager.getGameImage("resources/breakout_logo.png");		
		g.drawImage(img, (this.getWidth() - img.getWidth(null)) / 2, PADDING_20, Color.black, null);

		Image imgBtn = guiManager.getGameImage("resources/button.png");			
		Image imgBtnSelected = guiManager.getGameImage("resources/button_selected.png");

		int x = (this.getWidth() - imgBtn.getWidth(null)) / 2;
		int y = TOOLBAR_AREA_WIDTH;

		// print Title
		g.setFont(IGuiManager.TEXT_FONT);
		g.setColor(IGuiManager.TEXT_COLOR);

		int stringWidth = (int) g.getFontMetrics().getStringBounds(menuTitle, g).getWidth();

		g.drawString(menuTitle, (this.getWidth() - stringWidth) / 2, y - PADDING_20);

		// print Menu Items
		g.setFont(IGuiManager.BUTTON_FONT);
		g.setColor(IGuiManager.BUTTON_COLOR);

		for (int i = 0; i < menuItems.length; ++i) {
			if (i == selectedItem) {
				g.drawImage(imgBtnSelected, x, y, Color.black, null);
			} else {
				g.drawImage(imgBtn, x, y, Color.black, null);
			}

			g.drawString(TextMapping.getTextForMenuEnum(menuItems[i]), x + (PADDING_SCALE*PADDING_20), y + TEXT_HIGHT_SCALE);
			y += imgBtn.getHeight(null);
		}

	}


	private void paintGame(Graphics2D g) {
	
		// print Grid
		g.setColor(Color.black);
		Dimension gridSize = getController().getGridSize();
		g.fillRect(0, 0, (int) gridSize.getWidth(), (int) gridSize.getHeight());

		// print balls
		for (IBall b : getController().getBalls()) {

			// if in creative mode, display ball speed vector
			if (getController().getCreativeMode()) {
				g.setColor(Color.GRAY);
				g.drawLine((int) b.getX(), (int) b.getY(), (int) (b.getX() + VECTOR_LENGTH * b.getSpeedX()), (int) (b.getY() + VECTOR_LENGTH * b.getSpeedY()));
			}

			// draw the ellipse
			g.setColor(Color.red);
			g.fillOval(Double.valueOf(b.getX() - b.getRadius()).intValue() ,
					Double.valueOf(b.getY() - b.getRadius()).intValue(),
					Double.valueOf(b.getRadius() * 2).intValue(),
					Double.valueOf(b.getRadius() * 2).intValue());

		}

		// print bricks
		for (IBrick b : getController().getBricks()) {			


			// print Image if one is defined			
			Image brickImg = guiManager.getGameImage(b.getProperties().getProperty(IBrick.PROP_IMG_PATH, ""));			
			if (brickImg != null) {								
				g.drawImage(brickImg, b.getX(), b.getY(), b.getWidth(), b.getHeight(), Color.black, null);

				// print simple rectangle with color	
			}  else {
				g.setColor(Color.getColor(b.getProperties().getProperty(IBrick.PROP_COLOR, Color.blue.toString())));			
				g.fillRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
			}
		}

		// print slider
		IBrick s = getController().getSlider();
		if (s != null) {
			g.setColor(Color.gray);
			g.fillRect(s.getX(), s.getY(), s.getWidth(), s.getHeight());
		}

	}

	/**
	 *  selects the next Menu
	 */
	public void selectNextMenuItem() {
		if (selectedItem + 1 < menuItems.length) {
			selectedItem++;
		}
		this.repaint();
	}

	/**
	 * select the previous Menu
	 */
	public void selectPreviousMenuItem() {
		if (selectedItem - 1 >= 0) {
			selectedItem--;
		}
		this.repaint();
	}

	/**
	 * select the current Menu
	 */
	public void selectCurrentMenuItem() {
		getController().processMenuInput(menuItems[selectedItem]);
	}


	/** */
	public KeyListener getGameKeyListener() {
		if (keyListener == null) {
			keyListener = new GameView2DKeyListener();
		}
		return keyListener;

	}

	/** */
	public IGameController getController() {
		return guiManager.getGameController();
	}

	private void addEditorToolbar() {
		if (bpaEditorComps == null) {
			bpaEditorComps = new BpaEditorToolbar(guiManager, this);
		}
		bpaEditorComps.getTfiHeight().setText(
				String.valueOf((int) guiManager.getGameController().getGridSize().getHeight()));
		bpaEditorComps.getTfiWidth().setText(
				String.valueOf((int) guiManager.getGameController().getGridSize().getWidth()));
		this.add(bpaEditorComps, BorderLayout.EAST);
	}

	private void removeEditorToolbar() {
		if (bpaEditorComps != null) {
			this.remove(bpaEditorComps);
		}
	}

	/** 
	 * Set class name of the brick type that will be created
	 * in level editing mode.
	 */
	public void setNewBrickClassName(String newBrickClassName) {
		this.newBrickClassName = newBrickClassName;
	}

	/**
	 * (non-Javadoc)
	 * @see de.luma.breakout.communication.IGameObserver#updateOnResize()
	 */
	@Override
	public void updateOnResize() {
		Dimension viewSize = getController().getGridSize();
		if (getController().getCreativeMode()) {
			viewSize.setSize(viewSize.getWidth() + TOOLBAR_AREA_WIDTH, viewSize.getHeight());
		}
		this.setPreferredSize(viewSize);
		this.invalidate();
		guiManager.updateLayout();
	}

	/** */
	public void addBpaLevelSelection() {
		if (bpaLevelSelect == null) {
			bpaLevelSelect = new BpaLevelSelection(guiManager);
		}
		this.add(bpaLevelSelect, BorderLayout.CENTER);
	}
	
	private void removeLevelSelection() {
		if (bpaLevelSelect != null) {
			this.remove(bpaLevelSelect);
		}
	}


}
