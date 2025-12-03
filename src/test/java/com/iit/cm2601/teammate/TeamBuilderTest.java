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

            int teamSize = 5; // try 3, 4, 5, 6, etc.

            TeamBuilder builder = new TeamBuilder(participants, teamSize);
            List<Team> teams = builder.formTeams();

            System.out.println("Created " + teams.size()
                    + " teams (target team size = " + teamSize + ").");

            int sumCapacities = 0;
            for (Team t : teams) {
                sumCapacities += t.getTargetCapacity();
                System.out.println(
                        "Team " + t.getTeamId()
                                + " -> targetCapacity = " + t.getTargetCapacity()
                                + ", currentSize = " + t.getCurrentSize()
                );
            }

            System.out.println("Sum of team capacities = " + sumCapacities
                    + " (participants = " + participants.size() + ")");


        } catch (ParticipantFileException | InvalidParticipantDataException e) {
            System.out.println("Error while loading participants:");
            e.printStackTrace();
        }
    }
}
