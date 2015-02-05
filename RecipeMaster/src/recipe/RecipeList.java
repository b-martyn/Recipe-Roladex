package recipe;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JList;

import java.awt.GridBagConstraints;
import java.util.Collection;

import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class RecipeList extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private Collection<Recipe> recipes;
	private JList<Recipe> list;
	private Recipe selectedRecipe;
	
	public RecipeList(Collection<Recipe> recipes) {
		this.recipes = recipes;
		initialize();
	}
	
	private void initialize() {
		this.setMinimumSize(new Dimension(100, 0));
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		add(scrollPane, gbc_scrollPane);
		
		list = new JList<Recipe>();
		loadListModel();
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if(list.getSelectedValue() != null){
					selectedRecipe = list.getSelectedValue();
					firePropertyChange("recipeSelected", true, selectedRecipe);
				}
			}
		});
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setCellRenderer(new RecipeListRenderer());
		scrollPane.setViewportView(list);
	}
	
	public Collection<Recipe> getRecipes(){
		return recipes;
	}
	
	public Recipe getSelected(){
		return selectedRecipe;
	}
	
	public void setSelected(Recipe recipe){
		list.setSelectedValue(recipe, true);
	}
	
	public void loadListModel(){
		DefaultListModel<Recipe> recipeModel = new DefaultListModel<Recipe>();
		for(Recipe recipe : recipes){
			recipeModel.addElement(recipe);
		}
		list.setModel(recipeModel);
	}
	
	private class RecipeListRenderer implements ListCellRenderer<Recipe> {
		
		@Override
		public Component getListCellRendererComponent(
				JList<? extends Recipe> jList, Recipe value, int index,	boolean isSelected, boolean cellHasFocus) {
			JLabel renderer = (JLabel) new DefaultListCellRenderer().getListCellRendererComponent(jList, value, index, isSelected, cellHasFocus);
			
			if(renderer != null){
				renderer.setText(value.getName());
			}
			
			return renderer;
		}
		
	}
}
