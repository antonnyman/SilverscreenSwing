import java.util.Comparator;

/**
 * Created by anton on 2015-04-17.
 */
public class MovieVoteSorter implements Comparator<Movie> {
    @Override
    public int compare(Movie o1, Movie o2) {
        return o1.getVotes().compareTo(o2.getVotes());
    }
}
