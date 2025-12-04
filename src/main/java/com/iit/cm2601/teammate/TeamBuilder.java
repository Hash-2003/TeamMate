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
    private final int baseGameCap = 2;

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

    private void seedLeaders() {
        if (leaders.isEmpty()) {
            return;
        }

        for (Participant leader : leaders) {

            Team bestTeam = null;
            int bestTotalSkill = Integer.MAX_VALUE;

            for (Team team : teams) {
                if (team.isFull()) {
                    continue;
                }

                int leaderCountInTeam = team.getPersonalityCount(PersonalityType.LEADER);
                if (leaderCountInTeam > 0) {
                    continue;
                }

                GameType game = leader.getPreferredGame();
                int currentGameCount = team.getGameCount(game);
                if (currentGameCount >= baseGameCap) {
                    continue;
                }

                if (team.getTotalSkill() < bestTotalSkill) {
                    bestTotalSkill = team.getTotalSkill();
                    bestTeam = team;
                }
            }

            if (bestTeam != null) {
                bestTeam.addMember(leader);
            }
        }
    }

    private boolean isAlreadyAssigned(Participant p) {
        for (Team team : teams) {
            if (team.getMembers().contains(p)) {
                return true;
            }
        }
        return false;
    }

    private int maxThinkersForCapacity(int capacity) {
        if (capacity <= 5) {
            return 2;    // small
        } else if (capacity <= 7) {
            return 3;    // medium
        } else {
            return 4;    // large
        }
    }

    private void seedThinkers() {
        if (thinkers.isEmpty()) {
            return;
        }
        for (Participant thinker : thinkers) {

            Team bestTeam = null;
            int bestThinkerCount = Integer.MAX_VALUE;
            int bestTotalSkill = Integer.MAX_VALUE;

            for (Team team : teams) {
                if (team.isFull()) {
                    continue;
                }

                int currentThinkers = team.getPersonalityCount(PersonalityType.THINKER);
                int maxAllowed = maxThinkersForCapacity(team.getTargetCapacity());

                if (currentThinkers >= maxAllowed) {
                    continue;
                }

                GameType game = thinker.getPreferredGame();
                if (team.getGameCount(game) >= baseGameCap) {
                    continue;
                }


                if (currentThinkers < bestThinkerCount) {
                    bestThinkerCount = currentThinkers;
                    bestTotalSkill = team.getTotalSkill();
                    bestTeam = team;
                }

                else if (currentThinkers == bestThinkerCount &&
                        team.getTotalSkill() < bestTotalSkill) {
                    bestTotalSkill = team.getTotalSkill();
                    bestTeam = team;
                }
            }

            if (bestTeam != null) {
                bestTeam.addMember(thinker);
            }
        }
    }

    public List<Team> formTeams() {
        initializeTeamsWithCapacities();
        initializeTeamsWithCapacities();
        bucketParticipantsByPersonality();
        computeGlobalSkillStatistics();

        seedLeaders();
        seedThinkers();

        System.out.println("Leaders: " + leaders.size());
        System.out.println("Thinkers: " + thinkers.size());
        System.out.println("Balanced: " + balanced.size());
        System.out.println("Global average skill: " + globalAvgSkill);

        for (Team t : teams) {
            int leaderCount = t.getPersonalityCount(PersonalityType.LEADER);
            System.out.println("Team " + t.getTeamId()
                    + " -> leaders=" + leaderCount
                    +" | thinkers=" + t.getPersonalityCount(PersonalityType.THINKER)
                    + ", size=" + t.getCurrentSize()
                    +" | cap=" + t.getTargetCapacity()
            );
        }
        //developments will be added

        return teams;
    }

}
