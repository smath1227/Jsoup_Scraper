// General imports
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.BufferedWriter;

// JSoup imports
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scraper {

  private static int pageNumber = 1;
  private static String url = "https://www.walmart.com/reviews/product/533396764?page=" + pageNumber;
  private static boolean enoughReviews = false;

  private static ArrayList<Review> revs = new ArrayList<Review>();

  public static void run() {
    System.out.println("Scraping reviews..");
    Scraper.scrape(url);
    Scraper.writeToCSV();
    System.out.println("done");
  }

  private static void scrape(String url) {

    // scrape until there are more than 50 reviews
    while (!enoughReviews) {

      try {

        // only take reviews from the main review section, not the "most helpful" section
        Document doc = Jsoup.connect(url).get();
        Elements reviewTable = doc.select("li.dib.w-100.mb3");
        Elements reviews = reviewTable.select(".w_DHV_.pv3.mv0");

        // scrape important information for each review
        for (Element review : reviews) {

          String title = review.select("h3.w_kV33.w_Sl3f.w_mvVb.f5.b").text();
          String user = review.select("div.f6.gray.pr2.mb2").text();
          String comment = review.select(".tl-m.mb3.db-m").text();
          String rating = review.select("span.w_iUH7").text();
          String date = review.select(".f7.gray.mt1").text();
          String verified = review.select("span.green.b.mr1").text();

          // set value to N/A if value not present
          if (user.length() == 0) {
            user = "N/A";
          }

          if (date.length() == 0) {
            date = "N/A";
          }

          if (comment.length() == 0) {
            comment = "N/A";
          }

          if (verified.length() == 0) {
            verified = "Unverified Purchase";
          }

          if (title.length() == 0) {
            title = "No title";
          }

          // Create Review object and add to ArrayList
          Review singleReview = new Review(title, user, verified, date, rating, comment);
          revs.add(singleReview);
        }


        // switch pages if not enough reviews
        if (revs.size() <= 50) {
          pageNumber += 1;
          url = "https://www.walmart.com/reviews/product/533396764?page=" + pageNumber;
        }

        else {
          enoughReviews = true;
        }

      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  // Write to CSV file using revs ArrayList
  private static void writeToCSV() {

    try {

      // define writing tool
      FileWriter writer = new FileWriter("reviews");
      BufferedWriter bw = new BufferedWriter(writer);

      // header
      bw.write("User, Verification, Date, Rating, Comment");
      bw.newLine();

      // add each review to a new line in the CSV
      for (Review rev : revs) {
        bw.write(rev.getUser() + ", " + rev.getVerified() + ", " + rev.getDate() + ", " + rev.getRating() + ", " + rev.getComment());
        bw.newLine();
      }

      bw.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
