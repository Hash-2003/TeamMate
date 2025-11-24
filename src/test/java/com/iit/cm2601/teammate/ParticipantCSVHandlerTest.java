package com.iit.cm2601.teammate;

import com.iit.cm2601.teammate.exceptions.InvalidParticipantDataException;
import com.iit.cm2601.teammate.exceptions.ParticipantFileException;

import java.util.List;
import java.util.ArrayList;

public class ParticipantCSVHandlerTest {

    public static void main(String[] args) {

        String csvPath = "src/main/resources/com/iit/cm2601/teammate/data/participant_details.csv";

        ParticipantCSVHandler handler = new ParticipantCSVHandler();

        try {
            List<Participant> participants = handler.loadParticipants(csvPath);

            System.out.println("Loaded " + participants.size() + " participants.");


            participants.stream()
                    .limit(3)
                    .forEach(p -> System.out.println(p.getId() + " | " + p.getName() + " | " + p.getEmail() + " | " + p.getPreferredGame() + " | " + p.getSkillLevel() + " | " + p.getPreferredRole() + " | " +p.getPersonalityScore() + " | " + p.getPersonalityType()));

        } catch (ParticipantFileException | InvalidParticipantDataException e) {
            System.out.println("Error while loading participants:");
            e.printStackTrace();
        }
    }

    public static class ParticipantCSVHandlerSaveTest {

        public static void main(String[] args) {
            List<Participant> participants = new ArrayList<>();

            participants.add(new Participant(
                    "P001",
                    "Amal Kamal",
                    "amal@example.com",
                    GameType.CS_GO,
                    8,
                    RoleType.ATTACKER,
                    92,
                    PersonalityType.LEADER
            ));

            participants.add(new Participant(
                    "P002",
                    "Nimal Kamal",
                    "nimal@example.com",
                    GameType.DOTA_2,
                    6,
                    RoleType.DEFENDER,
                    78,
                    PersonalityType.BALANCED
            ));

            participants.add(new Participant(
                    "P003",
                    "Jamal Kamal",
                    "jamal@example.com",
                    GameType.CHESS,
                    5,
                    RoleType.STRATEGIST,
                    65,
                    PersonalityType.THINKER
            ));


            String testCsvPath = "src/main/resources/com/iit/cm2601/teammate/data/test_participants.csv";

            ParticipantCSVHandler handler = new ParticipantCSVHandler();

            try {
                handler.saveParticipantsToCsv(participants, testCsvPath);
                System.out.println("Saved participants to: " + testCsvPath);

                List<Participant> loaded = handler.loadParticipants(testCsvPath);
                System.out.println("Loaded " + loaded.size() + " participants from saved file.");

                for (Participant p : loaded) {
                    System.out.println(
                            p.getId() + " | " +
                                    p.getName() + " | " +
                                    p.getEmail() + " | " +
                                    p.getPreferredGame() + " | " +
                                    p.getPreferredRole() + " | " +
                                    p.getPersonalityType()
                    );
                }

            } catch (ParticipantFileException | InvalidParticipantDataException e) {
                System.out.println("Error during save/load test:");
                e.printStackTrace();
            }
        }
    }

}