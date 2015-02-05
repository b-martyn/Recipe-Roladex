package recipe;

public class RecipeIngredient implements RecipeComponent{
	private Ingredient ingredient;
	private Measurement measurement;
	
	public RecipeIngredient(Ingredient ingredient, Measurement measurement){
		this.setIngredient(ingredient);
		this.setMeasurement(measurement);
	}

	public Ingredient getIngredient() {
		return ingredient;
	}

	public void setIngredient(Ingredient ingredient) {
		this.ingredient = ingredient;
	}

	public Measurement getMeasurement() {
		return measurement;
	}

	public void setMeasurement(Measurement measurement) {
		this.measurement = measurement;
	}
}
