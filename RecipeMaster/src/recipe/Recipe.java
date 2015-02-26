package recipe;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;


public class Recipe {
	private String name;
	private String category = "";
	private Collection<RecipeIngredient> ingredients = new ArrayList<>();;
	private Collection<Instruction> instructions = new ArrayList<>();;
	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		String oldValue = this.name;
		this.name = name;
		pcs.firePropertyChange("nameChange", oldValue, this.name);
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		String oldValue = this.category;
		this.category = category;
		pcs.firePropertyChange("categoryChange", oldValue, this.category);
	}

	public Collection<RecipeIngredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(Collection<RecipeIngredient> ingredients) {
		Collection<RecipeIngredient> oldValue = new ArrayList<>();
		for(RecipeIngredient ingredient : this.ingredients){
			oldValue.add(ingredient);
		}
		this.ingredients = ingredients;
		pcs.firePropertyChange("ingredientChange", oldValue, this.ingredients);
	}
	
	public Collection<Instruction> getInstructions() {
		return instructions;
	}
	
	public void setInstructions(Collection<Instruction> instructions) {
		Collection<Instruction> oldValue = new ArrayList<>();
		for(Instruction instruction : this.instructions){
			oldValue.add(instruction);
		}
		this.instructions = instructions;
		pcs.firePropertyChange("instructionChange", oldValue, this.instructions);
	}

	@Override
	public String toString() {
		return "Recipe\nname=" + name + "\nCategory=" + category
				+ "\ningredients\n" + ingredients
				+ ", instructions=" + instructions + "]";
	}
	
	public void addPropertyChangeListener(PropertyChangeListener pcl){
		pcs.addPropertyChangeListener(pcl);
	}
}
