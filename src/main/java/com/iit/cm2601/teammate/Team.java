package com.iit.cm2601.teammate;

import java.util.*;

public class Team {
    private final int teamId;
    private final int targetCapacity;
    private final List<Participant> members;
    private int totalSkill = 0;

    private final Map<GameType, Integer> gameCounts = new EnumMap<>(GameType.class);
    private final Map<RoleType, Integer> roleCounts = new EnumMap<>(RoleType.class);
    private final Map<PersonalityType, Integer> personalityCounts = new EnumMap<>(PersonalityType.class);

    public Team(int teamId, int targetCapacity){
        this.teamId = teamId;
        this.targetCapacity = targetCapacity;
        this.members = new ArrayList<>();

        for (GameType g : GameType.values()){
            gameCounts.put(g, 0);
        }
        for (RoleType r : RoleType.values()){
            roleCounts.put(r, 0);
        }
        for (PersonalityType p : PersonalityType.values()){
            personalityCounts.put(p, 0);
        }
    }
    public int getTeamId() {
        return teamId;
    }
    public int getTargetCapacity() {
        return targetCapacity;
    }
    public List<Participant> getMembers() {
        return Collections.unmodifiableList(members);
    }
    public int getCurrentSize() {
        return members.size();
    }
    public boolean isFull(){
        return members.size() >= targetCapacity;
    }
    public int getTotalSkill(){
        return totalSkill;
    }
    public double getAvgSkill(){
        if(members.size() == 0){
            return 0.0;
        }
        return (double)totalSkill / members.size();
    }

    public int getGameCount(GameType gameType) {
        return gameCounts.getOrDefault(gameType, 0);
    }
    public int getRoleCount(RoleType roleType) {
        return roleCounts.getOrDefault(roleType, 0);
    }
    public int getPersonalityCount(PersonalityType personalityType) {
        return personalityCounts.getOrDefault(personalityType, 0);
    }
    public int getDistinctRoleCount() {
        int count = 0;
        for (RoleType role : RoleType.values()) {
            if (getRoleCount(role) > 0) {
                count++;
            }
        }
        return count;
    }

    public int getDistinctRoleCountWith(RoleType newRole) {
        int distinct = getDistinctRoleCount();
        if (getRoleCount(newRole) == 0) {
            distinct++;
        }
        return distinct;
    }

    public void addMember(Participant participant) {
        members.add(participant);

        totalSkill += participant.getSkillLevel();

        GameType g = participant.getPreferredGame();
        gameCounts.put(g, gameCounts.getOrDefault(g, 0) + 1);

        RoleType r = participant.getPreferredRole();
        roleCounts.put(r, roleCounts.getOrDefault(r, 0) + 1);

        PersonalityType p = participant.getPersonalityType();
        personalityCounts.put(p, personalityCounts.getOrDefault(p, 0) + 1);
    }

    //swapping needs to be improved. Then remove method is not needed
/*
    public void removeMember(Participant participant) {
        if (members.remove(participant)) {

            totalSkill -= participant.getSkillLevel();

            GameType g = participant.getPreferredGame();
            gameCounts.put(g, gameCounts.getOrDefault(g, 0) - 1);

            RoleType r = participant.getPreferredRole();
            roleCounts.put(r, roleCounts.getOrDefault(r, 0) - 1);

            PersonalityType p = participant.getPersonalityType();
            personalityCounts.put(p, personalityCounts.getOrDefault(p, 0) - 1);
        }
    }
*/


    @Override
    public String toString() {
        return "Team " + teamId +
                " (size=" + getCurrentSize() +
                ", target=" + targetCapacity +
                ", avgSkill=" + String.format("%.2f", getAvgSkill()) +
                ")";
    }

}
