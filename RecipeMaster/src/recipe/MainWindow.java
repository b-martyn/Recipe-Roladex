package recipe;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

public class MainWindow {

	private JFrame frame;
	
	private RecipeManager recipeManager = RecipeManagerFactory.getRecipeManager();
	
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
		frame = new JFrame();
		frame.setBounds(100, 100, 850, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		RecipeViewer recipeView = new RecipeViewer(recipeManager.getRecipes(), recipeManager);
		GridBagConstraints gbc_recipeView = new GridBagConstraints();
		gbc_recipeView.fill = GridBagConstraints.BOTH;
		gbc_recipeView.gridx = 0;
		gbc_recipeView.gridy = 0;
		frame.getContentPane().add(recipeView, gbc_recipeView);
	}
}
