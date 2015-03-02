package recipe;

import java.util.Collection;
import java.util.Map;

public interface RecipesDAO{
	Map<String, Collection<Recipe>> getRecipes();
	boolean deleteRecipe(Recipe recipe, String category);
	boolean saveRecipe(Recipe recipe);
	void saveRecipes(Map<String, Collection<Recipe>> recipes);
}
