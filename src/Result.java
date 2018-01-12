public class Result {
	/* 検索文書の番号 */
	int index;
	/* 検索文書と訓練データの類似度 */
	double probability;
	
	/**
	 * 初期化
	 * @param _index				検索文書の番号
	 * @param _probability		検索文書と訓練データの類似度
	 */
	public Result(int _index, double _probability) {
		this.probability = _probability;
		this.index = _index;
	}
	
	/**
	 * 検索文書の番号を返す
	 * @return 検索文書の番号
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * 検索文書と訓練データの類似度を返す
	 * @return 検索文書と訓練データの類似度
	 */
	public double getProbability() {
		return probability;
	}
}