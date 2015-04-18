import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.event.IIOReadProgressListener;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by anton on 15-04-17.
 */
public class SilverscreenSwing extends JPanel {
    private JPanel panel;
    private JLabel title;
    private JLabel title1;
    private JLabel title2;
    private JLabel title3;
    private JLabel title4;
    private JLabel spinner;
    private JLabel votes;
    private JLabel votes1;
    private JLabel votes2;
    private JLabel votes3;
    private JLabel votes4;
    private JLabel startarLabel;


    private ArrayList<Movie> movies;

    private BufferedImage picture;
    private URLConnection connection;

    private InetAddress inetAdress;


    public static void main(String[] args) {

        final boolean inFullScreenMode = true;

        final JFrame frame = new JFrame("SilverscreenSwing");
        frame.setContentPane(new SilverscreenSwing().panel);
        frame.setPreferredSize(new Dimension(1080, 720));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

//        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//        GraphicsDevice[] gd = ge.getScreenDevices();
//        frame.dispose();
//        frame.setUndecorated(true);
//        gd[gd.length - 1].setFullScreenWindow(frame);
//        frame.setVisible(true);

        System.setProperty("awt.useSystemAAFontSettings","on");
        System.setProperty("swing.aatext", "true");


        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if(e.getID() == KeyEvent.KEY_PRESSED) {

                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setVisible(false);
                }
                return false;
            }
        });
    }


    private void createUIComponents() throws IOException {

        panel = this;
        title = new JLabel();
        title1 = new JLabel();
        title2 = new JLabel();
        title3 = new JLabel();
        title4 = new JLabel();
        votes = new JLabel();
        votes1 = new JLabel();
        votes2 = new JLabel();
        votes3 = new JLabel();
        votes4 = new JLabel();
        spinner = new JLabel();
        startarLabel = new JLabel();
        spinner.setText("Laddar...");


        movies = new ArrayList<Movie>();



        Firebase firebase = new Firebase("https://klara.firebaseio.com/");
        firebase.child("top_movies").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                startarLabel.setVisible(false);
                Map<String, Object> movie = (Map<String, Object>) dataSnapshot.getValue();

                if(dataSnapshot.hasChildren()) {
                    Movie m = new Movie(dataSnapshot.getKey(),(String) movie.get("title"), (String) movie.get("fanart"), (String) movie.get("year"), (Long) movie.get("votes"));
                    if(!movies.contains(m)) {
                        movies.add(m);
                    }
                }

                Collections.sort(movies, new MovieVoteSorter());


                try {
                    String getURL = movies.get(movies.size() - 1).getUrl();
                    URL url = new URL(getURL);
                    connection = url.openConnection();
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
                    connection.connect();


                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                try {


                    ImageInputStream imageInputStream = ImageIO.createImageInputStream(connection.getInputStream());
                    Iterator<ImageReader> imageReaderIterator = ImageIO.getImageReaders(imageInputStream);
                    if(imageReaderIterator.hasNext()) {
                        ImageReader reader = imageReaderIterator.next();
                        reader.addIIOReadProgressListener(new IIOReadProgressListener() {
                            @Override
                            public void sequenceStarted(ImageReader source, int minIndex) { }

                            @Override
                            public void sequenceComplete(ImageReader source) { }

                            @Override
                            public void imageStarted(ImageReader source, int imageIndex) { }

                            @Override
                            public void imageProgress(ImageReader source, float percentageDone) {
                                spinner.setText(Math.ceil(percentageDone) + "%");
                            }

                            @Override
                            public void imageComplete(ImageReader source) {
                                spinner.setText("100 %");
                            }

                            @Override
                            public void thumbnailStarted(ImageReader source, int imageIndex, int thumbnailIndex) { }

                            @Override
                            public void thumbnailProgress(ImageReader source, float percentageDone) { }

                            @Override
                            public void thumbnailComplete(ImageReader source) { }

                            @Override
                            public void readAborted(ImageReader source) { }
                        });

                        reader.setInput(imageInputStream);
                        picture = reader.read(0);
                    }

                    spinner.setText(" ");
                    panel.revalidate();
                    panel.repaint();

                } catch (IOException e) {

                    e.printStackTrace();
                }


                title.setText(" " + movies.get( movies.size() - 1).getTitle() + " ");
                votes.setText(" " +movies.get(movies.size() -1).getVotes() + " ");
                if(movies.size() > 1) {
                    title1.setText(" " + movies.get(movies.size() - 2).getTitle() + " " );
                    votes1.setText(" " +movies.get(movies.size() -2).getVotes() + " ");

                }
                if(movies.size() > 2) {
                    title2.setText(" " +movies.get(movies.size() - 3).getTitle() + " ");
                    votes2.setText(" " + movies.get(movies.size() - 3).getVotes() + " ");
                }
                if(movies.size() > 3) {
                    title3.setText(" " +movies.get(movies.size() - 4).getTitle() + " ");
                    votes3.setText(" " +movies.get(movies.size() - 4).getVotes() + " ");
                }
                if(movies.size() > 4) {
                    title4.setText(" " +movies.get(movies.size() - 5).getTitle() + " ");
                    votes4.setText(" " +movies.get(movies.size() -5).getVotes() + " ");
                }


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

                Collections.sort(movies, new MovieVoteSorter());

                URLConnection connection = null;
                try {
                    String getURL = movies.get(movies.size() - 1).getUrl();
                    URL url = new URL(getURL);
                    connection = url.openConnection();
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
                    connection.connect();


                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                try {
                    spinner.setText("Uppdaterar...");
                    picture = ImageIO.read(connection.getInputStream());
                    spinner.setText(" ");
                    panel.revalidate();
                    panel.repaint();


                } catch (IOException e) {
                    e.printStackTrace();
                }


                for(Movie m : movies) {
                    System.out.println(m.getTitle() + " " + m.getVotes());
                }

                title.setText(" " + movies.get( movies.size() - 1).getTitle() + " ");
                votes.setText(" " +movies.get(movies.size() -1).getVotes() + " ");
                if(movies.size() > 1) {
                    title1.setText(movies.get(movies.size() - 2).getTitle());
                    votes1.setText(" " +movies.get(movies.size() -2).getVotes() + " ");

                }
                if(movies.size() > 2) {
                    title2.setText(movies.get(movies.size() - 3).getTitle());
                    votes2.setText(" " + movies.get(movies.size() - 3).getVotes() + " ");
                }
                if(movies.size() > 3) {
                    title3.setText(movies.get(movies.size() - 4).getTitle());
                    votes3.setText(" " +movies.get(movies.size() - 4).getVotes() + " ");
                }
                if(movies.size() > 4) {
                    title4.setText(movies.get(movies.size() - 5).getTitle());
                    votes4.setText(" " +movies.get(movies.size() -5).getVotes() + " ");
                }


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(FirebaseError firebaseError) { }

        });

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(picture, 0, 0, panel.getWidth(), panel.getHeight(), null);
    }

}


