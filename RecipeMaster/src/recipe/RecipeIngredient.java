package recipe;

public class RecipeIngredient implements RecipeComponent, Cloneable{
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
	
	@Override
	public RecipeIngredient clone(){
		return new RecipeIngredient(ingredient, measurement);
	}
	
	@Override
	public String toString(){
		return measurement.toString() + " of " + ingredient.toString();
	}
}
