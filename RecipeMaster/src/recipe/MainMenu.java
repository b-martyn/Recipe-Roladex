package recipe;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagLayout;

import javax.swing.AbstractButton;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenu extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JMenuBar menuBar;
	
	public MainMenu() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		menuBar = new JMenuBar();
		
		JMenuItem mnuNew = new JMenuItem("New");
		setupMenuItem(mnuNew, "mnuNew");
		menuBar.add(mnuNew);
		
		JMenuItem mnuEdit = new JMenuItem("Edit");
		setupMenuItem(mnuEdit, "mnuEdit");
		menuBar.add(mnuEdit);
		
		JMenuItem mnuDelete = new JMenuItem("Delete");
		setupMenuItem(mnuDelete, "mnuDelete");
		menuBar.add(mnuDelete);
		
		JMenuItem mnuMove = new JMenuItem("Move");
		setupMenuItem(mnuMove, "mnuMove");
		menuBar.add(mnuMove);
		
		add(menuBar);
	}
	
	private void setupMenuItem(JMenuItem menuItem, String name){
		menuItem.setName(name);
		menuItem.setActionCommand(name);
		menuItem.addMouseListener(new MenuMouseAdapter(menuItem));
	}
	
	public void addActionListener(ActionListener actionListener){
		for(Component component : menuBar.getComponents()){
			AbstractButton menuItem = (AbstractButton)component;
			menuItem.addActionListener(actionListener);
		}
	}
	
	private class MenuMouseAdapter extends MouseAdapter{
		private JMenuItem menuItem;
		private Color originalBackground;
		
		MenuMouseAdapter(JMenuItem menuItem){
			super();
			this.menuItem = menuItem;
			this.originalBackground = menuItem.getBackground();
		}
		 
		@Override
		public void mouseEntered(MouseEvent mouseEvent) {
			menuItem.setBackground(Color.GRAY);
		}
		
		@Override
		public void mouseExited(MouseEvent mouseEvent) {
			menuItem.setBackground(originalBackground);
		}
		
		@Override
		public void mousePressed(MouseEvent mouseEvent){
			menuItem.setBackground(Color.LIGHT_GRAY);
		}
		
		@Override
		public void mouseReleased(MouseEvent mouseEvent){
			menuItem.setBackground(Color.GRAY);
		}
	}
}
