package com.iit.cm2601.teammate;

import java.util.List;
import java.util.ArrayList;

public class TeamBuilder {
    private final List<Participant> allParticipants;
    private final int targetTeamSize;

    private final List<Team> teams = new ArrayList<>();

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

    public List<Team> formTeams() {
        initializeTeamsWithCapacities();
        // to be added: team assignment logic
        return teams;
    }

}
