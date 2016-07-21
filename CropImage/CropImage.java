import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.Stroke;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.util.Calendar;
import java.util.TimeZone;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import javax.swing.filechooser.FileFilter;


@SuppressWarnings("serial")
public class CropImage extends Panel {
    static class DispImage extends JPanel {
        private int           dispHeight;
        private BufferedImage dispImage;
        private int           dispWidth;

        DispImage(BufferedImage dispImage, int dispWidth, int dispHeight) {
            this.dispImage  = dispImage;
            this.dispWidth  = dispWidth;
            this.dispHeight = dispHeight;
        }

        public void paintComponent(Graphics g) {
            g.drawImage(dispImage, 0, 0, dispWidth, dispHeight, null);
        }
    }

    static private final int MAX_HEIGHT = 3456;
    static private final int MAX_WIDTH  = 5184;

    static private int            cropHeight  = 135;
    static private int            cropWidth   = 135;
    static private String         currentName;
    static private JFileChooser   fileopen;
    static private JFileChooser   filesave;
    static private BufferedImage  image;
    static private int            imageHeight = 432;
    static private JLabel         imageInfo   = new JLabel();
    static private int            imageWidth  = 648;
    static private Color          rectColor   = new Color(0, 0, 0);
    static private int            resizeValue = 8;
    static private JLabel         selectInfo  = new JLabel();
    static private Stroke         stroke      = new BasicStroke(2.0f);
    static private int            x           = 0;
    static private int            y           = 0;

    public CropImage() throws IOException {
        int photoHeight;
        int photoWidth;
        int ret = fileopen.showDialog(null, "Open file");

        if (ret != JFileChooser.APPROVE_OPTION) {
            System.out.println("Did not select a file");
            System.exit(1);
        }
        File input  = fileopen.getSelectedFile();
        currentName = input.getAbsolutePath();
        image       = ImageIO.read(input);
        photoWidth  = image.getWidth();
        photoHeight = image.getHeight();
        if (!verifyDimensions(photoWidth, photoHeight)) {
            System.out.println(String.format("Dimensions %s not correct",
                                             currentName));
            System.exit(1);
        }
        imageWidth  = photoWidth  / resizeValue;
        imageHeight = photoHeight / resizeValue;
        setSize(imageWidth, imageHeight);
        imageInfo.setText(String.format( "Image size: %dx%d, displayed as: %dx%d",
                                         photoWidth, photoHeight,
                                         imageWidth, imageHeight));
        selectInfo.setText(String.format("Crop size: %dx%d, displayed as: %dx%d",
                                         cropWidth * resizeValue, cropHeight * resizeValue,
                                         cropWidth,               cropHeight));
        addMouseListener(new MouseListener() {
                @Override
                public void mouseReleased(MouseEvent e) {
                }
                @Override
                public void mousePressed(MouseEvent e) {
                }
                @Override
                public void mouseExited(MouseEvent e) {
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                }
                @Override
                public void mouseClicked(MouseEvent e) {
                    x = (int) e.getPoint().getX() - cropWidth  / 2;
                    y = (int) e.getPoint().getY() - cropHeight / 2;
                    verifyXAndY();
                    repaint();
                }
            });
    }

    static private void initCrop(JButton crop, Panel panel) {
        crop.setToolTipText("To save the cropped image");
        crop.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    int ret = filesave.showDialog(panel, "Open file");

                    if (ret == JFileChooser.APPROVE_OPTION) {
                        BufferedImage dest;
                        JFrame        dispFrame;
                        int           dispHeight = cropHeight * resizeValue;
                        DispImage     dispImage;
                        int           dispWidth  = cropWidth  * resizeValue;
                        File          output     = filesave.getSelectedFile();

                        dest       = image.getSubimage(x * resizeValue, y * resizeValue,
                                                       dispWidth,       dispHeight);
                        dispFrame = new JFrame(output.getName());
                        dispImage = new DispImage(dest, dispWidth, dispHeight);
                        dispFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        dispFrame.add(dispImage);
                        dispFrame.setSize(dispWidth + 10, dispHeight + 40);
                        dispFrame.setVisible(true);

                        try {
                            ImageIO.write(dest, "JPG", output);
                        } catch (IOException e) {
                            System.out.println("Could not write: " + output.getAbsolutePath());
                        }
                    }
                }
            });
    }

    static private void initLoad(JButton load, Panel panel) {
        load.setToolTipText("To load a new image");
        load.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    BufferedImage  newImage = null;
                    String         newName;
                    int ret = fileopen.showDialog(panel, "Open file");

                    if (ret == JFileChooser.APPROVE_OPTION) {
                        File input = fileopen.getSelectedFile();

                        newName = input.getAbsolutePath();
                        try {
                            newImage = ImageIO.read(input);
                        } catch (IOException e) {
                            System.out.println("Could not read the input file: " + newName);
                        }
                        if (newImage != null) {
                            int photoWidth  = newImage.getWidth();
                            int photoHeight = newImage.getHeight();

                            if (!verifyDimensions(photoWidth, photoHeight)) {
                                System.out.println(String.format("Dimensions %s not correct",
                                                                 newName));
                            } else {
                                imageWidth  = photoWidth  / resizeValue;
                                imageHeight = photoHeight / resizeValue;
                                imageInfo.setText(String.format( "Image size: %dx%d, displayed as: %dx%d",
                                                                 photoWidth, photoHeight,
                                                                 imageWidth, imageHeight));
                                currentName = newName;
                                image       = newImage;
                                verifyXAndY();
                                panel.repaint();
                            }
                        }
                    }
                }
            });
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setStroke(stroke);
        g.setColor(rectColor);
        g.drawImage(image, 0, 0, imageWidth, imageHeight, null);
        g.drawRect(x, y, cropWidth, cropHeight);
    }

    static private boolean verifyDimensions(int width, int height) {
        if ((width > MAX_WIDTH)               || (height > MAX_HEIGHT) ||
            (width < cropWidth * resizeValue) || (height < cropHeight * resizeValue)) {
            return false;
        }
        return true;
    }

    static private void verifyXAndY() {
        if (x < 0 ) {
            x = 0;
        } else if ((x + cropWidth) > imageWidth) {
            x = imageWidth - cropWidth;
        }
        if (y < 0 ) {
            y = 0;
        } else if ((y + cropHeight) > imageHeight) {
            y = imageHeight - cropHeight;
        }
    }

    static public void main(String args[]) throws IOException {
        JButton  crop          = new JButton("Crop");
        JFrame   frame         = new JFrame("Crop image");
        JButton  load          = new JButton("Load");
        Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
        Panel    panel;

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        } catch (Exception e) {
            System.out.println("Could not change the look and feel");
        }
        fileopen = new JFileChooser(String.format("%s/Pictures/%d-%02d/original/",
                                                  System.getProperty("user.home"),
                                                  localCalendar.get(Calendar.YEAR),
                                                  localCalendar.get(Calendar.MONTH) + 1));
        filesave = new JFileChooser(String.format("%s/Pictures/%d-%02d/",
                                                  System.getProperty("user.home"),
                                                  localCalendar.get(Calendar.YEAR),
                                                  localCalendar.get(Calendar.MONTH) + 1));
        panel    = new CropImage();
        initCrop(crop, panel);
        initLoad(load, panel);
        frame.setLayout(null);
        crop.setBounds(      10, imageHeight + 10,  80, 30);
        load.setBounds(     110, imageHeight + 10,  80, 30);
        imageInfo.setBounds( 10, imageHeight + 50, 500, 30);
        selectInfo.setBounds(10, imageHeight + 90, 500, 30);
        frame.getContentPane().add(panel);
        frame.getContentPane().add(crop);
        frame.getContentPane().add(load);
        frame.getContentPane().add(imageInfo);
        frame.getContentPane().add(selectInfo);
        frame.setSize(imageWidth + 10, imageHeight + 160);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
