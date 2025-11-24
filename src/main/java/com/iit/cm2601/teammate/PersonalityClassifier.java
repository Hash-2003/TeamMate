package com.iit.cm2601.teammate;

public class PersonalityClassifier {

    public PersonalityType classify(int score) {

        if (score < 20 || score > 100) {
            throw new IllegalArgumentException(
                    "Personality score out of expected range (20–100): " + score
            );
        }


        if (score >= 90) {
            return PersonalityType.LEADER;
        } else if (score >= 70) {
            return PersonalityType.BALANCED;
        } else {
            return PersonalityType.THINKER;
        }
    }
}

