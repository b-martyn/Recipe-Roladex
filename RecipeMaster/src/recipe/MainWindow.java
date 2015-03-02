package recipe;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class MainWindow {

	private RecipeViewer frame;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new MainWindow();
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
		frame = new RecipeViewer(new RecipeManager(new RecipesDAOFilesImpl()));
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setBounds(100, 100, 850, 500);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}
}
