package recipe;

import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

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
		frame.setBounds(100, 100, 550, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		RecipeViewer recipeView = new RecipeViewer(recipeManager.getRecipes());
		GridBagConstraints gbc_recipeView = new GridBagConstraints();
		gbc_recipeView.fill = GridBagConstraints.BOTH;
		gbc_recipeView.gridx = 0;
		gbc_recipeView.gridy = 0;
		recipeView.addPropertyChangeListener("recipemove", new PropertyChangeListener(){
			@Override
			public void propertyChange(PropertyChangeEvent pce) {
				recipeManager.deleteRecipe((Recipe)pce.getNewValue());
				((Recipe)pce.getNewValue()).setCategory((String)pce.getOldValue());
				recipeManager.addRecipe((Recipe)pce.getNewValue());
				
			}
		});
		recipeView.addPropertyChangeListener("newcategory", new PropertyChangeListener(){
			@Override
			public void propertyChange(PropertyChangeEvent pce){
				recipeManager.newCategory((String)pce.getNewValue());
			}
		});
		recipeView.addPropertyChangeListener("New", new PropertyChangeListener(){
			@Override
			public void propertyChange(PropertyChangeEvent pce){
				recipeManager.addRecipe((Recipe)pce.getNewValue());
			}
		});
		recipeView.addPropertyChangeListener("Edit", new PropertyChangeListener(){
			@Override
			public void propertyChange(PropertyChangeEvent pce){
				recipeManager.addRecipe((Recipe)pce.getNewValue());
			}
		});
		recipeView.addPropertyChangeListener("Delete", new PropertyChangeListener(){
			@Override
			public void propertyChange(PropertyChangeEvent pce){
				recipeManager.deleteRecipe((Recipe)pce.getNewValue());
			}
		});
		frame.getContentPane().add(recipeView, gbc_recipeView);
	}

}
