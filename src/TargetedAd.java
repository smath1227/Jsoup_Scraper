import java.util.*;
import java.io.*;
public class TargetedAd  {
  public static String animals = "";

  public static void main(String[] args) throws Exception
  {

//    Scraper.run(); run only once or whenever update needed
    DataCollector collector = new DataCollector();
    collector.setData("reviews", "targetWords");
    String users = "";
    int catTotal = 0;
    int dogTotal = 0;
    String animals = "";

    ArrayList<String> targetWordsList = new ArrayList<String>();
    ArrayList<Integer> targetWordScore = new ArrayList<Integer>();
    Map<String, Integer> dogMap = new HashMap<>();
    Map<String, Integer> catMap = new HashMap<>();
    Map<String, Integer> petMap = new HashMap<>();


    File targetWordsFile = new File("/Users/sid/IdeaProjects/Jsoup Scraper/src/targetWords");
    Scanner sc = new Scanner(targetWordsFile);

    while (sc.hasNextLine()) {
      targetWordsList.add(sc.next());
      targetWordScore.add(Integer.parseInt(sc.next()));
    }

    boolean hasNextPost = true;
    collector.getNextPost();
    while (hasNextPost) {

      String nextReview = collector.getNextPost();
      String targetUserName = "";
      catTotal = 0;
      dogTotal = 0;

      if (nextReview != "NONE") {

        for (String word : targetWordsList) {

          if (nextReview.contains(word.toLowerCase())) {
            Scanner reviewScanner = new Scanner(nextReview);
            reviewScanner.useDelimiter(",");
            targetUserName = reviewScanner.next();

            if (!users.contains(targetUserName) && !(targetUserName.equals("N/A"))) {
              if (targetWordsList.indexOf(word.toLowerCase()) <= 11) {
                dogTotal += targetWordScore.get(targetWordsList.indexOf(word.toLowerCase()));

              }

              else if (targetWordsList.indexOf(word.toLowerCase()) > 11 && targetWordsList.indexOf(word.toLowerCase()) < 23) {
                catTotal += targetWordScore.get(targetWordsList.indexOf(word.toLowerCase()));

              }
              reviewScanner.close();
            }
          }
        }

        if (catTotal != 0 || dogTotal != 0) {
          if ((dogTotal > catTotal) && (dogTotal >= 3)) {
            // animals += "dog ";
            dogMap.put(targetUserName, dogTotal);
          }

          else if ((catTotal > dogTotal) && (catTotal >= 3)) {
            // animals += "cat ";
            catMap.put(targetUserName, catTotal);
          }

          else {
            // animals += "pet ";
            petMap.put(targetUserName, 0);
          }

        }
      } else
      {
        hasNextPost = false;
      }

    }
    users = sortAndConcat(dogMap, catMap, petMap);

    for (int i = 0; i < dogMap.size(); i++) {
      animals += "dog ";
    }
    for (int i = 0; i < catMap.size(); i++) {
      animals += "cat ";
    }
    for (int i = 0; i < petMap.size(); i++) {
      animals += "pet ";
    }

    collector.prepareAdvertisement("targetMarket.txt", users, animals.trim());


  }

  public static TreeMap<String, Integer> sortByValues(Map<String, Integer> map) {

    TreeMap<String, Integer> sortedMap = new TreeMap<>(new Comparator<String>() {

      @Override // override to avoid duplicate values from being deleted
      public int compare(String key1, String key2) {
        int valueComparison = map.get(key1).compareTo(map.get(key2));
        if (valueComparison == 0) {
          return key1.compareTo(key2);
        }

        return valueComparison;
      }
    });

    sortedMap.putAll(map);
    return sortedMap;
  }

  public static String sortAndConcat(Map<String, Integer> map1, Map<String, Integer> map2, Map<String, Integer> map3) {
    // Create a TreeMap for each map to sort by values
    TreeMap<String, Integer> sortedMap1 = sortByValues(map1);
    TreeMap<String, Integer> sortedMap2 = sortByValues(map2);
    TreeMap<String, Integer> sortedMap3 = sortByValues(map3);

    String users = "";
    for (String key : sortedMap1.keySet()) {
      users += key + " ";
    }
    for (String key : sortedMap2.keySet()) {
      users += key + " ";
    }
    for (String key : sortedMap3.keySet()) {
      users += key + " ";
    }

    return users.trim(); // Remove trailing space
  }



}
