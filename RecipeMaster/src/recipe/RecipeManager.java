package recipe;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RecipeManager implements PropertyChangeListener{
	private ConcurrentMap<String, Collection<Recipe>> recipes;
	private RecipesDAO recipesDAO;
	
	public RecipeManager(RecipesDAO recipesDAO){
		this.recipesDAO = recipesDAO;
		this.recipes = new ConcurrentHashMap<>(recipesDAO.getRecipes());
		for(String category : recipes.keySet()){
			for(Recipe recipe : recipes.get(category)){
				recipe.addPropertyChangeListener(this);
			}
		}
	}
	
	public Map<String, Collection<Recipe>> getRecipes(){
		return recipes;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent pce) {
		switch(pce.getPropertyName()){
			case "nameChange":
				System.out.println("Name Change");
				break;
			case "categoryChange":
				System.out.println("Category Change");
				changeRecipeCategory((Recipe)pce.getSource(), (String)pce.getOldValue());
				break;
			case "ingredientChange":
				System.out.println("Ingredient Change");
				break;
			case "instructionChange":
				System.out.println("Instruction Change");
				break;
			default:
				System.out.println("No property event name match");
				break;
		}
	}
	
	public void changeRecipeCategory(Recipe recipe, String oldCategory){
		deleteRecipe(recipe, oldCategory);
		newRecipe(recipe);
		refresh();
	}
	
	public void newRecipe(Recipe recipe){
		if(recipes.get(recipe.getCategory()) != null){
			recipes.get(recipe.getCategory()).add(recipe);
		}else{
			Collection<Recipe> newCategoryList = new ArrayList<>();
			newCategoryList.add(recipe);
			recipes.put(recipe.getCategory(), newCategoryList);
		}
		recipesDAO.saveRecipe(recipe);
	}
	
	public void deleteRecipe(Recipe recipe, String category){
		recipes.get(category).remove(recipe);
		recipesDAO.deleteRecipe(recipe, category);
		refresh();
	}
	
	public void save(){
		recipesDAO.saveRecipes(recipes);
	}
	
	private void refresh(){
		for(String category : recipes.keySet()){
			if(recipes.get(category).isEmpty()){
				recipes.remove(category);
			}
		}
	}
}
