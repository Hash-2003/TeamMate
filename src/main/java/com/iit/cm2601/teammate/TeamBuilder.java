package com.iit.cm2601.teammate;

import java.util.List;
import java.util.ArrayList;

public class TeamBuilder {
    private final List<Participant> allParticipants;
    private final int targetTeamSize;

    private final List<Team> teams = new ArrayList<>();

    private final List<Participant> leaders   = new ArrayList<>();
    private final List<Participant> thinkers  = new ArrayList<>();
    private final List<Participant> balanced  = new ArrayList<>();

    private double globalAvgSkill = 0.0;

    public TeamBuilder(List<Participant> allParticipants, int targetTeamSize) {
        this.allParticipants = new ArrayList<>(allParticipants);
        this.targetTeamSize = targetTeamSize;
    }

    private void initializeTeamsWithCapacities() {
        teams.clear();

        int N = allParticipants.size();
        if (N == 0) {
            return;
        }

        int T = (int) Math.ceil((double) N / targetTeamSize);

        double base = (double) N / T;
        int smallSize = (int) Math.floor(base);
        int bigSize = smallSize + 1;

        int k = N - (smallSize * T);

        for (int i = 0; i < T; i++) {
            int capacity;
            if (i < k) {
                capacity = bigSize;
            } else {
                capacity = smallSize;
            }
            Team team = new Team(i + 1, capacity); // teamId = 1-based
            teams.add(team);
        }
    }

    private void bucketParticipantsByPersonality() {
        leaders.clear();
        thinkers.clear();
        balanced.clear();

        for (Participant p : allParticipants) {
            PersonalityType type = p.getPersonalityType();
            if (type == PersonalityType.LEADER) {
                leaders.add(p);
            } else if (type == PersonalityType.THINKER) {
                thinkers.add(p);
            } else {
                balanced.add(p);
            }
        }

        java.util.Collections.shuffle(leaders);
        java.util.Collections.shuffle(thinkers);
        java.util.Collections.shuffle(balanced);
    }

    private void computeGlobalSkillStatistics() {
        if (allParticipants.isEmpty()) {
            globalAvgSkill = 0.0;
            return;
        }

        int totalSkill = 0;
        for (Participant p : allParticipants) {
            totalSkill += p.getSkillLevel();
        }

        globalAvgSkill = (double) totalSkill / allParticipants.size();
    }

    public List<Team> formTeams() {
        initializeTeamsWithCapacities();
        initializeTeamsWithCapacities();
        bucketParticipantsByPersonality();
        computeGlobalSkillStatistics();

        System.out.println("Leaders: " + leaders.size());
        System.out.println("Thinkers: " + thinkers.size());
        System.out.println("Balanced: " + balanced.size());
        System.out.println("Global average skill: " + globalAvgSkill);
        //developments will be added

        return teams;
    }

}
