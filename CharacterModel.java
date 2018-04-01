/* File Header: CharacterModel.java uses Markov Model to train, read, and generate new characters based on data, it functions the same as WordModel.java except it reads characters.
 * Luhao Wang cs8bwajw
 * luw055@ucsd.edu
 * 03/04/18
 *
 * Class Header: CharacterModel extends and uses all instances and methods from WordModel such as degree, which can be set with a construction calling super. This class modifies WordModel such that the markov Model works not just for words but for characters as well.
 */
	
import java.util.HashMap;
import java.util.ArrayList;
import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class CharacterModel extends WordModel {

    public CharacterModel (int degree) {
        super(degree);
    }

    // train from text method reads string of text and creates probabilities, sets frequencies
    // takes the string to be read
    // returns number of characters in string
    public int trainFromText(String content) {
        // counter for number of characters in string
        int counter=0;
        ArrayList<String> wordList= new ArrayList<String>();
        ArrayList<String> prefix= new ArrayList<String>();
        // scan String and convert every character to lower case
        Scanner sc= new Scanner(content).useDelimiter("");
        while (sc.hasNext()) {
            String next=sc.next().toLowerCase();
            // add character to wordlist and increment counter
            wordList.add(next);
            counter++;
        }
        // read [degree] number of character and set prediction as next character
        for (int k=this.degree;k<wordList.size();k++) 
        {
            for (int h=this.degree;h>0;h--) {
                prefix.add(wordList.get(k-h));
            }
            incrementPrediction(prefix,wordList.get(k));
            // clear current prefix every time
            prefix.clear();
        } 
        // return number of characters in string
        return counter;
    }


    // generate method starts with empty string and adds a random prefix to start, then generates
    // random characters to be added on to the string based on probabilities
    // Number of characters to be generated can be specified in parameter
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
        // count characters + added prefix
        int temp=words+count;
        // while there are not enough characters generated, generate add new word
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
            empty+=text.get(l);
        }
        return empty;
    }
}
