package com.iit.cm2601.teammate;

import com.iit.cm2601.teammate.exceptions.InvalidParticipantDataException;
import com.iit.cm2601.teammate.exceptions.ParticipantFileException;

import java.util.List;

public class TeamBuilderTest {

    public static void main(String[] args) {

        String csvPath = "src/main/resources/com/iit/cm2601/teammate/data/participant_details.csv";

        ParticipantCSVHandler handler = new ParticipantCSVHandler();

        try {
            List<Participant> participants = handler.loadParticipants(csvPath);
            System.out.println("Loaded " + participants.size() + " participants.");

            int teamSize = 3;

            TeamBuilder builder = new TeamBuilder(participants, teamSize);
            List<Team> teams = builder.formTeams();

            System.out.println("Created " + teams.size()
                    + " teams (target team size = " + teamSize + ").");

            int sumCapacities = 0;
            int totalAssigned = 0;
            for (Team t : teams) {
                sumCapacities += t.getTargetCapacity();
                totalAssigned += t.getCurrentSize();
                System.out.println("Team " + t.getTeamId()
                        + " -> cap=" + t.getTargetCapacity()
                        + ", size=" + t.getCurrentSize());
            }

            System.out.println("Sum of team capacities = " + sumCapacities
                    + " (should be >= participants: " + participants.size() + ")");
            System.out.println("Total assigned = " + totalAssigned
                    + " (should equal participants: " + participants.size() + ")");
            System.out.println("=== End External Check ===");

        } catch (ParticipantFileException | InvalidParticipantDataException e) {
            System.out.println("Error while loading participants:");
            e.printStackTrace();
        }
    }
}
