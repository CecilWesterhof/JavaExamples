/*
  It can be useful to get information about memory usage. That is why I wrote this class.
  To get a panel that shows the memory usage, use:
      getPanel(seconds);

  This returns a panel that has every interval seconds the memory usage updated.
  When called without a parameter it defaults to every 15 seconds.
  Minimum value:   1.
  Maximum value: 900.

  There is also a function debugMemory which prints the memory usage on System.out.

  When this class is executed it displays a form with a (default) memory panel on it.
  To  test it:
      javac ShowMemory.java && java ShowMemory

  It should work on every system where Java is intalled.

  An extension could be the possibility to change the interval while running. (setDelay)
 */

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


class ShowMemory {
    // public #########################
    public static void main(String[] args) {
        new ShowMemory();
    }


	public static void debugMemory() {
        long[] memoryValues = getMemoryValues();

		System.out.println("##### Heap utilization statistics [MB] #####");
		System.out.printf("Used Memory:  %6d\n", memoryValues[3]);
		System.out.printf("Free Memory:  %6d\n", memoryValues[0]);
		System.out.printf("Total Memory: %6d\n", memoryValues[2]);
		System.out.printf("Max Memory:   %6d\n", memoryValues[1]);
		System.out.println("############################################");
	}


    public static JPanel getPanel() {
        return getPanel(DEFAULT_SECONDS);
    }


    public static JPanel getPanel(int intervalSeconds) {
        final JLabel freeMem    = new JLabel("", JLabel.RIGHT);
        final JLabel maxMem     = new JLabel("", JLabel.RIGHT);
        final JLabel totalMem   = new JLabel("", JLabel.RIGHT);
        final JLabel usedMem    = new JLabel("", JLabel.RIGHT);

        GridBagConstraints  gbc         = new GridBagConstraints();
        JPanel              showMemory  = new JPanel();

        if (!checkInterval(intervalSeconds)) {
            return null;
        }
        showMemory.setLayout(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 4;
        gbc.ipady = 4;
        showMemory.add(new JLabel("Used Memory"), gbc);
        gbc.gridx = 1;
        showMemory.add(usedMem, gbc);
        ++gbc.gridy;
        gbc.gridx = 0;
        showMemory.add(new JLabel("Free Memory"), gbc);
        gbc.gridx = 1;
        showMemory.add(freeMem, gbc);
        ++gbc.gridy;
        gbc.gridx = 0;
        showMemory.add(new JLabel("Total Memory"), gbc);
        gbc.gridx = 1;
        showMemory.add(totalMem, gbc);
        ++gbc.gridy;
        gbc.gridx = 0;
        showMemory.add(new JLabel("Max Memory"), gbc);
        gbc.gridx = 1;
        showMemory.add(maxMem, gbc);
        showMemory(freeMem, maxMem, totalMem, usedMem);
        ActionListener updateMemory = new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    showMemory(freeMem, maxMem, totalMem, usedMem);
                }
            };
        new Timer(1000 * intervalSeconds, updateMemory).start();
        return showMemory;
    }


    // private ########################
    private final static int        DEFAULT_SECONDS = 15;
    private final static int        MAX_SECONDS     = 900;
    private final static int        MB              = 1024 * 1024;
    private final static int        MIN_SECONDS     = 1;
    private final static Runtime    RUNTIME         = Runtime.getRuntime();
    private final static String     UNIT            = " MB";

    private int intervalSeconds = DEFAULT_SECONDS;

    private ShowMemory() {
        final JFrame showMemory = new JFrame("ShowMemory");

        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    showMemory.add(ShowMemory.getPanel(60));
                    showMemory.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    showMemory.pack();
                    showMemory.setVisible(true);
                }
            });
    }

    private static Boolean checkInterval(int intervalSeconds) {
        if ((intervalSeconds < MIN_SECONDS) || (intervalSeconds > MAX_SECONDS)) {
            return false;
        }
        return true;
    }

    private static long[] getMemoryValues() {
        long free    = RUNTIME.freeMemory()  / MB;
        long max     = RUNTIME.maxMemory()   / MB;
        long total   = RUNTIME.totalMemory() / MB;
        long used    = total - free;

        return new long[] {free, max, total, used};
    }

    private static void showMemory(JLabel freeMem, JLabel maxMem, JLabel totalMem, JLabel usedMem) {
        long[] memoryValues = getMemoryValues();

        freeMem.setText( memoryValues[0] + UNIT);
        maxMem.setText(  memoryValues[1] + UNIT);
        totalMem.setText(memoryValues[2] + UNIT);
        usedMem.setText( memoryValues[3] + UNIT);
    }
}
