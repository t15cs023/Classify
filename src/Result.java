public class Result {
	int index;
	double probability;
	
	public Result(int _index, double _probability) {
		this.probability = _probability;
		this.index = _index;
	}
	
	public int getIndex() {
		return index;
	}
	
	public double getProbability() {
		return probability;
	}
}