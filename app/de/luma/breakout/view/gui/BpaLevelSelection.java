package de.luma.breakout.view.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.luma.breakout.communication.ObservableGame.MENU_ITEM;

/**
 * Panel to hold a button for each loadable level.
 */
@SuppressWarnings("serial")
public class BpaLevelSelection extends JPanel {	
	
	private static final int BORDER_WIDTH = 10;

	private IGuiManager guiManager;
	private JScrollPane scrollPane;
	private JPanel buttonsPanel;
	private ActionListener lvlBtnListener;

	/**
	 * Constructor
	 * @param guiMgr
	 */
	public BpaLevelSelection(IGuiManager guiMgr) {
		super();		
		this.guiManager = guiMgr;
		initializeComponents();
	}
	
	/**
	 * Initialize Components
	 *  put everything on a scolpane
	 *  set Background Color
	 */
	private void initializeComponents() {
		this.setBackground(Color.BLACK);
		
		this.setPreferredSize(new Dimension(IGuiManager.MENU_X_Y_RANGE, IGuiManager.MENU_X_Y_RANGE));
		buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, BORDER_WIDTH, BORDER_WIDTH));
		buttonsPanel.setBackground(Color.BLACK);
		
		scrollPane = new JScrollPane(buttonsPanel);
		scrollPane.setPreferredSize(new Dimension(IGuiManager.MENU_X_Y_RANGE, IGuiManager.MENU_X_Y_RANGE));
		this.setLayout(new BorderLayout());
		this.add(scrollPane, BorderLayout.CENTER);
	}
	
	/**
	 * Create a button for each loadable level.
	 */
	public void loadLevels() {
		buttonsPanel.removeAll();
		
		int i = 1;
		for (String filepath : guiManager.getGameController().getLevelList()) {
			BtnLevelSelection btn = new BtnLevelSelection(filepath, guiManager);
			btn.addActionListener(getLevelButtonListener());
			btn.setText(String.valueOf(i));
			i++;
			buttonsPanel.add(btn);
		}
	}
	
	/**
	 * Actionlistener for loading a new Level.
	 * fetch Filepath form Btn
	 * load Level
	 * and signals Gamecontroller to continue game
	 * 
	 *   in editmode Editor is started.
	 *   
	 *   in gamemode Real game is started.
	 * 
	 * @return
	 */
	private ActionListener getLevelButtonListener() {
		if (lvlBtnListener == null) {
			lvlBtnListener = new ActionListener() {
				
				/**
				 *  actionPerformed
				 */
				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getSource() instanceof BtnLevelSelection) {
						BtnLevelSelection btn = (BtnLevelSelection) e.getSource();						
						
						guiManager.getGameController().loadLevel(new File(btn.getFilePath()));
						guiManager.getGameController().processMenuInput(MENU_ITEM.MNU_CONTINUE);
					}
				}
			};
			
		}

		return lvlBtnListener;
	}
	
}