package com.fitlogga.app.models.plan;

import com.fitlogga.app.models.exercises.Exercise;

import java.util.ArrayList;
import java.util.Collection;

public class DailyRoutine {

    public static class Exercises extends ArrayList<Exercise> {

        private transient boolean startObserving = false;
        private transient boolean changesMade = false;

        public Exercises(int initialCapacity) {
            super(initialCapacity);
        }

        public Exercises() {
        }

        public Exercises(Collection<? extends Exercise> c) {
            super(c);
        }

        @Override
        public Exercise remove(int index) {
            if (startObserving) {
                this.changesMade = true;
            }
            return super.remove(index);
        }

        @Override
        public Exercise set(int index, Exercise element) {
            if (startObserving) {
                this.changesMade = true;
            }
            return super.set(index, element);
        }

        @Override
        public boolean add(Exercise e) {
            if (startObserving) {
                this.changesMade = true;
            }
            return super.add(e);
        }

        public boolean wereChangesMade() {
            return this.changesMade;
        }

        public void setStartObserving(boolean startObserving) {
            this.startObserving = startObserving;
        }
    }


    private String name = "";
    private Exercises exercises;

    
    public DailyRoutine() {
        this.exercises = new Exercises();
    }

    public DailyRoutine(String name) {
        this.name = name;
        this.exercises = new Exercises();
    }

    public DailyRoutine(Exercises exercises) {
        this.exercises = exercises;
    }

    public DailyRoutine(String name, Exercises exercises) {
        this.name = name;
        this.exercises = exercises;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Exercises getExercises() {
        return exercises;
    }
}
