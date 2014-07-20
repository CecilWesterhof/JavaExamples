/*
  ‘Check’ that MovingAverageQueue and MovingAverageArray do the same.
 */

class MovingAverageTest {
    // public #########################
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
        MovingAverageArray   movingAverageArray;
        MovingAverageQueue   movingAverageQueue;

        System.out.println("Test Moving Average with length 6:");
        movingAverageArray  = new MovingAverageArray(6);
        movingAverageQueue  = new MovingAverageQueue(6);
        for (i = 0; i < inputs06.length; ++i) {
            if (movingAverageArray.movingAverage(inputs06[i]) !=
                movingAverageQueue.movingAverage(inputs06[i])) {
                System.out.println("Different results at: " + i);
            }
        }
        System.out.println("TestMoving Average length 10:");
        movingAverageArray  = new MovingAverageArray(10);
        movingAverageQueue  = new MovingAverageQueue(10);
        for (i = 0; i < inputs10.length; ++i) {
            if (movingAverageArray.movingAverage(inputs10[i]) !=
                movingAverageQueue.movingAverage(inputs10[i])) {
                System.out.println("Different results at: " + i);
            }
        }
    }

}
