package recipe;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class Recipes implements PropertyChangeListener{
	private Map<String, Collection<Recipe>> recipes;
	
	public Recipes(Map<String, Collection<Recipe>> recipes){
		this.recipes = recipes;
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
	}
	
	public void newRecipe(Recipe recipe){
		if(recipes.get(recipe.getCategory()) != null){
			recipes.get(recipe.getCategory()).add(recipe);
		}else{
			Collection<Recipe> newCategoryList = new ArrayList<>();
			newCategoryList.add(recipe);
			recipes.put(recipe.getCategory(), newCategoryList);
		}
	}
	
	public void deleteRecipe(Recipe recipe, String category){
		recipes.get(category).remove(recipe);
	}
}
