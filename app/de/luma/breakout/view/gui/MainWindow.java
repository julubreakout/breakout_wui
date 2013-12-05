package de.luma.breakout.view.gui;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import de.luma.breakout.controller.IGameController;

/**
 * Main GUI window to hold the game view.
 * @author mabausch
 *
 */
@SuppressWarnings("serial")
public class MainWindow extends JFrame implements IGuiManager {	

	private GameView2D bpaGameView;	
	private IGameController controller;

	// store image ressources
	private MediaTracker mediaTracker;
	private final Map<String, Image> mapImages = new HashMap<String, Image>();

    /** */
	public MainWindow(IGameController controller) {
		super();
		this.controller = controller;
		
		this.setTitle("Breakout");		
		this.setVisible(true);		
		this.add(getBpaGameView2D(), BorderLayout.CENTER);
		this.pack();	

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		
		loadImageResources();
	}

	/**
	 * This mehtod returns a Image loaded from the specified FilePath
	 * @return Image
	 */
	public Image getGameImage(String filePath) {		
		if (filePath.equals("")) {
			return null;
		}

		Image retVal = getMapImages().get(filePath);

		// if there is no image in map try to load it
		if (retVal == null) {
			retVal = getToolkit().getImage(filePath);	
			mapImages.put(filePath, retVal);
			getMediaTracker().addImage(retVal, filePath.hashCode());

			try { 
				getMediaTracker().waitForAll(); 				
			} catch (InterruptedException ex) { 
				return null;
			}
		}		

		return retVal;
	}


	private boolean loadImageResources() {	
		Image img = null;
		String[] images = new String[] {
				"resources/button.png",
				"resources/button_selected.png",
				"resources/breakout_logo.png",		
				"resources/menu_background.png",
				"resources/levelbutton.png"
		};

		for (String str : images) {
			img = getToolkit().getImage(str);	
			mapImages.put(str, img);
			getMediaTracker().addImage(img, str.hashCode());
		}

		try { 
			getMediaTracker().waitForAll(); 
			return true;
		} catch (InterruptedException ex) { 
			return false;
		}
	}

	private MediaTracker getMediaTracker(){
		if (mediaTracker == null) {
			mediaTracker = new MediaTracker(this);
		}
		return mediaTracker;
	}

	/**
	 * Return the image by file path. Has to be loaded first.
	 */
	public Map<String, Image> getMapImages() {
		return mapImages;
	}

	/**
	 * Return the game view hold by this window.
	 */
	public GameView2D getBpaGameView2D() {
		if (bpaGameView == null) {
			bpaGameView = new GameView2D(this);			
		}
		return bpaGameView;
	}

	/**
	 * (non-Javadoc)
	 * @see de.luma.breakout.view.gui.IGuiManager#updateLayout()
	 */
	@Override
	public void updateLayout() {
		this.pack();
	}

	/**
	 * (non-Javadoc)
	 * @see de.luma.breakout.view.gui.IGuiManager#kill()
	 */
	@Override
	public void kill() {
		this.dispose();	
	}

	/**
	 * (non-Javadoc)
	 * @see de.luma.breakout.view.gui.IGuiManager#getGameController()
	 */
	@Override
	public IGameController getGameController() {
		return controller;
	}

}




