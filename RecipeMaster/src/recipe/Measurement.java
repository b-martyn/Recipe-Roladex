package recipe;

public class Measurement {
	
	private String type;
	private double amount;
	
	public Measurement(double amount, String type){
		this.type = type;
		this.amount = amount;
	}
	
	public String getType(){
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
