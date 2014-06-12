/*
  I needed to write some code that executes something in the Bash shell.
  This is not to difficult, but when there are spaces, things can become difficult.
  But if you know how it works, then there is no problem.

  The solution is: create a String array in which the first entry is the command
  and every parameter has his own entry (without quoutes).

  This program is the equavelent of:
      /usr/bin/convert -background "NavyBlue" -fill "Yellow" -font "Bookman-DemiItalic" -pointsize 24 label:"
          I hope I shall always possess
          firmness and virtue enough
          to maintain
          what I consider the most
          enviable of all titles,
          the character of an honest man.

          George Washington" citation.png

  To  test it:
      javac SystemCommand.java && java SystemCommand && display citation.png

  To use this you need of-course Java, but besides that ImageMagick needs to be installed.
  It is written for Linux.
 */

import java.util.Scanner;
import java.io.*;

public class SystemCommand {
    // public #########################
    public static void main(String[] args) {
    	try {
            doCommand(new String[] {"/usr/bin/convert",
                                    "-background", BACKGROUND,
                                    "-fill",       FILL,
                                    "-font",       FONT,
                                    "-pointsize",  FONTSIZE,
                                    "label:" +     CITATION,
                                    FILENAME});
    	}
    	catch (IOException e) {
    		System.out.println(e.getMessage());
    	}
    }


    // private ########################
    final static String INDENT     = "    ";

    final static String AUTHOR     = "George Washington";
    final static String BACKGROUND = "NavyBlue";
    final static String CITATION   =
        "\n"   +

        INDENT + "I hope I shall always possess"   + INDENT + "\n" +
        INDENT + "firmness and virtue enough"      + INDENT + "\n" +
        INDENT + "to maintain"                     + INDENT + "\n" +
        INDENT + "what I consider the most"        + INDENT + "\n" +
        INDENT + "enviable of all titles,"         + INDENT + "\n" +
        INDENT + "the character of an honest man." + INDENT + "\n" +
        "\n"   +
        INDENT + AUTHOR                            + INDENT + "\n";
    final static String FILENAME   = "citation.png";
    final static String FILL       = "Yellow";
    final static String FONT       = "Bookman-DemiItalic";
    final static String FONTSIZE   = "24";

    static Runtime runtime = Runtime.getRuntime();


    private static void doCommand(final String[] cmd) throws IOException {
        int     i;
        Process p;
        Scanner sc;

        p  = runtime.exec(cmd);
        sc = new Scanner(p.getInputStream());
        while (sc.hasNext()) {
            System.out.println(sc.nextLine());
        }
    }
}
