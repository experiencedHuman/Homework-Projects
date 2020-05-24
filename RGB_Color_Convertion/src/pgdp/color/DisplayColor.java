package pgdp.color;

import javax.swing.*;
import java.awt.*;

/**
 * Vorgegebene Klasse, hier drin wird nichts bewertet !!!
 * <p>
 * Veränderungen werden nicht gewertet.
 */
public class DisplayColor {

    // Zum Testen auskommentieren
    /*
    public static void displayColor(RgbColor8Bit c) {
        JFrame jf = new JFrame("Farbtest") {

            @Override
            public void paint(Graphics g) {
                super.paint(g);
                g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue()));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        jf.setLocation(300, 300);
        jf.setSize(400, 350);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }

    public static void main(String[] args) { // Hier eigene Farben ausprobieren
        RgbColor rgbColor = new RgbColor(2, 3, 0, 0);
        displayColor(rgbColor.toRgbColor8Bit());
    }
    */
//    public static void displayColor(RgbColor8Bit c) {
//        JFrame jf = new JFrame("Farbtest") {
//
//            @Override
//            public void paint(Graphics g) {
//                super.paint(g);
//                g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue()));
//                g.fillRect(0, 0, getWidth(), getHeight());
//            }
//        };
//        jf.setLocation(300, 300);
//        jf.setSize(400, 350);
//        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        jf.setVisible(true);
//    }
//
//    public static void main(String[] args) { // Hier eigene Farben ausprobieren
//        RgbColor rgbColor = new RgbColor(2, 3, 1, 3);
//        displayColor(rgbColor.toRgbColor8Bit());
//    }

}
