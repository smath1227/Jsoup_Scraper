import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.io.*;
import java.util.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * A DataCollector class to manage social media posts
 */
public class DataCollector
{
  private ArrayList<String> socialMediaPosts;
  private ArrayList<String> targetWords;
  private Scanner sc;
  private int currentPost;
  private int currentTargetWord;

  public DataCollector()
  {
    socialMediaPosts = new ArrayList<String>();
    targetWords = new ArrayList<String>();
    currentPost = 0;
    currentTargetWord = 0;
  }

  /**
   * Gather the data contained in the files socialMediaPostsFilename and
   * targetWordsFilename (including punctuation), with words separated by a single
   * space
   *
   * @param socialMediaPostsFilename the name of the file containing social media posts
   * @param targetWordsFilename the name of the file containing the target words
   */
  public void setData(String socialMediaPostsFilename, String targetWordsFilename) {
    // read in the social media posts found in socialMediaPosts
    // a try is like an if statement, "throwing" an error if the body of the try fails
    try
    {
      sc = new Scanner(new File(socialMediaPostsFilename));
      while (sc.hasNextLine())
      {
        // String method trim removes whitespace before and after a string
        String temp = sc.nextLine().trim();
        // DEBUG: System.out.println(temp);
        this.socialMediaPosts.add(temp);
      }
    } catch (Exception e) { System.out.println("Error reading or parsing" + socialMediaPosts + "\n" + e); }

    // read in the target words in targetWords
    try
    {
      sc = new Scanner(new File(targetWordsFilename));
      while (sc.hasNextLine())
      {
        // String method trim removes whitespace before and after a string
        this.targetWords.add(sc.nextLine().trim());
      }
    } catch (Exception e) { System.out.println("Error reading or parsing" + targetWords + "\n" + e); }
  }

  /**
   * Get the next post in socialMediaPosts with words separated by a single space, 
   * or "NONE" if there is no more data.
   *
   * @return a string containing one of the lines in socialMediaPosts
   */
  public String getNextPost()
  {
    if (currentPost < socialMediaPosts.size())
    {
      this.currentPost++;
      return socialMediaPosts.get(currentPost - 1);
    }
    else
    {
      return "NONE";
    }
  }

  /**
   * Get the next line in targetWords, with words separated by a space,
   * or "NONE" if there is no more data.
   *
   * @return a string containing one of the lines in targetWords
   */
  public String getNextTargetWord()
  {
    if (currentTargetWord < targetWords.size())
    {
      this.currentTargetWord++;
      return targetWords.get(currentTargetWord - 1);
    }
    else
    {
      this.currentTargetWord = 0;
      return "NONE";
    }
  }

  /**
   * Create a File named filename and stores all the usernames to target
   *
   * @param filename The name to save the file, must include .txt
   * @param usernames A string containing the usernames of people to target,
   * usernames are separated by a space.
   */
  public void prepareAdvertisement(String filename, String usernames, String animals) throws Exception
  {
    try
    {

      FileWriter fw = new FileWriter(filename);
      ArrayList<String> userList = new ArrayList<String>(Arrays.asList(usernames.split(" ")));

      for (String un : userList)
      {

        fw.write("@" + un + " " + getAdvertisement("advertisements.txt", animals, animals.split(" ")[userList.indexOf(un)], userList.indexOf(un)) +"\n");
      }

      fw.close();
    }

    catch (IOException e)
    {
      System.out.println("Could not write to file. " + e);
    }
  }

  /**
   * Print the array of posts
   */
  public void printAllPosts()
  {
    for (String post : this.socialMediaPosts)
      System.out.println(post);
  }

  /**
   * Print the array of target words
   */
  public void printAllTargetWords()

  {
    for (String word : this.targetWords)
      System.out.println(word);
  }

  public static int count(String str, String animal) {
    int count = 0;
    String[] words = str.split(" "); // Split the string into words

    for (String w : words) {
      if (w.equals(animal)) {
        count++;
      }
    }
    return count;
  }

  public static String getAdvertisement(String filename, String animals, String animal, int index) throws Exception {
    int c = count(animals, animal);

    if (animal.equals("cat")) {
      index = index - (count(animals, "dog") - 1);
    }

    if (animal.equals("pet")) {
      index = index - (count(animals, "dog") - 1) - (count(animals, "cat") - 1);
    }

    if (index < (c/3)) {
      return Files.readAllLines(Paths.get(filename)).get(0).replace("animal", animal);
    }
    else if (index >= (c/3) && index <= ((c/3) * 2)) {
      return Files.readAllLines(Paths.get(filename)).get(1).replace("animal", animal);
    }
    else {
      return Files.readAllLines(Paths.get(filename)).get(2).replace("animal", animal);
    }

  }


}
