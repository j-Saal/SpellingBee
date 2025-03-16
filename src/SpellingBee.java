import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, Jared Saal
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        makeWords("", letters);
    }

    public void makeWords(String s, String input) {
        if (!s.isEmpty()) {
            words.add(s);
        }

        for (int i = 0; i < input.length(); i++) {
            makeWords(s + input.charAt(i), input.substring(0, i) + input.substring(i + 1));
        }
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        mergeSort(words, 0, words.size() - 1);
    }

    public void mergeSort(ArrayList<String> words, int min, int max) {
        if (min >= max) {
            return;
        }

        int mid = min + (max - min) / 2;

        mergeSort(words, min, mid);
        mergeSort(words, mid + 1, max);
        merge(words, min, mid, max);
    }

    public void merge(ArrayList<String> words, int min, int mid, int max) {
        ArrayList<String> left = new ArrayList<>(words.subList(min, mid + 1));
        ArrayList<String> right = new ArrayList<>(words.subList(mid + 1, max + 1));

        int firstIndex = 0;
        int secondIndex = 0;
        int indexCounter = min;

        while (firstIndex < left.size() && secondIndex < right.size()) {
            if (left.get(firstIndex).compareTo(right.get(secondIndex)) <= 0) {
                words.set(indexCounter++, left.get(firstIndex++));
            } else {
                words.set(indexCounter++, right.get(secondIndex++));
            }
        }

        while (firstIndex < left.size()) {
            words.set(indexCounter++, left.get(firstIndex++));
        }

        while (secondIndex < right.size()) {
            words.set(indexCounter++, right.get(secondIndex++));
        }
    }
    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        for (int i = 0; i < words.size(); i++) {
            if (!binarySearch(DICTIONARY, words.get(i))) {
                words.remove(i);
                i--;
            }
        }
    }

    public boolean binarySearch(String[] dictionary, String word) {
        int min = 0;
        int max = dictionary.length - 1;

        while (min <= max) {
            int mid = min + ((max-min) / 2);
            if (word.equals(dictionary[mid])) {
                return true;
            }
            else if (dictionary[mid].compareTo(word) < 0) {
                min = mid + 1;
            }
            else {
                max = mid - 1;
            }
        }
        return false;
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
