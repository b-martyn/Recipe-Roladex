package recipe;

public class Instruction implements Comparable<Instruction>, RecipeComponent{
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
		return "Instruction " + stepNumber + ":" + message;
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
}