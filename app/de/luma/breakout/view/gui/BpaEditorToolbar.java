package de.luma.breakout.view.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.luma.breakout.communication.MENU_ITEM;
import de.luma.breakout.data.objects.IBrick;
import de.luma.breakout.data.objects.impl.Slider;

/**
 * Toolbar for save load and reset
 *         for resizeing
 *         for choosing bricks.
 * @author mabausch
 *
 */
@SuppressWarnings("serial")
public class BpaEditorToolbar extends JPanel {
	
	private static final int DEFAULT_GRID_SIZE = 500;
	private static final int EDITOR_PAN_WIDTH = 200;
	private static final int EDITOR_PAN_HIGHT = 300;
	private static final int LABEL_WIDTH = 80;
	private static final int COMP_HEIGHT = 20;
	private static final int COMP_WIDTH = 100;
	private static final int BORDER_WIDTH = 10;
	private static final int LAYOUT_WIDTH = 5;
	
	
	private JTextField tfiWidth;
	private JTextField tfiHeight;
	private BtnEditor btnSave;
	private BtnEditor btnLoad;
	private BtnEditor btnReset;

	private IGuiManager guiManager;
	private GameView2D gameView;

	private JPanel bpaSize;
	private JPanel bpaBricks;
	private JPanel bpaButtons;

	private ActionListener btnBricksActionListener;
	private ActionListener resizeActionListener;
	
	/**
	 * Constructor
	 * initialize components of the Editior Toolbar.
	 *   - set Background
	 *   - set Resizeable Pane
	 *   - set Brick Choose Pane
	 *   - set Button Pane
	 * @param guiManager
	 * @param gameView
	 */
	public BpaEditorToolbar(IGuiManager guiManager, GameView2D gameView) {
		super();
		this.guiManager = guiManager;
		this.gameView = gameView;
		
		this.setLayout(new FlowLayout(FlowLayout.CENTER, BORDER_WIDTH, BORDER_WIDTH));
		this.setBackground(Color.BLACK);
		this.setPreferredSize(new Dimension(EDITOR_PAN_WIDTH, EDITOR_PAN_HIGHT));
		this.add(getBpaSize());
		this.add(getBpaBricks());
		this.add(getBpaButtons());		

	}

	/**
	 * Panel for resizeing the Level.
	 * 
	 *  create two Textfields 
	 *    - one for width
	 *    - one for height.
	 * 
	 * @return
	 */
	private JPanel getBpaSize() {
		if (bpaSize == null) {
			bpaSize = new JPanel();
			bpaSize.setLayout(new FlowLayout(FlowLayout.CENTER));
			bpaSize.setBackground(Color.BLACK);
			bpaSize.setPreferredSize(new Dimension(EDITOR_PAN_WIDTH, COMP_WIDTH));

			// width textbox
			JLabel lblWidth = new JLabel("Width");
			lblWidth.setPreferredSize(new Dimension(LABEL_WIDTH, COMP_HEIGHT));
			tfiWidth = new JTextField();
			tfiWidth.setPreferredSize(new Dimension(LABEL_WIDTH, COMP_HEIGHT));
			setColors(lblWidth);
			setColors(tfiWidth);
			tfiWidth.addActionListener(getResizeActionListener());


			// height textbox
			JLabel lblHeight = new JLabel("Height");
			lblHeight.setPreferredSize(new Dimension(LABEL_WIDTH, COMP_HEIGHT));
			tfiHeight = new JTextField();
			tfiHeight.setPreferredSize(new Dimension(LABEL_WIDTH, COMP_HEIGHT));
			setColors(lblHeight);
			setColors(tfiHeight);		
			tfiHeight.addActionListener(getResizeActionListener());

			bpaSize.add(lblHeight);
			bpaSize.add(lblWidth);
			bpaSize.add(tfiHeight);		
			bpaSize.add(tfiWidth);			
		}
		return bpaSize;
	}

	/**
	 * Create Button Panel
	 * 
	 *   - save Button
	 *   - load Button
	 *   - reset Button
	 * @return
	 */
	private JPanel getBpaButtons() {
		if (bpaButtons == null) {
			bpaButtons = new JPanel();
			bpaButtons.setLayout(new FlowLayout(FlowLayout.CENTER, LAYOUT_WIDTH, 0));
			bpaButtons.setBackground(Color.BLACK);
			bpaButtons.setPreferredSize(new Dimension(EDITOR_PAN_WIDTH, EDITOR_PAN_HIGHT));


			// save button
			btnSave = new BtnEditor(guiManager);			
			btnSave.setPreferredSize(new Dimension(EDITOR_PAN_WIDTH, LABEL_WIDTH));
			btnSave.addActionListener(new ActionListener() {				
			
				/**
				 *  call controller to save Level
				 *   ... alalalallaaaaaaaa
				 */
				@Override
				public void actionPerformed(ActionEvent e) {
					guiManager.getGameController().saveLevel();
				}
			});
			btnSave.setText("Save");

			// load button
			btnLoad = new BtnEditor(guiManager);
			btnLoad.setPreferredSize(new Dimension(EDITOR_PAN_WIDTH, LABEL_WIDTH));
			btnLoad.addActionListener(new ActionListener() {		
				/**
				 *  call controller to display level choose.
				 */
				@Override
				public void actionPerformed(ActionEvent e) {
					guiManager.getGameController().processMenuInput(MENU_ITEM.MNU_LEVEL_CHOOSE);
				}
			});
			btnLoad.setText("Load");


			// reset button
			btnReset = new BtnEditor(guiManager);
			btnReset.setPreferredSize(new Dimension(EDITOR_PAN_WIDTH, LABEL_WIDTH));
			btnReset.addActionListener(new ActionListener() {		
				/**
				 * reset Level.
				 */
				@Override
				public void actionPerformed(ActionEvent e) {
					resetLevel();
				}
			});
			btnReset.setText("Reset");

			bpaButtons.add(btnSave);
			bpaButtons.add(btnLoad);
			bpaButtons.add(btnReset);
		}
		return bpaButtons;
	}

	/**
	 * Prepare a new level with default settings + Slider.
	 */
	public void resetLevel() {
		guiManager.getGameController().clearGrid();
		guiManager.getGameController().setSlider(new Slider(0, 0, COMP_WIDTH, COMP_HEIGHT));
		guiManager.getGameController().setGridSize(DEFAULT_GRID_SIZE, DEFAULT_GRID_SIZE);
	}

	private JPanel getBpaBricks() {
		if (bpaBricks == null) {
			bpaBricks = new JPanel();
			bpaBricks.setLayout(new FlowLayout(FlowLayout.CENTER));
			bpaBricks.setBackground(Color.BLACK);
			bpaBricks.setPreferredSize(new Dimension(EDITOR_PAN_WIDTH, COMP_WIDTH));
			bpaBricks.setBorder(BorderFactory.createLineBorder(IGuiManager.TEXT_COLOR));

			BtnEditorBrick btn = null;
			for (IBrick brick : guiManager.getGameController().getBrickClasses()) {				
				btn = new BtnEditorBrick(guiManager, brick);
				btn.setPreferredSize(new Dimension(LABEL_WIDTH, COMP_HEIGHT));
				btn.addActionListener(getBtnBrickActionListener());
				bpaBricks.add(btn);
			}			
		}
		return bpaBricks;
	}

	private ActionListener getBtnBrickActionListener() {
		if (btnBricksActionListener == null) {
			btnBricksActionListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					BtnEditorBrick btn = (BtnEditorBrick) e.getSource();
					gameView.setNewBrickClassName(btn.getBrickInstance().getClass().getName());
				}
			};			
		}
		return btnBricksActionListener;
	}

	private ActionListener getResizeActionListener() {
		if (resizeActionListener == null) {
			resizeActionListener = new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent e) {
					Dimension gridDim = guiManager.getGameController().getGridSize();

					doOnResizeAction(gridDim);
				}		
			};
		}
		return resizeActionListener;		
	}
	
	private void doOnResizeAction(Dimension gridDim) {
		if (!getTfiHeight().getText().trim().equals("")) {
			int newHeight = Integer.valueOf(getTfiHeight().getText());
			if (newHeight >= DEFAULT_GRID_SIZE) {
				gridDim.setSize(gridDim.getWidth(), newHeight);
			}
		}

		if (!getTfiWidth().getText().trim().equals("")) {
			gridDim.setSize(Integer.valueOf(getTfiWidth().getText()), gridDim.getHeight());
		}					

		guiManager.getGameController().setGridSize(gridDim.width, gridDim.height);	

		gameView.requestFocusInWindow();
	}

	private void setColors(JComponent c) {
		c.setForeground(IGuiManager.TEXT_COLOR);
		c.setBackground(Color.BLACK);
	}

	public JTextField getTfiWidth() {
		return tfiWidth;
	}

	public JTextField getTfiHeight() {
		return tfiHeight;
	}


}
