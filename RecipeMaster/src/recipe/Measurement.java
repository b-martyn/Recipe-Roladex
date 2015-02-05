package recipe;

public class Measurement {
	
	private Measurement.Type type;
	private double amount;
	
	public Measurement(double amount, Measurement.Type type){
		this.type = type;
		this.amount = amount;
	}
	
	public enum Type{
		CUP, TABLESPOON, TEASPOON, DASH, OUNCES;
	}
	
	public Measurement.Type getType(){
		return type;
	}
	
	public double getAmount(){
		return amount;
	}

	@Override
	public String toString() {
		return "Measurement:" + amount + ":" + type;
	}
}
