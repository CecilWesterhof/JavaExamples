/*
  I needed to write some code that executes something in the Bash shell.
  This is not to difficult, but when there are spaces, things can become difficult.
  But if you know how it works, then there is no problem.

  It can be done Runtime.getRuntime().exec. In this example that would be good enough.
  (See SystemCommand.java) But when you have a little bit more difficult things to do,
  it is better to use ProcessBuilder. That I do in this example.

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
      javac  SystemCommandWithProcessBuilder.java && \
        java SystemCommandWithProcessBuilder      && \
        display citation.png

  To use this you need of-course Java, but besides that ImageMagick needs to be installed.
  It is written for Linux.
 */

import java.io.*;

public class SystemCommandWithProcessBuilder {
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


    private static void doCommand(final String[] cmd) throws IOException {
        BufferedReader      br;
        String              line;
        Process             p;

        p   = new ProcessBuilder(cmd).start();
        br  = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }
}
