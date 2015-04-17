/**
 * Created by anton on 2015-04-17.
 */
public class Movie implements Comparable<Movie> {

    private String slug, title, url, year;
    private Long votes;


    public Movie(String slug, String title, String url, String year, Long votes) {
        this.slug = slug;
        this.title = title;
        this.url = url;
        this.year = year;
        this.votes = votes;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Long getVotes() {
        return votes;
    }

    public void setVotes(Long votes) {
        this.votes = votes;
    }

    @Override
    public int compareTo(Movie o) {
        return slug.compareTo(o.getSlug());
    }
}
