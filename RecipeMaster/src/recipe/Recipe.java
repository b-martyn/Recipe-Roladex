package recipe;
import java.util.Collection;


public class Recipe {
	private String name;
	private String category;
	private Collection<RecipeIngredient> ingredients;
	private Collection<Instruction> instructions;
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Collection<RecipeIngredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(Collection<RecipeIngredient> ingredients) {
		this.ingredients = ingredients;
	}
	
	public Collection<Instruction> getInstructions() {
		return instructions;
	}
	
	public void setInstructions(Collection<Instruction> instructions) {
		this.instructions = instructions;
	}

	@Override
	public String toString() {
		return "Recipe\nname=" + name + "\ningredients\n" + ingredients
				+ ", instructions=" + instructions + "]";
	}
}
