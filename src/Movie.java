/**
 * Created by anton on 2015-04-17.
 */
public class Movie implements Comparable<Movie> {

    private String slug, title, url;
    private Long votes, year;


    public Movie(String slug, String title, String url, Long year, Long votes) {
        this.slug = slug;
        this.title = title;
        this.url = url;
        this.votes = votes;
        this.year = year;
    }


    public String getSlug() {
        return slug;
    }


    public String getTitle() {
        return title;
    }


    public String getUrl() {
        return url;
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
