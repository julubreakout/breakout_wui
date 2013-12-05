package de.luma.breakout.view.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;

/**
 * Btn with imageDesign.
 * @author mabausch
 *
 */
@SuppressWarnings("serial")
public class BtnEditor extends JButton {
	
	private static final int STRING_Y = 50;
	
	private IGuiManager guiManager;
	
	/**
	 * Constructor
	 * @param guiManager
	 */
	public BtnEditor(IGuiManager guiManager) {
		super();
		this.guiManager = guiManager;
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(this.getBackground());	
		g2d.drawImage(guiManager.getGameImage("resources/button.png"), 0, 0, getWidth(), getHeight(), this);
		g2d.setColor(IGuiManager.BUTTON_COLOR);
		g.setFont(IGuiManager.BUTTON_FONT);
		
		int stringWidth = (int) g.getFontMetrics().getStringBounds(this.getText(), g).getWidth();
		g.drawString(this.getText(), (this.getWidth() - stringWidth) / 2, STRING_Y);	
	}
	
}
