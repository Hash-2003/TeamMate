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

            int teamSize = 10; //team size

            TeamBuilder algo =
                    new TeamBuilder(participants, teamSize);

            List<Team> teams = algo.formTeams();

            System.out.println("Created " + teams.size() + " teams (target size " + teamSize + ").");

            int sumCapacities = 0;
            for (Team t : teams) {
                sumCapacities += t.getTargetCapacity();
                System.out.println(t.toString());
            }

            System.out.println("Sum of team capacities = " + sumCapacities +
                    " (should be >= number of participants: " + participants.size() + ")");

        } catch (ParticipantFileException | InvalidParticipantDataException e) {
            System.out.println("Error while loading participants:");
            e.printStackTrace();
        }
    }
}
