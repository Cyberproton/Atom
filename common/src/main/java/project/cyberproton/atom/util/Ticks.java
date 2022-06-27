package project.cyberproton.atom.util;

import org.jetbrains.annotations.NotNull;
import java.util.concurrent.TimeUnit;

/**
 * Utility for converting between Minecraft game ticks and standard durations.
 */
public final class Ticks {
    // the number of ticks which occur in a second - this is a server implementation detail
    public static final int TICKS_PER_SECOND = 20;
    // the number of milliseconds in a second - constant
    public static final int MILLISECONDS_PER_SECOND = 1000;
    // the number of milliseconds in a tick - assuming the server runs at a perfect tick rate
    public static final int MILLISECONDS_PER_TICK = MILLISECONDS_PER_SECOND / TICKS_PER_SECOND;

    /**
     * Converts a duration in a certain unit of time to ticks.
     *
     * <p><code>Ticks.from(duration)</code> returns the number of ticks <b>from</b> the given duration.</p>
     *
     * @param duration the duration of time
     * @param unit the unit the duration is in
     * @return the number of ticks which represent the duration
     */
    public static long from(long duration, @NotNull TimeUnit unit) {
        return unit.toMillis(duration) / MILLISECONDS_PER_TICK;
    }

    /**
     * Converts a duration in a certain unit of time to ticks.
     *
     * <p><code>Ticks.from(duration)</code> returns the number of ticks <b>from</b> the given duration.</p>
     *
     * @param duration the duration of time
     * @param unit the unit the duration is in
     * @return the number of ticks which represent the duration
     */
    public static long from(double duration, @NotNull TimeUnit unit) {
        return (long) (convertDuration(duration, unit, TimeUnit.MILLISECONDS) / MILLISECONDS_PER_TICK);
    }

    /**
     * Converts ticks to a duration in a certain unit of time.
     *
     * <p><code>Ticks.to(ticks)</code> converts the number of ticks <b>to</b> a duration.</p>
     *
     * @param ticks the number of ticks
     * @param unit the unit to return the duration in
     * @return a duration value in the given unit, representing the number of ticks
     */
    public static long to(long ticks, @NotNull TimeUnit unit) {
        return unit.convert(ticks * MILLISECONDS_PER_TICK, TimeUnit.MILLISECONDS);
    }

    /**
     * Converts a double duration in a certain unit of time to another unit.
     *
     * @param duration the duration of time
     * @param from the unit the duration is in
     * @param to the unit the duration will be convert to
     * @return the duration that has been converted.
     */
    public static double convertDuration(double duration, TimeUnit from, TimeUnit to) {
        // is from or to the larger unit?
        if (from.ordinal() == to.ordinal()) {
            return duration;
        } else if (from.ordinal() < to.ordinal()) { // from is smaller
            return duration / from.convert(1, to);
        } else {
            return duration * to.convert(1, from);
        }
    }

    private Ticks() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

}
