package it.unibo.inner.impl;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import it.unibo.inner.api.IterableWithPolicy;
import it.unibo.inner.api.Predicate;

public class IterableWithPolicyImpl<T> implements IterableWithPolicy<T> {
    private T[] array;
    private Predicate<T> iterationPolicy;

    /**
     * Creates new iterable of a T array 
     * @param e
     *          the array to iterate
     */
    public IterableWithPolicyImpl(final T[] e) {
        this(e, new Predicate<T>() {
            @Override
            public boolean test(final T elem) {
                return true;
            }
        });
    }

    /**
     * Creates new iterable of a T array
     * @param e 
     *          the array to iterate
     * @param predicate
     *          iteration policy
     */
    public IterableWithPolicyImpl(final T[] e, final Predicate<T> predicate) {
        this.array = Arrays.copyOf(e, e.length);
        this.iterationPolicy = predicate;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Iterator<T> iterator() {
        return new IteratorImpl(this.array);
    }
    
    /**
     * @inheritDoc
     */
    @Override
    public void setIterationPolicy(final Predicate<T> filter) {
        this.iterationPolicy = filter;
    }

    @Override
    public String toString() {
        return Arrays.toString(array);
    }

    class IteratorImpl implements Iterator<T> {
        private int currentIndex;
        private T[] array;

        /**
         * @param array
         *              Array to iterate
         */

        public IteratorImpl(final T[] array) {
            this.array = array;
            this.currentIndex = 0;
        }

        @Override
        public boolean hasNext() {
            for (; currentIndex < this.array.length; currentIndex++) {
                if (iterationPolicy.test(array[currentIndex])) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public T next() {
            while (this.hasNext()) {
                if (iterationPolicy.test(this.array[currentIndex])) {
                    return this.array[currentIndex++];
                }
            }
            throw new NoSuchElementException();
        }
    }
}
