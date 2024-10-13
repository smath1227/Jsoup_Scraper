public class Review {

  private String user;
  private String rating;
  private String comment;
  private String date;
  private String verified;
  private String title;

  // Review Constructor
  public Review(String title, String user, String verified, String date, String rating, String comment) {
    this.title = title;
    this.user = user;
    this.date = date;
    this.rating = rating;
    this.comment = comment;
    this.verified = verified;
  }

  // Getters
  public String getUser() {
    return user;
  }

  public String getDate() {
    return date;
  }

  public String getRating() {
    return rating;
  }

  public String getComment() {
    return comment;
  }

  public String getVerified() {
    return verified;
  }

  public String getTitle() {
    return title;
  }

}
