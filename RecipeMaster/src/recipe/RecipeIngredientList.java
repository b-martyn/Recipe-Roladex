package recipe;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class RecipeIngredientList extends RecipeComponentList<RecipeIngredient> {
	private static final long serialVersionUID = 1L;

	public RecipeIngredientList(Recipe recipe) {
		super(recipe);
	}

	@Override
	public void loadListModel() {
		DefaultListModel<RecipeIngredient> ingredientModel = new DefaultListModel<RecipeIngredient>();
		if(recipe.getIngredients() != null){
			for(RecipeIngredient ingredient : recipe.getIngredients()){
				ingredientModel.addElement(ingredient);
			}
		}
		list.setModel(ingredientModel);
	}

	@Override
	protected ListCellRenderer<RecipeIngredient> cellRenderer() {
		return new RecipeIngredientListRenderer();
	}
	
	private class RecipeIngredientListRenderer implements ListCellRenderer<RecipeIngredient> {
		
		@Override
		public Component getListCellRendererComponent(
				JList<? extends RecipeIngredient> jList, RecipeIngredient value, int index,	boolean isSelected, boolean cellHasFocus) {
			JLabel renderer = (JLabel) new DefaultListCellRenderer().getListCellRendererComponent(jList, value, index, isSelected, cellHasFocus);
			
			if(renderer != null){
				renderer.setText(value.getMeasurement().getAmount() + " " + value.getMeasurement().getType() + " " + value.getIngredient().getName());
			}
			
			return renderer;
		}
	}
}
