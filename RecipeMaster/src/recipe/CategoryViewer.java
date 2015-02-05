package recipe;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class CategoryViewer extends JPanel {
	private static final long serialVersionUID = 1L;

	private Collection<Recipe> recipes;
	private String category;
	private RecipeList list;
	private JSplitPane splitPane;
	
	public CategoryViewer(String category, Collection<Recipe> recipes) {
		this.category = category;
		this.recipes = recipes;
		initialize();
	}
	
	private void initialize(){
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JPanel controlPanel = new CategoryViewerMenu();
		controlPanel.addPropertyChangeListener("New", new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent pce) {
				try {
					RecipeEditor dialog = new RecipeEditor();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
					dialog.addPropertyChangeListener("newrecipe", new PropertyChangeListener() {
						public void propertyChange(PropertyChangeEvent pce) {
							((Recipe)pce.getNewValue()).setCategory(category);
							firePropertyChange("New", category, (Recipe)pce.getNewValue());
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		controlPanel.addPropertyChangeListener("Edit", new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent pce) {
				if(list.getSelected() != null){
					try {
						RecipeEditor dialog = new RecipeEditor(list.getSelected());
						dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
						dialog.setVisible(true);
						dialog.addPropertyChangeListener("newrecipe", new PropertyChangeListener() {
							public void propertyChange(PropertyChangeEvent pce) {
								firePropertyChange("Edit", category, (Recipe)pce.getNewValue());
							}
						});
					} catch (Exception e) {
						//TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		controlPanel.addPropertyChangeListener("Delete", new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent pce) {
				if(list.getSelected() != null){
					firePropertyChange("Delete", true, list.getSelected());
				}
			}
		});
		controlPanel.addPropertyChangeListener("Move", new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent pce) {
				if(list.getSelected() != null){
					firePropertyChange("Move", pce.getNewValue(), list.getSelected());
				}
			}
		});
		GridBagConstraints gbc_contolPanel = new GridBagConstraints();
		gbc_contolPanel.insets = new Insets(0, 0, 5, 0);
		gbc_contolPanel.fill = GridBagConstraints.BOTH;
		gbc_contolPanel.gridx = 0;
		gbc_contolPanel.gridy = 0;
		add(controlPanel, gbc_contolPanel);
		
		splitPane = new JSplitPane();
		
		list = new RecipeList(recipes);
		list.addPropertyChangeListener("recipeSelected", new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent pce) {
				splitPane.setRightComponent(new RecipeDetails((Recipe)pce.getNewValue()));
			}
		});
		splitPane.setLeftComponent(list);
		splitPane.setRightComponent(new JPanel());
		
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.insets = new Insets(0, 0, 5, 0);
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.gridx = 0;
		gbc_splitPane.gridy = 1;
		add(splitPane, gbc_splitPane);
	}
	
	public String getCategory(){
		return category;
	}
	
	public RecipeList getList(){
		return list;
	}
	
	public Collection<Recipe> getRecipes(){
		return recipes;
	}
}
