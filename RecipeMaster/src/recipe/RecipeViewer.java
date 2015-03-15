package recipe;

import java.util.Collection;
import java.util.Map;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class RecipeViewer extends JTabbedPane implements ChangeListener, ListSelectionListener{
	private static final long serialVersionUID = 1L;
	
	private Map<String, Collection<Recipe>> recipes;
	private Recipe selectedRecipe = null;
	
	public RecipeViewer(Map<String, Collection<Recipe>> recipes){
		this.recipes = recipes;
		addChangeListener(this);
		reloadPanes();
	}
	
	public void reloadPanes(){
		removeAll();
		int index = 0;
		for(String category : recipes.keySet()){
			CategoryViewer categoryPanel = new CategoryViewer(category, recipes.get(category));
			categoryPanel.getList().addListSelectionListener(this);
			addTab(category, categoryPanel);
			setTabComponentAt(index++, new DeleteableTabComponent(this));
		}
		for(CategoryViewer categoryViewer : getTabComponents()){
			categoryViewer.getList().reload();
		}
		//reloadTabs();
	}
	
	public Recipe getSelectedRecipe(){
		return selectedRecipe;
	}
	
	private CategoryViewer[] getTabComponents(){
		CategoryViewer[] myComponents = new CategoryViewer[getTabCount()];
		for(int i = 0; i < getTabCount(); i++){
			myComponents[i] = (CategoryViewer)getComponentAt(i);
		}
		return myComponents;
	}

	@Override
	public void stateChanged(ChangeEvent changeEvent) {
		if(getSelectedIndex() == -1){
			selectedRecipe = null;
		}else{
			selectedRecipe = ((CategoryViewer)getComponentAt(getSelectedIndex())).getSelectedRecipe();
			System.out.println(((CategoryViewer)getComponentAt(getSelectedIndex())).getSelectedRecipe());
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent listSelectionEvent) {
		fireStateChanged();
	}
}