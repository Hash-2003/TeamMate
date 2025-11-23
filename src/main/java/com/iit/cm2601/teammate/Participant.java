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

    //Getters
    public String getId() {return id;}
    public String getName() {return name;}
    public String getEmail() {return email;}
    public GameType getPreferredGame() {return preferredGame;}
    public int getSkillLevel() {return skillLevel;}
    public RoleType getPreferredRole() {return preferredRole;}
    public int getPersonalityScore() {return personalityScore;}
    public PersonalityType getPersonalityType() {return personalityType;}

    //Setters
    public void setId(String id) {this.id = id;}
    public void setName(String name) {this.name = name;}
    public void setEmail(String email) {this.email = email;}
    public void setPreferredGame(GameType preferredGame) {this.preferredGame = preferredGame;}
    public void setSkillLevel(int skillLevel) {this.skillLevel = skillLevel;}
    public void setPreferredRole(RoleType preferredRole) {this.preferredRole = preferredRole;}
    public void setPersonalityScore(int personalityScore) {this.personalityScore = personalityScore;}
    public void setPersonalityType(PersonalityType personalityType) {this.personalityType = personalityType;}
}
