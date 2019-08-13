package com.fitlogga.app.models;

import java.util.ArrayList;
import java.util.Collection;

public class ObservedList<E extends Object> extends ArrayList<E> {

    private boolean changesMade = false;

    public ObservedList(int initialCapacity) {
        super(initialCapacity);
    }

    public ObservedList() {
    }

    public ObservedList(Collection<? extends E> c) {
        super(c);
    }

    @Override
    public E remove(int index) {
        this.changesMade = true;
        return super.remove(index);
    }

    @Override
    public E set(int index, E element) {
        this.changesMade = true;
        return super.set(index, element);
    }

    @Override
    public boolean add(E e) {
        this.changesMade = true;
        return super.add(e);
    }

    public boolean wereChangesMade() {
        return changesMade;
    }
}
