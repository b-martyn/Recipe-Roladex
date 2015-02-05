package recipe;

import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class RecipeViewer extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JTabbedPane tabbedPane;
	private Map<String, Collection<Recipe>> recipes;
	
	public RecipeViewer(Map<String, Collection<Recipe>> recipes) {
		this.recipes = recipes;
		initialize();
	}
	
	private void initialize(){
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		add(tabbedPane, gbc_tabbedPane);
		
		for(String category : recipes.keySet()){
			CategoryViewer categoryPanel = new CategoryViewer(category, recipes.get(category));
			categoryPanel.addPropertyChangeListener("Move", new MovePropertyChangeListener(categoryPanel));
			categoryPanel.addPropertyChangeListener("New", new NewRecipePropertyChangeListener(categoryPanel));
			categoryPanel.addPropertyChangeListener("Edit", new EditRecipePropertyChangeListener(categoryPanel));
			categoryPanel.addPropertyChangeListener("Delete", new DeleteRecipePropertyChangeListener(categoryPanel));
			tabbedPane.addTab(category, categoryPanel);
		}
	}
	
	private class MovePropertyChangeListener implements PropertyChangeListener{
		
		private CategoryViewer categoryViewer;
		
		MovePropertyChangeListener(CategoryViewer categoryViewer){
			this.categoryViewer = categoryViewer;
		}
		
		@Override
		public void propertyChange(PropertyChangeEvent pce) {
			JPopupMenu categoryList = new JPopupMenu();
			for(String category : recipes.keySet()){
				if(!category.equals(categoryViewer.getCategory())){
					JMenuItem menuItem = new JMenuItem(category);
					menuItem.addActionListener(new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent arg0) {
							firePropertyChange("recipemove", menuItem.getText(), categoryViewer.getList().getSelected());
							for(Component component : tabbedPane.getComponents()){
								if(((CategoryViewer)component).getCategory().equals(menuItem.getText())){
									((CategoryViewer)component).getRecipes().add(categoryViewer.getList().getSelected());
									((CategoryViewer)component).getList().loadListModel();
									break;
								}
							}
							categoryViewer.getRecipes().remove(categoryViewer.getList().getSelected());
							categoryViewer.getList().loadListModel();
						}
					});
					categoryList.add(menuItem);
				}
			}
			categoryList.addSeparator();
			JMenuItem newCategory = new JMenuItem("New");
			newCategory.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					NewCategory newCategoryDialog = new NewCategory();
					newCategoryDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					newCategoryDialog.setVisible(true);
					newCategoryDialog.addPropertyChangeListener("newcategory", new NewCategoryPropertyChangeListener(categoryViewer));
				}
			});
			categoryList.add(newCategory);
			categoryList.show((Component)pce.getOldValue(), 0, ((Component)pce.getOldValue()).getHeight());
		}
	}
	
	private class NewRecipePropertyChangeListener implements PropertyChangeListener{
		
		private CategoryViewer categoryViewer;
		
		NewRecipePropertyChangeListener(CategoryViewer categoryViewer){
			this.categoryViewer = categoryViewer;
		}
		
		@Override
		public void propertyChange(PropertyChangeEvent pce) {
			recipes.get(categoryViewer.getCategory()).add((Recipe)pce.getNewValue());
			categoryViewer.getList().loadListModel();
			firePropertyChange("New", pce.getOldValue(), pce.getNewValue());
		}
		
	}
	
	private class EditRecipePropertyChangeListener implements PropertyChangeListener{
		
		@SuppressWarnings("unused")
		private CategoryViewer categoryViewer;
		
		EditRecipePropertyChangeListener(CategoryViewer categoryViewer){
			this.categoryViewer = categoryViewer;
		}
		
		@Override
		public void propertyChange(PropertyChangeEvent pce) {
			firePropertyChange("Edit", pce.getOldValue(), pce.getNewValue());
		}
		
	}
	
	private class NewCategoryPropertyChangeListener implements PropertyChangeListener{
		
		private CategoryViewer categoryViewer;
		
		NewCategoryPropertyChangeListener(CategoryViewer categoryViewer){
			this.categoryViewer = categoryViewer;
		}
		
		@Override
		public void propertyChange(PropertyChangeEvent pce) {
			Collection<Recipe> newRecipeList = null;
			CategoryViewer newTab = null;
			if(recipes.containsKey((String)pce.getNewValue())){
				System.out.println(recipes.containsValue(categoryViewer.getList().getSelected()));
				newRecipeList = recipes.get((String)pce.getNewValue());
				newRecipeList.add(categoryViewer.getList().getSelected());
				for(Component component : tabbedPane.getComponents()){
					if(((CategoryViewer)component).getCategory().equals((String)pce.getNewValue())){
						((CategoryViewer)component).getRecipes().add(categoryViewer.getList().getSelected());
						((CategoryViewer)component).getList().loadListModel();
					}
				}
				firePropertyChange("recipemove", pce.getNewValue(), categoryViewer.getList().getSelected());
			}else{
				newRecipeList = new ArrayList<>();
				newRecipeList.add(categoryViewer.getList().getSelected());
				recipes.put((String)pce.getNewValue(), newRecipeList);
				newTab = new CategoryViewer((String)pce.getNewValue(), newRecipeList);
				newTab.addPropertyChangeListener("Move", new MovePropertyChangeListener(newTab));
				newTab.addPropertyChangeListener("New", new NewRecipePropertyChangeListener(newTab));
				newTab.addPropertyChangeListener("Edit", new EditRecipePropertyChangeListener(newTab));
				newTab.addPropertyChangeListener("Delete", new DeleteRecipePropertyChangeListener(newTab));
				tabbedPane.addTab((String)pce.getNewValue(), newTab);
				firePropertyChange("newcategory", this, pce.getNewValue());
			}
			categoryViewer.getRecipes().remove(categoryViewer.getList().getSelected());
			categoryViewer.getList().loadListModel();
		}
	}
	
	private class DeleteRecipePropertyChangeListener implements PropertyChangeListener{
		
		private CategoryViewer categoryViewer;
		
		DeleteRecipePropertyChangeListener(CategoryViewer categoryViewer){
			this.categoryViewer = categoryViewer;
		}
		
		@Override
		public void propertyChange(PropertyChangeEvent pce) {
			categoryViewer.getRecipes().remove(categoryViewer.getList().getSelected());
			categoryViewer.getList().loadListModel();
			firePropertyChange("Delete", pce.getOldValue(), pce.getNewValue());
		}
		
	}
}
