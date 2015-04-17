import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by anton on 15-04-17.
 */
public class SilverscreenSwing {
    private JPanel panel;
    private JLabel label;

    private Firebase firebase;


    public static void main(String[] args) {


        JFrame frame = new JFrame("SilverscreenSwing");
        frame.setContentPane(new SilverscreenSwing().panel);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }


    private void createUIComponents() throws IOException {

        panel = new JPanel();
        firebase = new Firebase("https://klara.firebaseio.com/");
        firebase.child("top_movies").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                Iterable<DataSnapshot> dsList = dataSnapshot.getChildren();

                

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

        URLConnection connection = new URL("https://walter.trakt.us/images/movies/000/000/612/fanarts/original/f4fe709963.jpg").openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        connection.connect();

        BufferedImage picture = null;
        try {
            picture = ImageIO.read(connection.getInputStream());
            label = new JLabel(new ImageIcon(picture));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
