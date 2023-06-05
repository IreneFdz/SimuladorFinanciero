package ontology.conceptos;


public class Decision_crisis_sistemica extends Concepto {
    // decision = 1 -> -1
    // decision = 2 -> +1
	private int decision;
    	
	public Decision_crisis_sistemica() {}

	public int getDecision() {
		return decision;
	}
    public void setDecision(int decision) {
		this.decision = decision;
	}
}
