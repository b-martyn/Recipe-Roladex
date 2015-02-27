package recipe;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class RecipeCategoryEditor extends JPopupMenu{
	private static final long serialVersionUID = 1L;
	
	private Set<String> categoryList;
	private Recipe recipe;
	
	public RecipeCategoryEditor(Set<String> categoryList){
		this.recipe = new Recipe();
		this.categoryList = categoryList;
		initialize();
	}
	
	public RecipeCategoryEditor(Recipe recipe, Set<String> categoryList){
		this.recipe = recipe;
		this.categoryList = categoryList;
		initialize();
	}
	
	private void initialize(){
		for(String category : categoryList){
			if(!category.equalsIgnoreCase(recipe.getCategory())){
				JMenuItem menuItem = new JMenuItem(category);
				menuItem.setActionCommand("categoryselected");
				add(menuItem);
			}
		}
		addSeparator();
		JMenuItem newCategory = new JMenuItem("New");
		newCategory.setActionCommand("newcategoryselected");
		add(newCategory);
	}
	
	public void addActionListener(ActionListener actionListener){
		for(Component component : getComponents()){
			if(component instanceof AbstractButton){
				((AbstractButton)component).addActionListener(actionListener);
			}
		}
	}
}
