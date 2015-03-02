package recipe;

import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.Collection;

public class RecipePrinter{
	
	public static void main(String...args){
		Recipe recipe = new Recipe();
		recipe.setName("Test Recipe");
		recipe.setCategory("Test Category");
		Collection<RecipeIngredient> ingredients = new ArrayList<>();
		Collection<Instruction> instructions = new ArrayList<>();
		for(int i = 0; i < 2; i++){
			ingredients.add(new RecipeIngredient(new Ingredient("Ingredient " + i), new Measurement(1.0, "cup")));
			instructions.add(new Instruction(i, "This is instruction number " + i));
		}
		recipe.setIngredients(ingredients);
		recipe.setInstructions(instructions);
		System.out.println(recipe);
	}
	
	/*
	private Recipe recipe;
	
	public void print(Recipe...recipes){
		for(Recipe recipe : recipes){
			this.recipe = recipe;
			PrinterJob job = PrinterJob.getPrinterJob();
			job.setPrintable(this);
			boolean doPrint = job.printDialog();
			if(doPrint){
				//try {
					System.out.println("Printing");
					//job.print();
				//} catch (PrinterException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				//}
			}
		}
	}

	@Override
	public int print(Graphics arg0, PageFormat arg1, int arg2) throws PrinterException {
		// TODO Number Of Pages
			/*
			 * Size of Ingredient
			 * 	-Max size for first page and max size for subsequent pages
			 * Size of Instructions
			 * 	-Max size for first page and max size for subsequent pages
			 * -Compare sizes for run off
			 */
		// TODO Visual of each page
/*		return PAGE_EXISTS;
	}
	*/
}
