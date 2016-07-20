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

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import javax.swing.filechooser.FileFilter;


@SuppressWarnings("serial")
public class CropImage extends Panel {
    static private final int MAX_HEIGHT = 3456;
    static private final int MAX_WIDTH  = 5184;

    static private int            cropHeight  = 135;
    static private int            cropWidth   = 135;
    static private String         currentName;
    static private JFileChooser   fileopen    =
        new JFileChooser(System.getProperty("user.home") + "/Pictures");
    static private JFileChooser   filesave    =
        new JFileChooser(System.getProperty("user.home") + "/Pictures");
    static private BufferedImage  image;
    static private int            imageHeight = 432;
    static private int            imageWidth  = 648;
    static private Color          rectColor   = new Color(0, 0, 0);
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
        File input = fileopen.getSelectedFile();
        currentName = input.getAbsolutePath();
        image  = ImageIO.read(input);
        photoWidth  = image.getWidth();
        photoHeight = image.getHeight();
        if ((photoWidth > MAX_WIDTH) || (photoHeight > MAX_HEIGHT)) {
            System.out.println(String.format("Dimensions %s not correct",
                                             currentName));
            System.exit(1);
        }
        imageWidth  = photoWidth  / 8;
        imageHeight = photoHeight / 8;
        setSize(imageWidth, imageHeight);
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

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setStroke(stroke);
        g.setColor(rectColor);
        g.drawImage(image, 0, 0, imageWidth, imageHeight, null);
        g.drawRect(x, y, cropWidth, cropHeight);
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
        JButton crop  = new JButton("Crop");
        JFrame  frame = new JFrame("Crop image");
        JButton load  = new JButton("Load");
        Panel   panel = new CropImage();

        frame.setLayout(null);
        crop.setToolTipText("To save the cropped image");
        crop.setBounds(10, imageHeight + 10, 80, 30);
        crop.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    int ret = filesave.showDialog(panel, "Open file");

                    if (ret == JFileChooser.APPROVE_OPTION) {
                        BufferedImage dest = new BufferedImage(cropWidth * 8,
                                                               cropHeight * 8,
                                                               BufferedImage.TYPE_3BYTE_BGR);
                        Graphics g = dest.getGraphics();
                        File output = filesave.getSelectedFile();

                        g.drawImage(image,
                                    0, 0,
                                    cropWidth * 8, cropHeight * 8,
                                    x * 8, y * 8,
                                    (x + cropWidth) * 8, (y + cropHeight) * 8,
                                    null);
                        g.dispose();
                        try {
                            ImageIO.write(dest, "JPG", output);
                        } catch (IOException e) {
                            System.out.println("Could not write: " + output.getAbsolutePath());
                        }
                        System.out.println(String.format("convert %s -crop 1080x1080+%d+%d %s; display %s",
                                                         currentName,
                                                         x * 8, y * 8,
                                                         output.getAbsolutePath(),
                                                         output.getAbsolutePath()));
                    }
                }
            });
        load.setToolTipText("To load a new image");
        load.setBounds(110, imageHeight + 10, 80, 30);
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

                            if ((photoWidth > MAX_WIDTH) || (photoHeight > MAX_HEIGHT)) {
                                System.out.println(String.format("Dimensions %s not correct",
                                             newName));
                            } else {
                                imageWidth  = photoWidth  / 8;
                                imageHeight = photoHeight / 8;
                                currentName = newName;
                                image       = newImage;
                                verifyXAndY();
                                panel.repaint();
                            }
                        }
                    }
                }
            });
        frame.getContentPane().add(panel);
        frame.getContentPane().add(crop);
        frame.getContentPane().add(load);
        frame.setSize(imageWidth + 10, imageHeight + 80);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
