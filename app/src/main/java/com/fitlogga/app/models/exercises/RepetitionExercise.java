package com.fitlogga.app.models.exercises;

public class RepetitionExercise extends Exercise {

    /**
     * Builder class for instantiating RepetitionExercise objects.
     * Builder is used because of the many required parameters.
     */
    public static class Builder {

        private String name;
        private String description;
        private int numberOfSets;
        private int numberOfRepetitions;
        private int restTimeBetweenSets;
        private boolean completed;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setNumberOfSets(int numberOfSets) {
            this.numberOfSets = numberOfSets;
            return this;
        }

        public Builder setNumberOfReps(int numberOfReps) {
            this.numberOfRepetitions = numberOfReps;
            return this;
        }

        /**
         * @param restTimeBetweenSets rest time between each set, in SECONDS.
         */
        public Builder setRestTimeBetweenSets(int restTimeBetweenSets) {
            this.restTimeBetweenSets = restTimeBetweenSets;
            return this;
        }

        public Builder setCompleted(boolean completed) {
            this.completed = completed;
            return this;
        }

        public RepetitionExercise build() throws NullPointerException {
            validateProperties();
            return new RepetitionExercise(name, description, numberOfSets,
                    numberOfRepetitions, restTimeBetweenSets, completed);

        }


        private void validateProperties() {
            if (name == null) {
                throw new NullPointerException("A name must be provided.");
            }
            if (numberOfSets == 0) {
                throw new NullPointerException("Number of sets can not be null/0");
            }
            if (numberOfRepetitions == 0) {
                throw new NullPointerException("Number of repetitions can not be null/0");
            }
            if (restTimeBetweenSets == 0) {
                throw new NullPointerException("Rest time in between sets can not be null/0");
            }
        }



    }

    private String name;
    private String description;
    private int numberOfSets;
    private int numberOfRepetitions;
    private int restTimeBetweenSets;
    private transient int numSetsCompleted = 0;

    RepetitionExercise(String name, String description, int numberOfSets,
                               int numberOfRepetitions, int restTimeBetweenSets) {
        super(ExerciseType.REPETITION_EXERCISE);
        this.name = name;
        this.description = description;
        this.numberOfSets = numberOfSets;
        this.numberOfRepetitions = numberOfRepetitions;
        this.restTimeBetweenSets = restTimeBetweenSets;
    }

    public RepetitionExercise(String name, String description, int numberOfSets, int numberOfRepetitions, int restTimeBetweenSets, boolean completed) {
        this(name, description, numberOfSets, numberOfRepetitions, restTimeBetweenSets);
        setCompleted(true);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getNumberOfSets() {
        return numberOfSets;
    }

    public int getNumberOfRepetitions() {
        return numberOfRepetitions;
    }

    public int getRestTimeBetweenSets() {
        return restTimeBetweenSets;
    }

    public int getNumSetsCompleted() {
        return numSetsCompleted;
    }

    public void setNumSetsCompleted(int numSetsCompleted) {
        this.numSetsCompleted = numSetsCompleted;
    }
}