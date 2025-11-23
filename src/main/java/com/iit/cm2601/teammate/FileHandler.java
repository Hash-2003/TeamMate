package com.iit.cm2601.teammate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    public List<Participant> loadParticipants(String csvPath) throws IOException {

        List<Participant> participants = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {

            String line;
            boolean skipHeader = true;

            while ((line = br.readLine()) != null) {

                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                String[] fields = line.split(",");


                if (fields.length < 7) {
                    System.out.println("Skipping invalid row: " + line);
                    continue;
                }

                try {

                    String id = fields[0].trim();
                    String name = fields[1].trim();
                    String game = fields[2].trim();
                    String role = fields[3].trim();
                    int skill = Integer.parseInt(fields[4].trim());
                    int score = Integer.parseInt(fields[5].trim());
                    String personality = fields[6].trim();

                    Participant p = new Participant(id, name, game, role, skill, score, personality);

                    participants.add(p);

                } catch (Exception e) {
                    System.out.println("Skipping malformed row: " + line);
                }
            }
        }

        return participants;
    }
}

