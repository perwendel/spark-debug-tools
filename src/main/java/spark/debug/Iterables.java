package spark.debug;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.google.common.base.Optional;

/**
 * Provides utility functions for efficiently wrapping Iterable objects.
 * Note: Some of these are provided in Guava as well.
 */
public class Iterables {
    @FunctionalInterface
    public static interface Filter<T> {
        /**
         * @param item An item.
         * @return True if item should be included in the output, false otherwise.
         */
        public boolean matches(T item);
    }

    @FunctionalInterface
    public static interface Mapper<T, S> {
        /**
         * @param item An item.
         * @return The item produced by executing the map function.
         */
        public S map(T item);
    }

    @FunctionalInterface
    public static interface Reducer<T, S> {
        public S reduce(S accumulator, T item);
    }

    /**
     * @param input  An input iterable.
     * @param filter A filtering function.
     * @return An iterable that provides a view of input which only contains items matching filter.
     */
    public static <T> Iterable<T> filter(Iterable<T> input, Filter<T> filter) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return new FilterIterator<T>(input.iterator(), filter);
            }
        };
    }

    /**
     * Implements a filter iterator.
     *
     * @param <T> Value type.
     */
    private static final class FilterIterator<T> implements Iterator<T> {
        private final Iterator<T> input;
        private final Filter<T> filter;
        private boolean hasNext = false;
        private T next = null;

        public FilterIterator(Iterator<T> input, Filter<T> filter) {
            this.input = input;
            this.filter = filter;
            advance();
        }

        private void advance() {
            while (input.hasNext()) {
                hasNext = true;
                next = input.next();

                if (filter.matches(next)) {
                    return;
                } else {
                    next = null;
                    hasNext = false;
                }
            }

            next = null;
            hasNext = false;
        }

        @Override
        public boolean hasNext() {
            return hasNext;
        }

        @Override
        public T next() {
            T value = next;
            advance();
            return value;
        }
    }

    /**
     * @param input  An input Iterable.
     * @param mapper A map function.
     * @return A view of input which is obtained by executing the map function on all values in input.
     */
    public static <T, S> Iterable<S> map(Iterable<T> input, Mapper<T, S> mapper) {
        return new Iterable<S>() {
            @Override
            public Iterator<S> iterator() {
                return new MapIterator<T, S>(input.iterator(), mapper);
            }
        };
    }

    /**
     * Implements a map iterator.
     *
     * @param <T> Original value type.
     * @param <S> Mapped value type.
     */
    private static final class MapIterator<T, S> implements Iterator<S> {
        private final Iterator<T> input;
        private final Mapper<T, S> mapper;

        public MapIterator(Iterator<T> input, Mapper<T, S> mapper) {
            this.input = input;
            this.mapper = mapper;
        }

        @Override
        public boolean hasNext() {
            return input.hasNext();
        }

        @Override
        public S next() {
            return mapper.map(input.next());
        }
    }

    /**
     * @param input  An input list.
     * @param mapper A map function.
     * @return The input list of type List<S> (modified in-place from List<T>).
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T, S> List<S> mapList(List<T> input, Mapper<T, S> mapper) {
        ListIterator iterator = input.listIterator();

        while (iterator.hasNext()) {
            iterator.set(mapper.map((T) iterator.next()));
        }

        return (List<S>) input;
    }

    /**
     * @param collection   An iterable.
     * @param initialValue An initial value.
     * @param reducer      A reduction function.
     * @return The reduced value.
     */
    public static <T, S> S reduce(Iterable<T> collection, S initialValue, Reducer<T, S> reducer) {
        S value = initialValue;

        for (T item : collection) {
            value = reducer.reduce(value, item);
        }

        return value;
    }

    /**
     * @param collection An iterable.
     * @param filter     A filter.
     * @return The first item matching the filter (if any).
     */
    public static <T> Optional<T> first(Iterable<T> collection, Filter<T> filter) {
        for (T item : collection) {
            if (filter.matches(item)) {
                return Optional.of(item);
            }
        }

        return Optional.absent();
    }

    /**
     * @param collection An iterable (non-empty).
     * @return The first item in the given iterable.
     */
    public static <T> T first(Iterable<T> collection) {
        for (T item : collection) {
            return item;
        }

        throw new IllegalArgumentException("Provided iterable was empty.");
    }
}
