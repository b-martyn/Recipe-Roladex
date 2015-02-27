package recipe;

import java.util.Collection;
import java.util.Map;

public interface RecipesDAO{
	Map<String, Collection<Recipe>> getRecipes();
	boolean saveRecipe(Recipe recipe);
	boolean saveRecipes(Map<String, Collection<Recipe>> recipes);
}
