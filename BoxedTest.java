import java.time.format.DateTimeFormatter;
import java.time.LocalTime;


public class BoxedTest {
    // ################ main
    public static void main(final String[] args) {
        for(final Storage storage : storageList) {
            test(storage);
        }
    }

    // ################ constructors
    // Do not allow initiation
    private BoxedTest() {
    }


    // ################ private
    private static String getCurrentTime() {
        return LocalTime.now().format(timeFormat);
    }

	private static void test(final Storage storage) {
        System.out.printf("%s: Timing %s\n", getCurrentTime(), storage.description);
        for (int r = 0; r < repeats; ++r) {
            long tDuration;
            long tStart;

            System.out.printf("%s: ", getCurrentTime());
            tStart = System.nanoTime();
            storage.function.run();
            tDuration = System.nanoTime() - tStart;
            System.out.printf("took %.2E seconds.\n", tDuration / 1_000_000_000.0);
        }
        System.out.println();
	}

    private static final int                repeats       = 5;
    private static final DateTimeFormatter  timeFormat    = DateTimeFormatter.ofPattern("HH:mm:ss");


    // ################ functions to test
    private static Thread boxedBoxed = new Thread(new Runnable() {
            public void run() {
                Long sum = 0L;

                for (Long i = Long.valueOf(start); i < end; ++i) {
                    sum += i;
                }
            }
        });

    private static Thread boxedBoxedInt = new Thread(new Runnable() {
            public void run() {
                Long sum = 0L;

                for (Integer i = start; i < end; ++i) {
                    sum += i;
                }
            }
        });

    private static Thread boxedNotBoxed = new Thread(new Runnable() {
            public void run() {
                Long sum = 0L;

                for (long i = start; i < end; ++i) {
                    sum += i;
                }
            }
        });

    private static Thread boxedNotBoxedInt = new Thread(new Runnable() {
            public void run() {
                Long sum = 0L;

                for (int i = start; i < end; ++i) {
                    sum += i;
                }
            }
        });

    private static Thread notBoxedBoxed = new Thread(new Runnable() {
            public void run() {
                long sum = 0L;

                for (Long i = Long.valueOf(start); i < end; ++i) {
                    sum += i;
                }
            }
        });

    private static Thread notBoxedBoxedInt = new Thread(new Runnable() {
            public void run() {
                long sum = 0L;

                for (Integer i = start; i < end; ++i) {
                    sum += i;
                }
            }
        });

    private static Thread notBoxedNotBoxed = new Thread(new Runnable() {
            public void run() {
                long sum = 0L;

                for (long i = start; i < end; ++i) {
                    sum += i;
                }
            }
        });

    private static Thread notBoxedNotBoxedInt = new Thread(new Runnable() {
            public void run() {
                long sum = 0L;

                for (int i = start; i < end; ++i) {
                    sum += i;
                }
            }
        });

    private static final Storage storageList[] = new Storage[] {
        new Storage(notBoxedNotBoxedInt, "long int    "),
        new Storage(notBoxedNotBoxed,    "long long   "),
        new Storage(boxedNotBoxedInt,    "Long int    "),
        new Storage(boxedNotBoxed,       "Long long   "),
        new Storage(notBoxedBoxedInt,    "long Integer"),
        new Storage(notBoxedBoxed,       "long Long   "),
        new Storage(boxedBoxedInt,       "Long Integer"),
        new Storage(boxedBoxed,          "Long Long   "),
    };

    private static int end   = Integer.MAX_VALUE;
    private static int start = Integer.MIN_VALUE;

    // ################ inner class
    private static class Storage {
        Storage(final Thread function, final String description) {
            this.function    = function;
            this.description = description;
        }

        public final Thread function;
        public final String description;
    }

}
