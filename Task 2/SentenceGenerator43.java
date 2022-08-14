import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.lang.Math;

public class SentenceGenerator43{
  public static void main(String[] args){
    System.out.println("Welcome to Sentence Generator\n\n");
    //starts the program by letting users choose options
    while(chooseGeneratorType());
  }
  //lets user a type of generator (rsg, ssg, osg)
  static boolean chooseGeneratorType() {
    System.out.println("\n\n1. Random Sentence Generator \n2. Sorted Sentence Generator \n3. Ordered Sentence Generator \n4. Close Program");
    System.out.println("\nChoose a Sentence Generator Type:");

    Scanner scanner = new Scanner(System.in);
    int option = scanner.nextInt();

    SentenceGenerator sentenceGenerator = new SentenceGenerator();
    //user chooses RSG
    if(option == 1){
      sentenceGenerator.setGeneratorStrategy(new RandomSentenceGenerator());
    }
    //user chooses SSG
    else if(option == 2){
      sentenceGenerator.setGeneratorStrategy(new SortedSentenceGenerator());
    }
    //user chooses OSG
    else if(option == 3){
      sentenceGenerator.setGeneratorStrategy(new OrderedSentenceGenerator());
    }
    //user chooses exit
    else if(option == 4){
      System.out.println("\n\nThanks for Using this Program :D \n\n");
      return false;
    }
    //user chooses something invalid
    else{
      System.out.println("Invalid Option\n");
      return true;
    }
    //after choosing generator type, lets user continously choose to add
    while(chooseOperationAfterChoosingGenerator(sentenceGenerator));
    return true;
  }

  //let user choose operation (add words / generate sentence / close)
  //true: chosen an option between add or generate, false: back home
  static boolean chooseOperationAfterChoosingGenerator(SentenceGenerator sentenceGenerator){
    System.out.printf("\n\n1. Add words to the WordList \n2. Generate Sentence \n3. Back Home");
    System.out.println("\nChoose an Option:");
    Scanner scanner = new Scanner(System.in);
    int option = scanner.nextInt();
    
    //user chooses to add words
    if (option == 1) {
      System.out.println("Keep entering words, enter fullstop '.' to stop.\n");
      while(true){
        scanner.nextLine();
        String word = scanner.nextLine();
        //user enters "."
        if(word.startsWith(".")){
          System.out.println("Word Entry Halted\n");
          //word entry stops
          break;
        }
        //program starts to execute the word adding process
        sentenceGenerator.addToWordList(word);
      }
    }
    //user chooses to generate sentence
    else if (option == 2) {
      String sentence = sentenceGenerator.generateSentence();
      System.out.println("\n\"" + sentence + "\"\n");
    }
    //user chooses to close prompt to go back to choosing generator type
    else if(option == 3){
      return false;
    }
    //user chooses something else, invalid option
    else{
      System.out.println("Invalid Option.\n");
      return true;
    }
    return true;
  }
}

class SentenceGenerator{
  //no specific reason to be 16, feelt like anything longer would it too long of a sentence for a terminal
  private final static int MAX_WORD_COUNT = 16;
  private GeneratorStrategy generatorStrategy;
  public void setGeneratorStrategy(GeneratorStrategy generatorStrategy){
    this.generatorStrategy = generatorStrategy;
  }
  //generates the actual sentence, called when user chooses option 2 in a generator
  public String generateSentence(){
    ArrayList<String> words = generatorStrategy.getWordsForTheSentence(MAX_WORD_COUNT);
    String generatedSentence = generatorStrategy.stringCat(words);
    if(words == null || generatedSentence == null)
      return "";
    return generatedSentence;
  }
  //adds word to the wordlist / data dictionary, called when user chooses option 1 in a generator
  public void addToWordList(String word){
    String formattedWord = generatorStrategy.formatString(word);
    generatorStrategy.addWordToList(formattedWord);
  }
}

//the abstract strategy that other concrete stategies will implement from
interface GeneratorStrategy{
  //concatenate the words into a sentence 
  String stringCat(ArrayList<String> words);
  //formats the words accordingly before adding to list
  //case conversion, reversal etc
  String formatString(String word);
  //prepares the word to be created as a sentence
  ArrayList<String> getWordsForTheSentence(int maxWordCount);
  //adds formatted words to the list
  void addWordToList(String formattedWord);
}

class RandomSentenceGenerator implements GeneratorStrategy{
  private static ArrayList<String> wordList = new ArrayList<>();
  
  //prepares the word to be created as a sentence
  @Override
  public ArrayList<String> getWordsForTheSentence(int maxWordCount){
    ArrayList<String> wordList = getWordList();
    try {
      int currentWordLimit = Math.min(maxWordCount, wordList.size());
      int wordCount = (int) ( ((Math.random()*100) % currentWordLimit) + 1);
      ArrayList<String> words = new ArrayList<>(wordCount);
      for (int i = 0; i < wordCount; i++) {
        int randomIndex = (int)((Math.random()*10000)%wordList.size());
        words.add(wordList.get(randomIndex));
      }
      return words;
    }
    catch (IndexOutOfBoundsException exception){
      System.out.println("RSG Error! " + exception.getLocalizedMessage());
      return null;
    }
  }

  //concatenate the words into a sentence 
  @Override
  public String stringCat(ArrayList<String> words) {
    try {
      String sentence = words.get(0);
      for (int i = 1; i < words.size(); i++) {
        sentence = sentence.concat(" " + words.get(i));
      }
      return sentence;
    }
    catch (IndexOutOfBoundsException exception){
      System.out.println("RSG Error, Word List is empty! ");
      return null;
    }
  }

  //formats the words accordingly before adding to list
  //lower case conversion in this case 
  @Override
  public String formatString(String word) {
    return word.toLowerCase();
  }

  //adds formatted words to the list
  @Override
  public void addWordToList(String formattedWord) {
    wordList = getWordList();
    wordList.add(formattedWord);
  }

  public static ArrayList<String> getWordList() {
    return wordList;
  }
}


class SortedSentenceGenerator implements GeneratorStrategy{
  private static ArrayList<String> wordList = new ArrayList<>();
  
  //prepares the word to be created as a sentence
  @Override
  public ArrayList<String> getWordsForTheSentence(int maxWordCount) {
    ArrayList<String> wordList = getWordList();
    try {
      int currentWordLimit = Math.min(maxWordCount, wordList.size());
      int wordCount = (int) ( ((Math.random()*100) % currentWordLimit) + 1);

      ArrayList<String> words = new ArrayList<>(wordCount);

      for (int i = 0; i < wordCount; i++) {
        int randomIndex = (int) ((Math.random() * 100000000) % wordList.size());
        words.add(wordList.get(randomIndex));
      }
      Collections.sort(words);
      return words;
    }
    catch (IndexOutOfBoundsException exception){
      System.out.println("SSG Error" + exception.getLocalizedMessage());
      return null;
    }
  }

  //concatenate the words into a sentence 
  @Override
  public String stringCat(ArrayList<String> words) {
    try {
      String sentence = words.get(0);
      for (int i = 1; i < words.size(); i++) {
        sentence = sentence.concat(" " + words.get(i));
      }
     return sentence;
    }
    catch (IndexOutOfBoundsException exception){
      System.out.println("SSG Error, Word List is empty!");
      return null;
    }
  }

  //formats the words accordingly before adding to list
  //lower case conversion in this case
  @Override
  public String formatString(String word) {
    return word.toLowerCase();
  }

  //adds formatted words to the list
  @Override
  public void addWordToList(String formattedWord) {
    wordList = getWordList();
    wordList.add(formattedWord);
  }

  public static ArrayList<String> getWordList() {
    return wordList;
  }
}


class OrderedSentenceGenerator implements GeneratorStrategy{
  private static ArrayList<String> wordList = new ArrayList<>();
  
  //prepares the word to be created as a sentence
  @Override
  public ArrayList<String> getWordsForTheSentence(int maxWordCount) {
    ArrayList<String> wordList = getWordList();
    try {
      int currentWordLimit = Math.min(maxWordCount, wordList.size());
      int wordCount = (int) (((Math.random()*100) % currentWordLimit) + 1);
      wordCount = Math.min(wordCount, wordList.size());
      ArrayList<Integer> vocIndices = new ArrayList<>(wordList.size());
      ArrayList<Integer> wordIndices = new ArrayList<>(wordCount);
      for(int i=0; i<wordList.size(); i++)
        vocIndices.add(i);
      Collections.shuffle(vocIndices);
      for(int i=0; i<wordCount; i++){
        wordIndices.add(vocIndices.get(i));
      }
      Collections.sort(wordIndices);
      ArrayList<String> words = new ArrayList<>(wordCount);
      for(int i=0; i<wordCount; i++){
        words.add(wordList.get(wordIndices.get(i)));
      }
      return words;
    }
    catch (IndexOutOfBoundsException exception){
      System.out.println("OSG Error! " + exception.getLocalizedMessage());
      return null;
    }
  }

  //concatenate the words into a sentence 
  @Override
  public String stringCat(ArrayList<String> words) {
    try {
      String sentence = words.get(0);
      for (int i = 1; i < words.size(); i++) {
        sentence = sentence.concat(" " + words.get(i));
      }
      return sentence;
    } 
    catch (IndexOutOfBoundsException exception){
      System.out.println("OSG Error, Word List is empty!");
      return null;
    }
  }

  //formats the words accordingly before adding to list
  //upper case conversion and reversal in this case
  @Override
  public String formatString(String word) {
    word = word.toUpperCase();
    String reversedWord = "";
    for(int i=word.length()-1; i>=0; i--){
      reversedWord = reversedWord.concat(String.valueOf(word.charAt(i)));
    }
    return reversedWord;
  }

  //adds formatted words to the list
  @Override
  public void addWordToList(String formattedWord) {
    wordList = getWordList();
    wordList.add(formattedWord);
  }

  public static ArrayList<String> getWordList() {
    return wordList;
  }
}