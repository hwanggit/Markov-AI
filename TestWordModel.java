import java.util.HashMap;
import java.util.ArrayList;

public class TestWordModel {

	public static void main(String[] args){
		testIncrementPrediction();
		testGetFlattenedList();
	}


	static void testIncrementPrediction(){
		System.out.println("\nTesting incrementPrediction(), using \"the annoying doge\"...");
		try {
			// prefix [the, annoying]
			ArrayList<String> prefix = new ArrayList<String>();
			prefix.add("the");
			prefix.add("annoying");

			// prediction "doge"
			final String prediction = "doge";

			// try increment prediction
			WordModel model = new WordModel(2);
			model.incrementPrediction(prefix, "doge");

			// verify key, should be [the annoying]
			HashMap<ArrayList<String>, HashMap<String, Integer>> predictionMap = model.getPredictionMap();
			if (!predictionMap.containsKey(prefix)) {
				System.out.println("prefix \"the annoying\" not found.");
				return;
			}
			// verify value, should be doge
			HashMap<String, Integer> freqMap = predictionMap.get(prefix);
			if (!freqMap.containsKey(prediction)) {
				System.out.println("prediction \"doge\" not found.");
				return;
			}
			// verify freq, should be 1
			int frequency = freqMap.get(prediction);
			if (frequency != 1) {
				System.out.println("frequency " + frequency + " is incorrect. Should be 1.");
				return;
			}
		} catch (Exception e) {
			System.out.println("Exception occurred when testing incrementPrediction().");
			e.printStackTrace();
			return;
		}
		System.out.println("PASSED.");
		return;
	}



	static void testGetFlattenedList(){
		System.out.println("\nTesting getFlattenedList()...");
		try {
			WordModel model = new WordModel(1);

			// prefix [the]
			ArrayList<String> prefix = new ArrayList<String>();
			prefix.add("the");

			// prediction annoying:3 doge:2
			HashMap<String, Integer> freqMap = new HashMap<String, Integer>();
			freqMap.put("annoying", 3);
			freqMap.put("doge", 2);
			
			// put the Key and the Value
			HashMap<ArrayList<String>, HashMap<String, Integer>> predictionMap = model.getPredictionMap();
			predictionMap.put(prefix, freqMap);

			// get a flattened list
			ArrayList<String> flattenedList = model.getFlattenedList(prefix);

			// verify there are exactly 3 annoying and 2 doge in the list, order does not matter
			int annoyingCount = 0;
			int dogeCount = 0;
			for (int i=0; i<flattenedList.size(); i++){
				if (flattenedList.get(i).equals("annoying")) annoyingCount += 1;
				else if (flattenedList.get(i).equals("doge")) dogeCount += 1;
				else {
					System.out.println("unexpected token: " + flattenedList.get(i));
					return;
				}
			}
			if (annoyingCount!=3 || dogeCount!=2) {
				System.out.println("flattened list incorrect");
				return;
			}

		} catch (Exception e) {
			System.out.println("Exception occurred when testing getFlattenedList().");
			e.printStackTrace();
			return;
		}
		System.out.println("PASSED.");
		return;
	}


}