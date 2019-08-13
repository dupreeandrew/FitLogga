package com.fitlogga.app.models.plan;

import java.util.ArrayList;
import java.util.Collection;

public class DailyRoutine<E extends Object> extends ArrayList<E> {

    private transient boolean changesMade = false;


    public DailyRoutine(int initialCapacity) {
        super(initialCapacity);
    }

    public DailyRoutine() {
    }

    public DailyRoutine(Collection<? extends E> c) {
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
