import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Classify {

	/**
	 * 主な処理
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<ArrayList<Word>> list = tfidfrun();
		ArrayList<ArrayList<Word>> queryList = queryrun();
		ArrayList<ArrayList<Result>> searchResultCos = new ArrayList<>();
		ArrayList<ArrayList<Result>> searchResultCosWithoutTFIDF = new ArrayList<>();
		ArrayList<ArrayList<Result>> searchResultJac = new ArrayList<>();
		ArrayList<ArrayList<Result>> searchResultJacWithoutTFIDF = new ArrayList<>();
		ArrayList<ArrayList<Result>> searchResultDice = new ArrayList<>();
		ArrayList<ArrayList<Result>> searchResultSimpson = new ArrayList<>();
		
		System.out.println("Cosine");
		//Create cosineMeasurement result list
		for(int i = 0; i < queryList.size(); i++) {
			double[] listResult = new double[list.size()];
			for(int j = 0; j < list.size(); j++) {
				listResult[j] = cosineCalc(list.get(j),queryList.get(i));
			}
			ArrayList<Result> result = findLargests(listResult, list.size());
			searchResultCos.add(result);
		}
		
		System.out.println();
		System.out.println("Cosine without tfidf");
		//Create cosineMeasurement result list
		for(int i = 0; i < queryList.size(); i++) {
			double[] listResult = new double[list.size()];
			for(int j = 0; j < list.size(); j++) {
				listResult[j] = cosineCalcWithoutTFIDF(list.get(j),queryList.get(i));
			}
			ArrayList<Result> result = findLargests(listResult, list.size());
			searchResultCosWithoutTFIDF.add(result);
		}
		
		System.out.println();
		System.out.println("Jaccard");
		//Create jaccardMeasurement result list
		for(int i = 0; i < queryList.size(); i++) {
			double[] listResult = new double[list.size()];
			for(int j = 0; j < list.size(); j++) {
				listResult[j] = jaccardCalc(list.get(j),queryList.get(i));
			}
			ArrayList<Result> result = findLargests(listResult, list.size());
			searchResultJac.add(result);
		}
		
		System.out.println();
		System.out.println("Jaccard");
		//Create jaccardMeasurement result list
		for(int i = 0; i < queryList.size(); i++) {
			double[] listResult = new double[list.size()];
			for(int j = 0; j < list.size(); j++) {
				listResult[j] = jaccardCalcWithoutTFIDF(list.get(j),queryList.get(i));
			}
			ArrayList<Result> result = findLargests(listResult, list.size());
			searchResultJacWithoutTFIDF.add(result);
		}
		
		System.out.println();
		System.out.println("Dice");
		//Create jaccardMeasurement result list
		for(int i = 0; i < queryList.size(); i++) {
			double[] listResult = new double[list.size()];
			for(int j = 0; j < list.size(); j++) {
				listResult[j] = diceCalc(list.get(j),queryList.get(i));
			}
			ArrayList<Result> result = findLargests(listResult, list.size());
			searchResultDice.add(result);
		}
		
		System.out.println();
		System.out.println("Simpson");
		//Create jaccardMeasurement result list
		for(int i = 0; i < queryList.size(); i++) {
			double[] listResult = new double[list.size()];
			for(int j = 0; j < list.size(); j++) {
				listResult[j] = simpsonCalc(list.get(j),queryList.get(i));
			}
			ArrayList<Result> result = findLargests(listResult, list.size());
			searchResultSimpson.add(result);
		}
		
		System.out.println("Printing cosine measurement results...");
		printResult(searchResultCos);
		System.out.println("Printing cosinewttfidf measurement results...");
		printResult(searchResultCosWithoutTFIDF);
		System.out.println("Printing jaccard measurement results...");
		printResult(searchResultJac);			
		System.out.println("Printing jaccardwttfidf measurement results...");
		printResult(searchResultJacWithoutTFIDF);		
		System.out.println("Printing dice measurement results...");
		printResult(searchResultDice);
		System.out.println("Printing simpson measurement results...");
		printResult(searchResultSimpson);
	}

	private static void printResult(ArrayList<ArrayList<Result>> searchResult) {
		String answers[] = {"国際", "経済", "家庭", "科学", "芸能", "スポーツ", "芸能", "経済", "国際", "スポーツ"};
		for(int i = 0; i < searchResult.size(); i++) {
			System.out.println("Result for " + "query " + (i+1));
			for(int j = 0; j < searchResult.get(i).size(); j++) {
				Result result = searchResult.get(i).get(j);
				String genre[] = {"国際", "経済", "家庭", "科学", "芸能", "スポーツ", "文化", "読書"};
				System.out.print(genre[result.getIndex()] + ", " + result.getProbability() + " ");
				if(genre[result.getIndex()].equals(answers[i]))
					System.out.print("O");
				else
					System.out.print("X");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	private static ArrayList<Result> findLargests(double[] listResult, int size) {
		ArrayList<Result> result = new ArrayList<>();
		double max = -10000;
		for(int i = 0; i < size; i++) {
			if(listResult[i] > max)
				max = listResult[i];
		}
		for(int i = 0; i < size; i++) {
			if(listResult[i] == max)
				result.add(new Result(i, listResult[i]));
		}
		return result;
	}
	
	private static double cosineCalc(ArrayList<Word> subWordList, ArrayList<Word> subQueryList) {
		double x2 = 0.0;
		for(int i = 0; i < subWordList.size(); i++) {
			double x = subWordList.get(i).return_val();
			x2 += x*x;
		}
		double y2 = 0.0;
		for(int i = 0; i < subQueryList.size(); i++) {
			double y=subQueryList.get(i).return_val();
			y2 += y*y;
		}
		double inner_product = 0.0;
		for(int i = 0; i < subQueryList.size(); i++) {
			String currentQSubWord = subQueryList.get(i).return_word();
			double currentQSubVal = subQueryList.get(i).return_val();
			for(int j = 0; j < subWordList.size(); j++) {
				String currentLSubWord = subWordList.get(j).return_word();
				double currentLSubVal = subWordList.get(j).return_val();
				if(currentQSubWord.equals(currentLSubWord)) {
					inner_product += currentQSubVal * currentLSubVal; 
				}
			}
		}
		double down = Math.sqrt(x2*y2);
		double result = inner_product/down;
		System.out.println("x2:" + x2 + " y2:" + y2 + " inner_product:" + inner_product);
		System.out.println(result);
		return result;
	}

	private static double cosineCalcWithoutTFIDF(ArrayList<Word> subWordList, ArrayList<Word> subQueryList) {
		double x2 = 0.0;
		for(int i = 0; i < subWordList.size(); i++) {
			x2 += 1;
		}
		double y2 = 0.0;
		for(int i = 0; i < subQueryList.size(); i++) {
			y2 += 1;
		}
		double inner_product = 0.0;
		for(int i = 0; i < subQueryList.size(); i++) {
			String currentQSubWord = subQueryList.get(i).return_word();
			for(int j = 0; j < subWordList.size(); j++) {
				String currentLSubWord = subWordList.get(j).return_word();
				if(currentQSubWord.equals(currentLSubWord)) {
					inner_product += 1; 
				}
			}
		}
		double down = Math.sqrt(x2*y2);
		double result = inner_product/down;
		System.out.println("x2:" + x2 + " y2:" + y2 + " inner_product:" + inner_product);
		System.out.println(result);
		return result;
	}
	
	private static double jaccardCalc(ArrayList<Word> subWordList, ArrayList<Word> subQueryList) {
		double x2 = 0.0;
		for(int i = 0; i < subWordList.size(); i++) {
			double x = subWordList.get(i).return_val();
			x2 += x*x;
		}
		double y2 = 0.0;
		for(int i = 0; i < subQueryList.size(); i++) {
			double y=subQueryList.get(i).return_val();
			y2 += y*y;
		}
		double inner_product = 0.0;
		for(int i = 0; i < subQueryList.size(); i++) {
			String currentQSubWord = subQueryList.get(i).return_word();
			double currentQSubVal = subQueryList.get(i).return_val();
			for(int j = 0; j < subWordList.size(); j++) {
				String currentLSubWord = subWordList.get(j).return_word();
				double currentLSubVal = subWordList.get(j).return_val();
				if(currentQSubWord.equals(currentLSubWord)) {
					inner_product += currentQSubVal * currentLSubVal; 
				}
			}
		}
		double up = inner_product;
		double down = x2+y2-inner_product;
		double result = up/down;
		System.out.println("x2:" + x2 + " y2:" + y2 + " inner_product:" + inner_product);
		System.out.println(result);
		return result;
	}
	
	private static double jaccardCalcWithoutTFIDF(ArrayList<Word> subWordList, ArrayList<Word> subQueryList) {
		double x2 = subWordList.size();
		double y2 = subQueryList.size();
		double inner_product = 0.0;
		for(int i = 0; i < subQueryList.size(); i++) {
			String currentQSubWord = subQueryList.get(i).return_word();
			double currentQSubVal = subQueryList.get(i).return_val();
			for(int j = 0; j < subWordList.size(); j++) {
				String currentLSubWord = subWordList.get(j).return_word();
				double currentLSubVal = subWordList.get(j).return_val();
				if(currentQSubWord.equals(currentLSubWord)) {
					inner_product += currentQSubVal * currentLSubVal; 
				}
			}
		}
		double up = inner_product;
		double down = x2+y2-inner_product;
		double result = up/down;
		System.out.println("x2:" + x2 + " y2:" + y2 + " inner_product:" + inner_product);
		System.out.println(result);
		return result;
	}
	
	private static double diceCalc(ArrayList<Word> subWordList, ArrayList<Word> subQueryList) {
		double x2 = 0.0;
		for(int i = 0; i < subWordList.size(); i++) {
			double x = subWordList.get(i).return_val();
			x2 += x*x;
		}
		double y2 = 0.0;
		for(int i = 0; i < subQueryList.size(); i++) {
			double y=subQueryList.get(i).return_val();
			y2 += y*y;
		}
		double inner_product = 0.0;
		for(int i = 0; i < subQueryList.size(); i++) {
			String currentQSubWord = subQueryList.get(i).return_word();
			double currentQSubVal = subQueryList.get(i).return_val();
			for(int j = 0; j < subWordList.size(); j++) {
				String currentLSubWord = subWordList.get(j).return_word();
				double currentLSubVal = subWordList.get(j).return_val();
				if(currentQSubWord.equals(currentLSubWord)) {
					inner_product += currentQSubVal * currentLSubVal; 
				}
			}
		}
		double up = 2*inner_product;
		double down = x2+y2;
		double result = up/down;
		System.out.println("x2:" + x2 + " y2:" + y2 + " inner_product:" + inner_product);
		System.out.println(result);
		return result;
	}
	
	private static double simpsonCalc(ArrayList<Word> subWordList, ArrayList<Word> subQueryList) {
		double inner_product = 0.0;
		for(int i = 0; i < subQueryList.size(); i++) {
			String currentQSubWord = subQueryList.get(i).return_word();
			double currentQSubVal = subQueryList.get(i).return_val();
			for(int j = 0; j < subWordList.size(); j++) {
				String currentLSubWord = subWordList.get(j).return_word();
				double currentLSubVal = subWordList.get(j).return_val();
				if(currentQSubWord.equals(currentLSubWord)) {
					inner_product += currentQSubVal * currentLSubVal; 
				}
			}
		}
		double up = inner_product;
		double down = 0;
		if(subQueryList.size() < subWordList.size())
			down = subQueryList.size();
		else
			down = subWordList.size();
		double result = up/down;
		System.out.println("inner_product:" + inner_product + " down:" + down);
		System.out.println(result);
		return result;
	}
	
	/**
	 * 訓練データの各ジャンルでTFIDFをもとめ、
	 * ジャンル別に単語とTFIDF値からなるWord型のリストをまとめるリストを返り値とする
	 * @return ジャンル別に単語とTFIDF値からなるWord型のリストをまとめるリスト
	 */
	public static ArrayList<ArrayList<Word>> tfidfrun() {
		/* ここからtrain.freqのファイル読み込み処理 */
		ArrayList<ArrayList<String>> temp = new ArrayList<>();
		String fileName = "train.freq";
		String train[][];
		int train_height, train_width;
		try(BufferedReader b = new BufferedReader(new FileReader(fileName)))
		{
			String currentLine;
			while((currentLine = b.readLine()) != null) {
				if(currentLine.isEmpty())
					continue;


				ArrayList<String> rows = new ArrayList<>();
				String[] line = currentLine.trim().split(" ");
				for(String string:line) {
					if(!string.isEmpty()) {
						rows.add(string);
					}
				}
				temp.add(rows);
			}

		}
		catch(IOException e) {
			;
		}
		train_height = temp.size();
		train_width = temp.get(0).size();
		train = new String[train_height][train_width];
		for(int y = 0; y < train_height; y++) {
			for(int x = 0; x < train_width; x++) {
				train[y][x] = temp.get(y).get(x);
			}
		} 
		/* ここまでtrain.freqのファイル読み込み処理 */

		/* 読み込み確認用 */
		/*
		for(int y = 0; y < train_height; y++) {
			for(int x = 0; x < train_width; x++) {
				System.out.print(train[y][x] + " ");
			}
			System.out.println();
		}
		 */

		String trainwdf[][] = new String[train_height][train_width+1];
		for(int y = 0; y < train_height; y++) {
			for(int x = 0; x < train_width; x++) {
				trainwdf[y][x] = train[y][x];
			}
			trainwdf[y][train_width] = "0";
		}

		/* dfを計算し、4番目の要素に保存する */
		for(int y = 0; y < train_height; y++) {
			for(int ysub = 0; ysub < train_height; ysub++) {
				if(trainwdf[y][1].equals(trainwdf[ysub][1])) {
					int count = Integer.parseInt(trainwdf[y][3]);
					count++;
					trainwdf[y][3] = Integer.toString(count);
				}
			}
		}

		/* tfidfが5番目の要素となる配列を生成し、計算する */
		String trainwtfidf[][] = new String[train_height][train_width+2];
		for(int y = 0; y < train_height; y++) {
			for(int x = 0; x < train_width+1; x++) {
				trainwtfidf[y][x] = trainwdf[y][x];
			}
			trainwtfidf[y][train_width+1] = Double.toString(Double.parseDouble(trainwtfidf[y][2]) * (log2(10.0/Double.parseDouble(trainwtfidf[y][3])) +1));
		}

		/* 最終的なリスト確認用 */
		/*
		for(int y = 0; y < train_height; y++) {
			for(int x = 0; x < 5; x++) {
				System.out.print(trainwtfidf[y][x] + " ");
			}
			System.out.println();
		}
		 */

		/* 管理を簡単にするために、ArrayListに保存する */
		ArrayList<ArrayList<Word>> l = new ArrayList<>();
		ArrayList<Word> w = new ArrayList<>();
		for(int i = 0; i < 8; i++) {
			w = new ArrayList<>();
			//ArrayListはジャンルを保持できないため、0:国際, 1:経済, ..., 7:読書 のようにジャンルを数値化する
			String genre[] = {"国際", "経済", "家庭", "科学", "芸能", "スポーツ", "文化", "読書"};
			for(int y = 0; y < train_height; y++) {
				if(trainwtfidf[y][0].equals(genre[i])) {
					w.add(new Word(trainwtfidf[y][1], Double.parseDouble(trainwtfidf[y][4])));
				}
			}
			l.add(w);
		}

		/* リスト確認用 */
		for(int i = 0; i < l.size(); i++) {
			String genre[] = {"国際", "経済", "家庭", "科学", "芸能", "スポーツ", "文化", "読書"};
			System.out.println();
			System.out.println(genre[i]);
			for(int j = 0; j < l.get(i).size(); j++) {
				Word currentw = l.get(i).get(j);
				System.out.println(currentw.return_word() + " " + currentw.return_val());
			}
		}

		return l;
	}
	
	/**
	 * テストデータの読み込みを行い、
	 * 1つの文書に属する単語とその出現回数を1つのリストに保存し、全文書を扱うリストにそのリストを保存する。
	 * 返り値が全文書のリストを扱うリストである。
	 * @return 全文書のリストを扱うリスト
	 */
	public static ArrayList<ArrayList<Word>> queryrun() {
		ArrayList<ArrayList<Word>> list = new ArrayList<>();
		/* ここからquery.freqのファイル読み込み処理 */
		ArrayList<ArrayList<String>> query = new ArrayList<>();
		String fileName = "test.freq";
		try(BufferedReader b = new BufferedReader(new FileReader(fileName)))
		{
			String currentLine;
			while((currentLine = b.readLine()) != null) {
				if(currentLine.isEmpty())
					continue;


				ArrayList<String> rows = new ArrayList<>();
				String[] line = currentLine.trim().split(" ");
				for(String string:line) {
					if(!string.isEmpty()) {
						rows.add(string);
					}
				}
				query.add(rows);
			}

		}
		catch(IOException e) {
			;
		}

		/* 確認用 */
		/*
		for(int i = 0; i < query.size(); i++) {
			for(int j = 0; j < query.get(i).size(); j++) {
				System.out.print(query.get(i).get(j) + " ");
			}
			System.out.println();
		}
		 */
		/* ここまでquery.freqのファイル読み込み処理 */
		for(int i = 1; i <= 10; i++) {
			ArrayList<Word> w = new ArrayList<>();
			for(int j = 0; j < query.size(); j++) {
				if(query.get(j).get(0).equals(Integer.toString(i))) {
					Word currentWord = new Word(query.get(j).get(1), Double.parseDouble(query.get(j).get(2)));
					w.add(currentWord);
				}
			}
			list.add(w);
		}
		for(int i= 0; i < list.size(); i++) {
			for(int j = 0; j < list.get(i).size(); j++) {
				Word w = list.get(i).get(j);
				System.out.println(i + " "+ w.return_word() + " " + w.return_val());
			}
		}
		return list;
	}

	/**
	 * ベース2のログを処理するメソッド
	 * @param d 入力数値
	 * @return log2で計算した結果
	 */
	private static double log2(double d) {
		return (Math.log(d)/Math.log(2));
	}
}
