package recipe;

import java.util.Collection;
import java.util.Map;

public interface RecipeManager{
	void newCategory(String category);
	void deleteRecipe(Recipe recipe);
	void addRecipe(Recipe recipe);
	Map<String, Collection<Recipe>> getRecipes();
	boolean saveRecipe(Recipe recipe);
	boolean saveRecipes(Map<String, Collection<Recipe>> recipes);
}
