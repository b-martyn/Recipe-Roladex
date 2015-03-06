package recipe;

import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

public class Instruction implements Comparable<Instruction>, RecipeComponent, Cloneable, Printable{
	private int stepNumber;
	private String message;
	
	public Instruction(){
	}
	
	public Instruction(int stepNumber, String message){
		this.stepNumber = stepNumber;
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStepNumber() {
		return stepNumber;
	}

	public void setStepNumber(int stepNumber) {
		this.stepNumber = stepNumber;
	}

	@Override
	public String toString() {
		return stepNumber + ": " + message;
	}

	@Override
	public int compareTo(Instruction compare) {
		if(compare.getStepNumber() > stepNumber){
			return -1;
		}else if(compare.getStepNumber() < stepNumber){
			return 1;
		}else{
			return 0;
		}
	}
	
	@Override
	public Instruction clone(){
		Instruction instruction = new Instruction();
		instruction.stepNumber = stepNumber;
		instruction.message = message;
		return instruction;
	}

	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
		//graphics.drawString(this.toString(), x, y);
		return PAGE_EXISTS;
	}
}