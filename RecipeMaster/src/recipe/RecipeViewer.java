package recipe;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class RecipeViewer extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JTabbedPane tabbedPane;
	private Map<String, Collection<Recipe>> recipes;
	private RecipeManager recipeManager;
	
	public RecipeViewer(Map<String, Collection<Recipe>> recipes, RecipeManager recipeManager) {
		this.recipes = recipes;
		this.recipeManager = recipeManager;
		initialize();
	}
	
	private void initialize(){
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JPanel controlPanel = new MainMenu();
		GridBagConstraints gbc_contolPanel = new GridBagConstraints();
		gbc_contolPanel.insets = new Insets(0, 0, 5, 0);
		gbc_contolPanel.fill = GridBagConstraints.BOTH;
		gbc_contolPanel.gridx = 0;
		gbc_contolPanel.gridy = 0;
		controlPanel.addPropertyChangeListener("Edit", new PropertyChangeListener(){
			@Override
			public void propertyChange(PropertyChangeEvent pce) {
				editRecipe(getSelectedCategoryViewer());
			}
		});
		controlPanel.addPropertyChangeListener("New", new PropertyChangeListener(){
			@Override
			public void propertyChange(PropertyChangeEvent pce) {
				newRecipe();
			}
		});
		controlPanel.addPropertyChangeListener("Move", new PropertyChangeListener(){
			@Override
			public void propertyChange(PropertyChangeEvent pce) {
				moveRecipe((Component)pce.getNewValue(), getSelectedCategoryViewer());
			}
		});
		controlPanel.addPropertyChangeListener("Delete", new PropertyChangeListener(){
			@Override
			public void propertyChange(PropertyChangeEvent pce) {
				deleteRecipe(getSelectedCategoryViewer());
			}
		});
		add(controlPanel, gbc_contolPanel);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 1;
		add(tabbedPane, gbc_tabbedPane);
		
		if(recipes.isEmpty()){
			tabbedPane.addTab("Welcome", new WelcomeScreen());
		}else{
			for(String category : recipes.keySet()){
				CategoryViewer categoryPanel = new CategoryViewer(category, recipes.get(category));
				tabbedPane.addTab(category, categoryPanel);
			}
		}
	}
	
	public CategoryViewer getSelectedCategoryViewer(){
		return (CategoryViewer)tabbedPane.getComponent(tabbedPane.getSelectedIndex());
	}
	
	public JTabbedPane getTabbedPane(){
		return tabbedPane;
	}
	
	private void moveRecipe(Component initiator, CategoryViewer categoryViewer){
		if(categoryViewer.getList().getSelected() != null){
			JPopupMenu categoryList = new CategoryList(recipes.keySet());
			categoryList.show(initiator, 0, initiator.getHeight());
			categoryList.addPropertyChangeListener("categoryselected", new PropertyChangeListener(){
				@Override
				public void propertyChange(PropertyChangeEvent pce) {
					recipes.get(categoryViewer.getCategory()).remove(categoryViewer.getList().getSelected());
					recipeManager.deleteRecipe(categoryViewer.getList().getSelected());
					categoryViewer.getList().getSelected().setCategory((String)pce.getNewValue());
					recipes.get((String)pce.getNewValue()).add(categoryViewer.getList().getSelected());
					recipeManager.addRecipe(categoryViewer.getList().getSelected());
					categoryViewer.getList().loadListModel();
					for(Component component : tabbedPane.getComponents()){
						if(((CategoryViewer)component).getCategory().equals((String)pce.getNewValue())){
							((CategoryViewer)component).getList().loadListModel();
							break;
						}
					}
				}
			});
			categoryList.addPropertyChangeListener("newcategory", new PropertyChangeListener(){
				@Override
				public void propertyChange(PropertyChangeEvent pce) {
					recipeManager.deleteRecipe(categoryViewer.getList().getSelected());
					recipes.get(categoryViewer.getCategory()).remove(categoryViewer.getList().getSelected());
					categoryViewer.getList().getSelected().setCategory((String)pce.getNewValue());
					if(recipes.containsKey((String)pce.getNewValue())){
						recipes.get((String)pce.getNewValue()).add(categoryViewer.getList().getSelected());
						for(Component component : tabbedPane.getComponents()){
							if(((CategoryViewer)component).getCategory().equals((String)pce.getNewValue())){
								((CategoryViewer)component).getList().loadListModel();
							}
						}
					}else{
						Collection<Recipe> newRecipeList = new ArrayList<>();
						newRecipeList.add(categoryViewer.getList().getSelected());
						recipes.put((String)pce.getNewValue(), newRecipeList);
						tabbedPane.addTab((String)pce.getNewValue(), new CategoryViewer((String)pce.getNewValue(), newRecipeList));
						recipeManager.newCategory((String)pce.getNewValue());
					}
					recipeManager.addRecipe(categoryViewer.getList().getSelected());
					categoryViewer.getList().loadListModel();
				}
			});
		}
	}
	
	private void newRecipe(){
		RecipeEditor dialog = new RecipeEditor(recipes.keySet());
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);
		dialog.addPropertyChangeListener("newrecipe", new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent pce) {
				recipes.get(((Recipe)pce.getNewValue()).getCategory()).add((Recipe)pce.getNewValue());
				reloadTabs();
				recipeManager.addRecipe((Recipe)pce.getNewValue());
			}
		});
		dialog.addPropertyChangeListener("newcategory", new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent pce) {
				recipeManager.newCategory((String)pce.getNewValue());
				recipes.put((String)pce.getNewValue(), new ArrayList<Recipe>());
				tabbedPane.add((String)pce.getNewValue(), new CategoryViewer((String)pce.getNewValue(), recipes.get((String)pce.getNewValue())));
			}
		});
	}
	
	private void editRecipe (CategoryViewer categoryViewer){
		if(categoryViewer.getList().getSelected() != null){
			RecipeEditor dialog = new RecipeEditor(recipes.keySet(), categoryViewer.getList().getSelected());
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
			dialog.addPropertyChangeListener("editrecipe", new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent pce) {
					if(categoryViewer.getCategory().equals(((Recipe)pce.getNewValue()).getCategory())){
						recipeManager.addRecipe((Recipe)pce.getNewValue());
					}else{
						String category = ((Recipe)pce.getNewValue()).getCategory();
						((Recipe)pce.getNewValue()).setCategory(categoryViewer.getCategory());
						recipeManager.deleteRecipe((Recipe)pce.getNewValue());
						recipes.get(categoryViewer.getCategory()).remove((Recipe)pce.getNewValue());
						((Recipe)pce.getNewValue()).setCategory(category);
						recipes.get(category).add((Recipe)pce.getNewValue());
						recipeManager.addRecipe((Recipe)pce.getNewValue());
					}
					reloadTabs();
				}
			});
			dialog.addPropertyChangeListener("newcategory", new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent pce) {
					recipeManager.newCategory((String)pce.getNewValue());
					recipes.put((String)pce.getNewValue(), new ArrayList<Recipe>());
					tabbedPane.add((String)pce.getNewValue(), new CategoryViewer((String)pce.getNewValue(), recipes.get((String)pce.getNewValue())));
				}
			});
		}
	}
	
	private void deleteRecipe(CategoryViewer categoryViewer){
		if(categoryViewer.getList().getSelected() != null){
			categoryViewer.getRecipes().remove(categoryViewer.getList().getSelected());
			categoryViewer.getList().loadListModel();
			recipeManager.deleteRecipe(categoryViewer.getList().getSelected());
		}
	}
	
	private void reloadTabs(){
		for(Component component : tabbedPane.getComponents()){
			if(component instanceof CategoryViewer){
				((CategoryViewer)component).getList().loadListModel();
			}
		}
	}
}
