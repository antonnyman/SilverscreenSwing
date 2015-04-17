import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * Created by anton on 15-04-17.
 */
public class SilverscreenSwing {
    private JPanel panel;
    private JLabel label;
    private JLabel title;

    ArrayList<Movie> movies;

    BufferedImage picture;
    URLConnection connection;


    public static void main(String[] args) {

        int PrevX = 100, PrevY = 100, PrevWidth = 800, PrevHeight = 600;
        boolean inFullScreenMode = true;

        final JFrame frame = new JFrame("SilverscreenSwing");
        frame.setContentPane(new SilverscreenSwing().panel);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gd = ge.getScreenDevices();
        if(inFullScreenMode){
            frame.dispose();
            frame.setUndecorated(true);
            gd[gd.length-1].setFullScreenWindow(frame);
            frame.setVisible(true);

        }

        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager(); //Listen to keyboard
        manager.addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_TYPED) {
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setVisible(false);
                }
                return false;
            }
        });
    }


    private void createUIComponents() throws IOException {

        panel = new JPanel();
        label = new JLabel();
        title = new JLabel();

        movies = new ArrayList<Movie>();



        Firebase firebase = new Firebase("https://klara.firebaseio.com/");
        firebase.child("top_movies").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Map<String, Object> movie = (Map<String, Object>) dataSnapshot.getValue();

                if(dataSnapshot.hasChildren()) {
                    Movie m = new Movie(dataSnapshot.getKey(),(String) movie.get("title"), (String) movie.get("fanart"), (String) movie.get("year"), (Long) movie.get("votes"));
                    if(!movies.contains(m)) {
                        movies.add(m);
                    }
                }

                Collections.sort(movies, new MovieVoteSorter());

                title.setText(movies.get(movies.size() - 1).getTitle());



                try {
                    String getURL = movies.get(movies.size() - 1).getUrl();
                    System.out.println(getURL);
                    URL url = new URL(getURL);
                    connection = url.openConnection();
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
                    connection.connect();


                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                try {
                    picture = ImageIO.read(connection.getInputStream());
                    label.setIcon(new ImageIcon(picture));


                } catch (IOException e) {
                    e.printStackTrace();
                }




                for(Movie m : movies) {
                    System.out.println(m.getSlug() + " " +m.getTitle() + " " + m.getVotes() + " " + m.getUrl());
                }
                panel.revalidate();
                panel.repaint();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                Map<String, Object> movie = (Map<String, Object>) dataSnapshot.getValue();

                Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();
                Collections.sort(movies);

                int place = Collections.binarySearch(movies, new Movie(dataSnapshot.getKey(),(String) movie.get("title"), (String) movie.get("fanart"), (String) movie.get("year"), (Long) movie.get("votes") + 2));

                for(DataSnapshot ds : dataSnapshots) {
                    if(ds.getKey().equals("votes")) {
                        movies.get(place).setVotes((Long) ds.getValue());
                    }
                }

                panel.repaint();

                Collections.sort(movies, new MovieVoteSorter());

                BufferedImage picture;
                URLConnection connection = null;
                try {
                    String getURL = movies.get(movies.size() - 1).getUrl();
                    System.out.println(getURL);
                    URL url = new URL(getURL);
                    connection = url.openConnection();
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
                    connection.connect();


                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                try {
                    picture = ImageIO.read(connection.getInputStream());
                    label.setIcon(new ImageIcon(picture));
                    label.revalidate();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                for(Movie m : movies) {
                    System.out.println(m.getSlug() + " " +m.getTitle() + " " + m.getVotes() + " " + m.getUrl());
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });

    }

}


