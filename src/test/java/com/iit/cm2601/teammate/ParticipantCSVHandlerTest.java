package com.iit.cm2601.teammate;

import com.iit.cm2601.teammate.exceptions.InvalidParticipantDataException;
import com.iit.cm2601.teammate.exceptions.ParticipantFileException;

import java.util.List;

public class ParticipantCSVHandlerTest {

    public static void main(String[] args) {

        String csvPath = "src/main/resources/com/iit/cm2601/teammate/data/participant_details.csv";

        ParticipantCSVHandler handler = new ParticipantCSVHandler();

        try {
            List<Participant> participants = handler.loadParticipants(csvPath);

            System.out.println("Loaded " + participants.size() + " participants.");


            participants.stream()
                    .limit(3)
                    .forEach(p -> System.out.println(p.getId() + " | " + p.getName() + " | " + p.getPreferredGame() + " | " + p.getPreferredRole() + " | " + p.getPersonalityType()));

        } catch (ParticipantFileException | InvalidParticipantDataException e) {
            System.out.println("Error while loading participants:");
            e.printStackTrace();
        }
    }
}