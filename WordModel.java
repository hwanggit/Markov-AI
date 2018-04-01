/* File Header: WordModel.java is a file that utilizes the Markov Model in training, reading, and generating texts. The program is able to read a string of words, then based on Markov probabilities, predict the next  word that can possibly appear.
 * Luhao Wang cs8bwajw
 * luw055@ucsd.edu
 *
 * Class Header: WordModel class uses HashMap instance variables to store prefixes (words that come before the prediction, and the prediction itself, as well as the frequency of each prediction). Instance also includes the degree of the model (how many words to read before prediction, and a Random variable). The class contains methods which change the frequency, generate a new word, read/train the model.
 */

import java.util.HashMap;
import java.util.ArrayList;
import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class WordModel {

    protected HashMap<ArrayList<String>, HashMap<String, Integer>> predictionMap;
    protected HashMap<ArrayList<String>, ArrayList<String>> cache;
    protected int degree;
    protected Random random;
    
    // Constructor method initializes all instance variables and takes the degree as parameter
    // sets degree
    public WordModel (int degree) {
        this.degree=degree;
        this.predictionMap= new HashMap<ArrayList<String>, HashMap<String, Integer>>();
        this.cache= new HashMap<ArrayList<String>, ArrayList<String>>();
        this.random=new Random();
    }
    
    //increment method checks if prefix exists, if not add, else increment prediction if it exists
    // takes the prefix and prediciton to be checked
    public void incrementPrediction(ArrayList<String> prefix, String prediction){
        // variable to increment frequency
        int frequency=0;
        // if prefix is not in prediction Map
        if (!predictionMap.containsKey(prefix)) {
            HashMap<String,Integer> newValue = new HashMap<String,Integer>();
            predictionMap.put(prefix, newValue);
        }     
        // if prefix is in map, check if prediction is in value, if yes, increment, if not, add
        else {
            if (!predictionMap.get(prefix).containsKey(prediction)) {
                predictionMap.get(prefix).put(prediction,0);
            }
            else {
                frequency=predictionMap.get(prefix).get(prediction);
                frequency++;
                predictionMap.get(prefix).put(prediction,frequency);
            }
       }
    }

    // train from text method reads string of text and creates probabilities, sets frequencies
    // takes the string to be read
    // returns number of words in string
    public int trainFromText(String content) {
        // counter for number of words in string
        int counter=0;
        ArrayList<String> wordList= new ArrayList<String>();
        ArrayList<String> prefix= new ArrayList<String>();
        // scan String and convert every word to lower case
        Scanner sc= new Scanner(content);
        while (sc.hasNext()) {
            String next=sc.next().toLowerCase();
            // add word to wordlist and increment counter
            wordList.add(next);
            counter++;
        }
        // read [degree] number of words and set prediction as next word
        for (int k=this.degree;k<wordList.size();k++) 
        {
            for (int h=this.degree;h>0;h--) {
                prefix.add(wordList.get(k-h));
            }
            incrementPrediction(prefix,wordList.get(k));
            // clear current prefix every time
            prefix.clear();
        } 
        return counter;
    }

    // getFlattenedList method turns value HashMap into ArrayList by repeating every prediction based on 
    // frequency
    // takes the prefix to be checked
    public ArrayList<String> getFlattenedList(ArrayList<String> prefix){
        int frequency=0;
        // if cache does contain prefix, return the flattened list
        if (cache.containsKey(prefix)) {
            return cache.get(prefix);
        }
        // if cache does not contain prefix, then turn value of predictionMap into ArrayList containing word
        // and their repetitions (frequencies)
        ArrayList<String> flat= new ArrayList<String>();
        // for every string in prediction, add it to the flat list
        for (String keyBottom : predictionMap.get(prefix).keySet()) {
            frequency=predictionMap.get(prefix).get(keyBottom);
            for (int j=0;j<frequency;j++)
            {
                flat.add(keyBottom);
            }
        }
        // put flat list in cache
        cache.put(prefix,flat); 
        //return flat list
        return flat;
    }
    
    // generate Next method reads prefix and generates the next word based on frequency of prediction
    // takes prefix to be checked
    public String generateNext(ArrayList<String> prefix) {
        // generate random word, return
        ArrayList<String> getList= getFlattenedList(prefix);
        int randomWord=random.nextInt(getList.size()-1)+1;
        return getList.get(randomWord);
    }
    
    // generate method starts with empty string and adds a random prefix to start, then generates
    // random words to be added on to the string based on probabilities
    // Number of words to be generated can be specified in parameter
    public String generate(int count) {
        // create empty string
        String empty="";
        int words=0;
        int size=0;
        // create array list of all keys
        ArrayList<ArrayList<String>> keys = new ArrayList<ArrayList<String>>(predictionMap.keySet());
        ArrayList<String> text= new ArrayList<String>();
        ArrayList<String> currKey=new ArrayList<String>();
        // generate random key
        int randomPrefix=random.nextInt(keys.size());
        currKey=keys.get(randomPrefix);
        // get size of key
        size=currKey.size();
        // add key to text list
        for (int k=0;k<keys.get(randomPrefix).size();k++) {
            text.add(currKey.get(k));
            words++;
        }  
        // count words + added prefix
        int temp=words+count;
        // while there are not enough words generated, generate add new word
        while (words!=temp)
        {
            // generate new word based on current key and add
            String randomWord=generateNext(currKey);
            text.add(randomWord);
            words++;
            // clear current key
            currKey.clear();
            // create a new key based on current [degree] words
            for (int h=size;h>0;h--) {
                currKey.add(text.get(words-h));
            }
           
        }
        // turn text list into string and return
        for (int l=0;l<text.size();l++) {
            empty+=text.get(l)+" ";
        }
        return empty;
    }

    /** return reference to predictionMap */
    public HashMap<ArrayList<String>, HashMap<String, Integer>> getPredictionMap() {
        return this.predictionMap;
    }

    /* return reference to cache */
    public HashMap<ArrayList<String>, ArrayList<String>> getCache(){
        return this.cache;
    }

    /* return a reference to the random object */
    public Random getRandom(){
        return this.random;
    }

    /* return the degree */
    public int getDegree(){
        return this.degree;
    }


    /* provided for debug purposes,
       but really, provided so students can look at how to iterate HashMap */ 
    public void printMap(){

        /* for each key (prefix) in predictionMap, 
           print prefix, print all predictions with counts */
        for(ArrayList<String> prefix: predictionMap.keySet()){
            System.out.println("Prefix: " + prefix + ":");

            HashMap<String, Integer> freqMap = predictionMap.get(prefix);
            /* for each key (prediction) in the subsequen HashMap, 
               print prediction, print count */
            for (String prediction: freqMap.keySet()){
                System.out.printf("\tPrediction: \"%s\"   Count: %d\n", 
                    prediction, freqMap.get(prediction));           
            }
        }       
    }

}
