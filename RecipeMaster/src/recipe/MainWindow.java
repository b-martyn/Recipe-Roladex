package recipe;

import java.awt.EventQueue;

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
		frame.setVisible(true);
	}
}
