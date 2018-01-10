public class Word {
	/* tf*idf重み */
	private double tf_idf_value;
	/* 単語 */
	private String word;
	
	/**
	 * 初期化
	 * @param w 単語
	 * @param val tf*idf重み
	 */
	public Word(String w, double val) {
		this.tf_idf_value = val;
		this.word = w;
	}
	
	/**
	 * 重みを返す
	 * @return tf*idf重み
	 */
	public double return_val() {
		return tf_idf_value;
	}
	
	/**
	 * 単語を返す
	 * @return 単語
	 */
	public String return_word() {
		return word;
	}
}