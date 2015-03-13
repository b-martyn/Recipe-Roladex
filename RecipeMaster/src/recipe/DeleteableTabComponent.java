package recipe;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicButtonUI;

public class DeleteableTabComponent extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final JTabbedPane pane;
	
	public DeleteableTabComponent(JTabbedPane pane){
		super(new FlowLayout(FlowLayout.LEFT, 0, 0));
		this.pane = pane;
		setOpaque(false);
		JLabel label = new TabTitleLabel();
		add(label);
		
		JButton button = new TabButton();
		add(button);
	}
	
	private class TabTitleLabel extends JLabel{
		private static final long serialVersionUID = 1L;

		@Override
		public String getText(){
			int i = pane.indexOfTabComponent(DeleteableTabComponent.this);
			if(i != -1){
				return pane.getTitleAt(i);
			}
			return null;
		}
	}
	
	private class TabButton extends JButton implements ActionListener, MouseListener{
		private static final long serialVersionUID = 1L;
		
		private final int size = 17;
		
		public TabButton(){
			setPreferredSize(new Dimension(size, size));
			setToolTipText("Close this Tab");
			setUI(new BasicButtonUI());
			setContentAreaFilled(false);
			setFocusable(false);
			setBorder(BorderFactory.createEtchedBorder());
			setBorderPainted(false);
			addMouseListener(this);
			setRolloverEnabled(true);
			addActionListener(this);
		}
		
		@Override
		public void updateUI(){
		}
		
		//Draw Cross for close button
		@Override
		protected void paintComponent(Graphics graphics){
			super.paintComponent(graphics);
			Graphics2D graphics2D = (Graphics2D)graphics.create();
			if(getModel().isPressed()){
				graphics2D.translate(1, 1);
			}
			graphics2D.setStroke(new BasicStroke(2));
			graphics2D.setColor(Color.BLACK);
			if(getModel().isRollover()){
				graphics2D.setColor(Color.MAGENTA);
			}
			int delta = 6;
			graphics2D.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
			graphics2D.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
			graphics2D.dispose();
		}
		
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			int i = pane.indexOfTabComponent(DeleteableTabComponent.this);
			System.out.println(i);
			if(i != -1){
				pane.remove(i);
			}
		}

		@Override
		public void mouseClicked(MouseEvent mouseEvent) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent mouseEvent) {
			Component component = mouseEvent.getComponent();
			if(component instanceof AbstractButton){
				AbstractButton button = (AbstractButton)component;
				button.setBorderPainted(true);
			}
		}

		@Override
		public void mouseExited(MouseEvent mouseEvent) {
			Component component = mouseEvent.getComponent();
			if(component instanceof AbstractButton){
				AbstractButton button = (AbstractButton)component;
				button.setBorderPainted(false);
			}
		}

		@Override
		public void mousePressed(MouseEvent mouseEvent) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent mouseEvent) {
			// TODO Auto-generated method stub
			
		}
	}
}
