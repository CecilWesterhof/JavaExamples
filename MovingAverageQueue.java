/*
  http://en.wikipedia.org/wiki/Moving_average

  This is an implementation of moving average in java. This is the
  ‘neat’ variant.

  Moving average gives in the normal definition only a value when there
  are ate least LENGTH values. My implementation gives always a result.
  You can always ignore the first LENGTH - 1 values.

  Also look at MovingAverageArray. That is a more simple solution. For
  clearness of code I prefer this variant, but this variant could have a
  performance problem. (For one we use Float instead of float.)
 */

import java.util.*;

class MovingAverageQueue {
    // public #########################
    public MovingAverageQueue(int N) {
        LENGTH      = N;
        oldValues   = new LinkedList<>();

    }

    public static void main(String[] args) {
        int             i;
        float           inputs06[]      = {
            20, 22, 21, 24, 24, 23, 25, 26, 20, 24,
            26, 26, 25, 27, 28, 27, 29, 27, 25, 24
        };
        float           inputs10[]      = {
            20, 22, 24, 25, 23, 26, 28, 26, 29, 27,
            28, 30, 27, 29, 28
        };
        MovingAverageQueue   movingAverage;

        System.out.println("Moving Average length 6:");
        movingAverage   = new MovingAverageQueue(6);
        for (i = 0; i < inputs06.length; ++i) {
            System.out.println(movingAverage.movingAverage(inputs06[i]));
        }
        System.out.println("Moving Average length 10:");
        movingAverage   = new MovingAverageQueue(10);
        for (i = 0; i < inputs10.length; ++i) {
            System.out.println(movingAverage.movingAverage(inputs10[i]));
        }
    }

    public float movingAverage(float next) {
        currentTotal += next;
        oldValues.add(next);
        if (oldValues.size() > LENGTH) {
            currentTotal -= oldValues.remove();
        }
        return currentTotal / oldValues.size();
    }


    // private ########################
    private final int LENGTH;

    private float           currentTotal    = 0;
    private Queue<Float>    oldValues;

}
