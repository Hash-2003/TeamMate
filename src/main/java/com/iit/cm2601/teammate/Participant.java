package com.iit.cm2601.teammate;

public class Participant {
    private String id;
    private String name;
    private String email;
    private GameType preferredGame;
    private int skillLevel;
    private RoleType preferredRole;
    private int personalityScore;
    private PersonalityType personalityType;

    public Participant(String id,
                       String name,
                       String email,
                       GameType preferredGame,
                       int skillLevel,
                       RoleType preferredRole,
                       int personalityScore,
                       PersonalityType personalityType) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.preferredGame = preferredGame;
        this.skillLevel = skillLevel;
        this.preferredRole = preferredRole;
        this.personalityScore = personalityScore;
        this.personalityType = personalityType;

    }

}
