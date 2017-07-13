import java.time.format.DateTimeFormatter;
import java.time.LocalTime;
import java.util.function.Supplier;


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
            for (int ir = 0; ir < innerRepeats; ++ir) {
                storage.function.get();
            }
            tDuration = System.nanoTime() - tStart;
            System.out.printf("took %.2E seconds.\n", tDuration / 1_000_000_000.0);
        }
        System.out.println();
	}

    private static final int                end           = 100;
    private static final int                innerRepeats  = 100_000_000;
    private static final int                repeats       = 5;
    private static final int                start         = 0;
    private static final DateTimeFormatter  timeFormat    = DateTimeFormatter.ofPattern("HH:mm:ss");


    // ################ functions to test
    private static Supplier<Long> boxedBoxed = () -> {
        Long sum = 0L;

        for (Long i = Long.valueOf(start); i < end; ++i) {
            sum += i;
        }
        return sum;
    };

    private static Supplier<Long> boxedBoxedInt = () -> {
        Long sum = 0L;

        for (Integer i = start; i < end; ++i) {
            sum += i;
        }
        return sum;
    };

    private static Supplier<Long> boxedNotBoxed = () -> {
        Long sum = 0L;

        for (long i = start; i < end; ++i) {
            sum += i;
        }
        return sum;
    };

    private static Supplier<Long> boxedNotBoxedInt = () -> {
        Long sum = 0L;

        for (int i = start; i < end; ++i) {
            sum += i;
        }
        return sum;
    };

    private static Supplier<Long> notBoxedBoxed = () -> {
        long sum = 0L;

        for (Long i = Long.valueOf(start); i < end; ++i) {
            sum += i;
        }
        return sum;
    };

    private static Supplier<Long> notBoxedBoxedInt = () -> {
        long sum = 0L;

        for (Integer i = start; i < end; ++i) {
            sum += i;
        }
        return sum;
    };

    private static Supplier<Long> notBoxedNotBoxed = () -> {
        long sum = 0L;

        for (long i = start; i < end; ++i) {
            sum += i;
        }
        return sum;
    };

    private static Supplier<Long> notBoxedNotBoxedInt = () -> {
        long sum = 0L;

        for (int i = start; i < end; ++i) {
            sum += i;
        }
        return sum;
    };

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

    // ################ inner class
    private static class Storage {
        Storage(final Supplier<Long> function, final String description) {
            this.function    = function;
            this.description = description;
        }

        public final Supplier<Long> function;
        public final String         description;
    }

}
