package recipe;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class CategoryViewer extends JPanel implements PropertyChangeListener{
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
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		splitPane = new JSplitPane();
		
		list = new RecipeList(recipes);
		list.addPropertyChangeListener(this);
		
		splitPane.setLeftComponent(list);
		splitPane.setRightComponent(new JPanel());
		
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.insets = new Insets(0, 0, 5, 0);
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.gridx = 0;
		gbc_splitPane.gridy = 0;
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
	
	public void reload(){
		if(list.getSelected() != null){
			splitPane.setRightComponent(new RecipeDetails(list.getSelected()));
		}else{
			splitPane.setRightComponent(new JPanel());
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
		switch(propertyChangeEvent.getPropertyName()){
			case "selectedRecipeChange":
				reload();
				firePropertyChange(propertyChangeEvent.getPropertyName(), propertyChangeEvent.getOldValue(), propertyChangeEvent.getNewValue());
				break;
		}
	}
}
