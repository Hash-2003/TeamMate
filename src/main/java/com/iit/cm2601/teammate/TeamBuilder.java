package com.iit.cm2601.teammate;

import java.util.*;

public class TeamBuilder {
    private final List<Participant> allParticipants;
    private final int targetTeamSize;

    private final List<Team> teams = new ArrayList<>();

    private final List<Participant> leaders   = new ArrayList<>();
    private final List<Participant> thinkers  = new ArrayList<>();
    private final List<Participant> balanced  = new ArrayList<>();

    private double globalAvgSkill = 0.0;
    private final int baseGameCap = 2;

    private static final double WGame  = 5.0;
    private static final double WRole  = 4.0;
    private static final double WPres  = 3.0;
    private static final double WSkill = 1.0;

    private static final double BigPanelty = 1000.0;

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

        Collections.shuffle(leaders);
        Collections.shuffle(thinkers);
        Collections.shuffle(balanced);
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

    private List<Participant> collectUnassignedParticipants() {
        List<Participant> remaining = new ArrayList<>();

        for (Participant p : allParticipants) {
            if (!isAlreadyAssigned(p)) {
                remaining.add(p);
            }
        }

        return remaining;
    }

    private double computeGamePenalty(Participant p, Team t) {
        GameType game = p.getPreferredGame();
        int count = t.getGameCount(game);

        if (count >= baseGameCap) {
            return BigPanelty;
        }
        return 0.0;
    }

    private double computeRolePenalty(Participant p, Team t) {
        int current = t.getDistinctRoleCount();
        int after = t.getDistinctRoleCountWith(p.getPreferredRole());

        int cap = t.getTargetCapacity();
        int target = (cap <= 5) ? 3 : 4;

        if (after > current) return 0.0;

        if (t.getCurrentSize() >= cap - 1 && current < target) {
            return 5.0;
        }

        return 1.0;
    }

    private double computePersonalityPenalty(Participant p, Team t) {
        int L = t.getPersonalityCount(PersonalityType.LEADER);
        int Th = t.getPersonalityCount(PersonalityType.THINKER);

        PersonalityType type = p.getPersonalityType();
        if (type == PersonalityType.LEADER) L++;
        else if (type == PersonalityType.THINKER) Th++;

        int cap = t.getTargetCapacity();

        int maxTh = maxThinkersForCapacity(cap);

        double penalty = 0.0;

        if (L == 0) penalty += 3.0;
        if (L > 2) penalty += 5.0;

        // Thinker penalties
        if (Th == 0) penalty += 2.0;
        if (Th > maxTh) penalty += 4.0;

        return penalty;
    }

    private double computeSkillPenalty(Participant p, Team t) {
        double newAvg = (t.getTotalSkill() + p.getSkillLevel())
                / (double) (t.getCurrentSize() + 1);

        return Math.abs(newAvg - globalAvgSkill);
    }

    private double scorePlacement(Participant p, Team t) {
        double gamePenalty = computeGamePenalty(p, t);
        double rolePenalty = computeRolePenalty(p, t);
        double persPenalty = computePersonalityPenalty(p, t);
        double skillPenalty = computeSkillPenalty(p, t);

        return WGame * gamePenalty
                + WRole * rolePenalty
                + WPres * persPenalty
                + WSkill * skillPenalty;
    }

    private void assignRemainingParticipants() {

        List<Participant> remaining = collectUnassignedParticipants();

        remaining.sort((a, b) -> Integer.compare(b.getSkillLevel(), a.getSkillLevel()));

        for (Participant p : remaining) {

            Team bestTeam = null;
            double bestScore = Double.MAX_VALUE;
            List<Team> bestTeamsList = new ArrayList<>();

            for (Team t : teams) {

                if (t.isFull()) continue;

                double score = scorePlacement(p, t);

                if (score < bestScore) {
                    bestScore = score;
                    bestTeamsList.clear();
                    bestTeamsList.add(t);
                }
                else if (Math.abs(score - bestScore) < 1e-9) {
                    bestTeamsList.add(t);
                }
            }

            if (!bestTeamsList.isEmpty()) {
                Team chosen = bestTeamsList.get(
                        new Random().nextInt(bestTeamsList.size())
                );
                chosen.addMember(p);
            }
            else {
                System.out.println("Warning: No placement found for " + p.getId());
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
        assignRemainingParticipants();

        return teams;
    }

}
