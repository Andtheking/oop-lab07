package it.unibo.nestedenum;

import java.util.Comparator;
import java.util.List;
import it.unibo.functional.api.Function;
import it.unibo.functional.Transformers;

/**
 * Implementation of {@link MonthSorter}.
 */
public final class MonthSorterNested implements MonthSorter {

    private enum Month {
        JANUARY(31),
        FEBRUARY(28),
        MARCH(31),
        APRIL(30),
        MAY(31),
        JUNE(30),
        JULY(31),
        AUGUST(31),
        SEPTEMBER(30),
        OCTOBER(31),
        NOVEMBER(30),
        DECEMBER(31);

        /**
         * Number of days of the month
         */
        public int days;
        
        private Month(int day) {
            this.days = day;
        }

        /**
         * Get {@link Month} value from string
         * @param s
         *          the name of the {@link Month}
         * @return
         *          the value of the {@link Month}
         */
        public static Month fromString(final String s) {
            List<Month> m = Transformers.select(List.of(Month.values()), new Function<Month,Boolean>() {
                @Override
                public Boolean call(Month input) {
                    return input.toString().toLowerCase().startsWith(s.toLowerCase());
                }
            });
            if (m.size() == 1) {
                return m.getFirst();
            }
            final String error_message = m.size() > 1
                ? "Ambigous Month \"" + s + "\"" 
                : "Month \""+ s + "\"not found.";
            throw new IllegalArgumentException(error_message);            
        }
    }

    /** 
     * @inheritDoc
     */
    @Override
    public Comparator<String> sortByDays() {
        return new SortByDate();
    }

    /**
     * @inheritDoc
     */
    @Override
    public Comparator<String> sortByOrder() {
        return new SortByMonthOrder();
    }

    private class SortByMonthOrder implements Comparator<String> {
        @Override
        public int compare(String arg0, String arg1) {
            return Month.fromString(arg0).compareTo(Month.fromString(arg1));
        }
    }

    private class SortByDate implements Comparator<String> {
        @Override
        public int compare(String arg0, String arg1) {
            return Integer.compare(Month.fromString(arg0).days, Month.fromString(arg1).days);
        }

    }
}
