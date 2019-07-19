package com.fitlogga.app.models.exercises;

public class FreeWeightExercise extends TimerExercise {


    /**
     * Builder class for instantiating FreeWeightExercise objects.
     * Builder is used because of the many required parameters.
     */
    public static class Builder {

        /*
         it may seem like there's a lot of duplicate code from RepetitionExercise, but
         we can't extend the builder class because of conflicting build() names.
          */

        private String name;
        private String description;
        private int amountOfSets;
        private int amountOfRepetitions;
        private int amountOfWeight;
        private String amountOfWeightUnits;
        private int restTimeBetweenSets;
        private boolean completed;
        private String uuid;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setNumberOfSets(int amountOfSets) {
            this.amountOfSets = amountOfSets;
            return this;
        }

        public Builder setNumberOfReps(int amountOfReps) {
            this.amountOfRepetitions = amountOfReps;
            return this;
        }

        public Builder setAmountOfWeight(int amountOfWeight) {
            this.amountOfWeight = amountOfWeight;
            return this;
        }

        public Builder setAmountOfWeightUnits(String units) {
            this.amountOfWeightUnits = units;
            return this;
        }

        public Builder setRestTimeBetweenSets(int restTimeBetweenSets) {
            this.restTimeBetweenSets = restTimeBetweenSets;
            return this;
        }

        public Builder setCompleted(boolean completed) {
            this.completed = completed;
            return this;
        }

        public Builder setUuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public FreeWeightExercise build() throws NullPointerException {
            validateProperties();
            return new FreeWeightExercise(name, description, amountOfSets,
                    amountOfRepetitions, amountOfWeight, amountOfWeightUnits, restTimeBetweenSets,
                    uuid);

        }

        private void validateProperties() {
            if (name == null) {
                throw new NullPointerException("A name must be provided.");
            }
            if (amountOfSets == 0) {
                throw new NullPointerException("Number of sets can not be null/0");
            }
            if (amountOfRepetitions == 0) {
                throw new NullPointerException("Number of repetitions can not be null/0");
            }
            if (restTimeBetweenSets == 0) {
                throw new NullPointerException("Rest time in between sets can not be null/0");
            }
            if (amountOfWeight == 0) {
                throw new NullPointerException("Amount of weight can not be null/0");
            }
            if (amountOfWeightUnits == null) {
                throw new NullPointerException("No weight unit was specified");
            }
        }


    }

    private String name;
    private String description;
    private int numberOfSets;
    private int numSetsCompleted;
    private int numberOfRepetitions;
    private int amountOfWeight;
    private String amountOfWeightUnits;
    private int restTimeBetweenSets;

    FreeWeightExercise(String name, String description, int numberOfSets,
                               int numberOfRepetitions, int amountOfWeight,
                               String amountOfWeightUnits, int restTimeInBetweenSets, String uuid) {
        super(ExerciseType.FREE_WEIGHT_EXERCISE, restTimeInBetweenSets * 1000, uuid);
        this.name = name;
        this.description = description;
        this.numberOfSets = numberOfSets;
        this.numberOfRepetitions = numberOfRepetitions;
        this.amountOfWeight = amountOfWeight;
        this.amountOfWeightUnits = amountOfWeightUnits;
        this.restTimeBetweenSets = restTimeInBetweenSets;
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

    public int getNumSetsCompleted() {
        return numSetsCompleted;
    }

    public int getNumberOfRepetitions() {
        return numberOfRepetitions;
    }

    public int getAmountOfWeight() {
        return amountOfWeight;
    }

    public String getAmountOfWeightUnits() {
        return amountOfWeightUnits;
    }

    public int getRestTimeBetweenSets() {
        return restTimeBetweenSets;
    }

    public void setNumSetsCompleted(int numSetsCompleted) {
        this.numSetsCompleted = numSetsCompleted;
    }

    public void setAmountOfWeight(int amountOfWeight) {
        this.amountOfWeight = amountOfWeight;
    }
}
