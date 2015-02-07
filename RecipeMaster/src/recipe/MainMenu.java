package recipe;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.GridBagLayout;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenu extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public MainMenu() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JMenuBar menuBar = new JMenuBar();
		
		JMenuItem mntmNew = new JMenuItem("New");
		mntmNew.addMouseListener(new MenuMouseAdapter(mntmNew));
		menuBar.add(mntmNew);
		
		JMenuItem mntmEdit = new JMenuItem("Edit");
		mntmEdit.addMouseListener(new MenuMouseAdapter(mntmEdit));
		menuBar.add(mntmEdit);
		
		JMenuItem mntmDelete = new JMenuItem("Delete");
		mntmDelete.addMouseListener(new MenuMouseAdapter(mntmDelete));
		menuBar.add(mntmDelete);
		
		JMenuItem mntmMove = new JMenuItem("Move");
		mntmMove.addMouseListener(new MenuMouseAdapter(mntmMove));
		menuBar.add(mntmMove);
		
		add(menuBar);
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
		
		@Override
		public void mouseClicked(MouseEvent mouseEvent){
			firePropertyChange(menuItem.getText(), true, menuItem);
		}
	}
}
