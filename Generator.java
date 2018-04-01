/** Junshen (Kevin) Chen 
 *  cs8bwhat
 *  File Generator.java
 *  Contains program that constructs, trains, and uses a markov model 
 */

import java.lang.Object;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

/** class Generator: contains a main method that parses command line arguments,
 *  trains a Word/CharModel and generate sentences based on user specifications 
 */
public class Generator {

    static final boolean debug = false; // set this to true to print the
                                        // Hashmaps before generating

    static final int DEFAULT_DEGREE = 2; // default degree 2
    static final int MIN_ARG = 2; 
    static final int ARG_PAIR = 2;
    static final int DEFAULT_TOKEN_COUNT = 100;

    /** main
     *  parses command line arguments, uses a MarkovModel to generate sentences
     *  @param args     an array of String arguments
     *  @exception FileNotFoundException    thrown when input file not found
     *  @exception IOException thrown when error reading from file 
     */
    public static void main(String[] args) 
        throws FileNotFoundException, IOException{

        if (args.length % ARG_PAIR != 0 || args.length < MIN_ARG)
            usage();

        String filename = args[0];
        boolean useWordModel = false;
        if (args[1].equals("w"))
        	useWordModel = true;
        else if (args[1].equals("c"))
        	useWordModel = false;
        else usage();

        int tokenCount = DEFAULT_TOKEN_COUNT;
        int degree = DEFAULT_DEGREE;

        // parse args
        for (int i=2; i<args.length; i+=ARG_PAIR){
            if (args[i].equals("-d")) 
                degree = Integer.parseInt(args[i+1]);
            else if (args[i].equals("-n"))
                tokenCount = Integer.parseInt(args[i+1]);        
            else 
                usage();
        }

        if (degree >= tokenCount) {
            System.out.println("degree must be strictly less than count.");
            usage();
        }

        System.out.print("Constructing a Markov ");
        System.out.print(useWordModel? "word" : "character");
        System.out.println(" model of degree: " + degree);
        System.out.println();

        // construct new model object
        WordModel model;
        if (useWordModel)
        	model = new WordModel(degree);
        else
        	model = new CharacterModel(degree);

        // get all text from the file, train sentence by sentence
        if (filename == null) usage();
        System.out.println("Training from data: " + filename);

        // read the entire file as a string
        File file = new File(filename);
		FileInputStream fis = new FileInputStream(file);
		byte[] data = new byte[(int) file.length()];
		fis.read(data);
		fis.close();
		String content = new String(data, "UTF-8");

        // wrap the training text around to avoid HashMap Key falloff 
        // when trying to predict prefix at the end of text
        if (useWordModel) {
            Scanner sc = new Scanner(content);
            for (int i=0; i<degree; i++) {
                content += (" " + sc.next());
                if (!sc.hasNext()) sc = new Scanner(content);
            }
        } else {
            for (int i=0; i<degree; i++) {
                content += content.charAt(i);
            }
        }

		// `train the model
		int tokensRead = model.trainFromText(content);
        
        System.out.println("Training done, " + tokensRead + " read.");
        System.out.println();

        // print hashmaps before printing sentences
        if (debug) {
            System.out.println("Debug selected, printing HashMaps...");
            model.printMap();
            System.out.println();
        }

        // generate sentences
        System.out.println("Generating text...\n");
		System.out.println(model.generate(tokenCount));
    }

    /** usages
     *  prints out the usage message, exit program
     */
    static void usage(){
        System.out.println("Generator");
        System.out.println("Usage: java Generator filename w|c [-d degree] [-n count]");
        System.out.println("   filename    ->  Required. Specify the training data file");
        System.out.println("     w|c       ->  Either w (word) or c (character) required. Specify which model to use");
        System.out.println("  [-d degree]  ->  Specify the degree of the Markov Model. Default at 2, must < count");
        System.out.println("  [-n count]   ->  Specify how many words or chars to generate. Default at 100");
        System.exit(1);
    }

} // end of Generator.java