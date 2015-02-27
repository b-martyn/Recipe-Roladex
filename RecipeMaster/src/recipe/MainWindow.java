package recipe;

import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MainWindow implements WindowListener, ActionListener{

	private RecipeViewer frame;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public MainWindow() {
		initialize();
	}
	
	private void initialize() {
		frame = new RecipeViewer(new Recipes(RecipeManagerFactory.getRecipeManager().getRecipes()));
		frame.addWindowListener(this);
		frame.addActionListener(this);
		frame.setBounds(100, 100, 850, 500);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	private void save(){
		System.out.println("Save");
		//RecipeManagerFactory.getRecipeManager().saveRecipes(recipeViewer.getRecipes().getRecipes());
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// Do Nothing
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		save();
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// Do Nothing
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// Do Nothing
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// Do Nothing
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// Do Nothing
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// Do Nothing
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		switch(actionEvent.getActionCommand()){
			case "mnuFileSave":
				save();
				break;
		}
	}
}
