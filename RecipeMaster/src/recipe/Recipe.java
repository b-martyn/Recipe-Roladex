package recipe;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;


public class Recipe implements Printable{
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
		StringBuilder sb = new StringBuilder(name + "\n" + category + "\n");
		for(RecipeIngredient ingredient : ingredients){
			sb.append(ingredient.toString() + "\n");
		}
		for(Instruction instruction : instructions){
			sb.append(instruction.toString() + "\n");
		}
		return sb.toString();
	}
	
	public void addPropertyChangeListener(PropertyChangeListener pcl){
		pcs.addPropertyChangeListener(pcl);
	}

	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageNumber) throws PrinterException {
		/* 
		 * User (0,0) is typically outside the imageable area, so we must
         * translate by the X and Y values in the PageFormat to avoid clipping
         */
        Graphics2D g2d = (Graphics2D)graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        
		RecipePrinter recipePrinter = new RecipePrinter(this, graphics, pageFormat);
		if(pageNumber > recipePrinter.getNumOfPages() - 1){//pageNumber 0 based, numOfPages is not
			return NO_SUCH_PAGE;
		}
		recipePrinter.renderPage(pageNumber);
		
		return PAGE_EXISTS;
	}
}
