package com.iit.cm2601.teammate;

import com.iit.cm2601.teammate.exceptions.InvalidParticipantDataException;
import com.iit.cm2601.teammate.exceptions.ParticipantFileException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 Handles reading and writing Participant data to/from CSV files.
 */
public class ParticipantCSVHandler {

    private static final String CSV_HEADER =
            "ID,Name,Email,PreferredGame,SkillLevel,PreferredRole,PersonalityScore,PersonalityType";

    public List<Participant> loadParticipants(String csvPath)
            throws ParticipantFileException, InvalidParticipantDataException {

        List<Participant> participants = new ArrayList<>();

        Set<String> seenIds = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {

            String line;
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;

                // Skip header row
                if (lineNumber == 1 && line.startsWith("ID")) {
                    continue;
                }

                if (line.trim().isEmpty()) {
                    // Skip empty lines
                    continue;
                }

                String[] fields = line.split(",");

                if (fields.length != 8) {
                    throw new InvalidParticipantDataException(
                            "Invalid column count at line " + lineNumber +
                                    ". Expected 8 columns but found " + fields.length);
                }

                try {
                    String id = fields[0].trim();
                    String name = fields[1].trim();
                    String email = fields[2].trim();

                    String rawGame = fields[3].trim().toUpperCase();
                    rawGame = rawGame.replace(" ", "_")
                            .replace(":", "_");

                    String rawRole = fields[5].trim().toUpperCase();
                    rawRole = rawRole.replace(" ", "_");

                    int skillLevel = Integer.parseInt(fields[4].trim());
                    int personalityScore = Integer.parseInt(fields[6].trim());

                    String rawPersonality = fields[7].trim().toUpperCase();
                    rawPersonality = rawPersonality.replace(" ", "_");

                    GameType gameType = GameType.valueOf(rawGame);
                    RoleType roleType = RoleType.valueOf(rawRole);
                    PersonalityType personalityType = PersonalityType.valueOf(rawPersonality);

                    if (seenIds.contains(id)) {
                        throw new InvalidParticipantDataException(
                                "Duplicate participant ID found in CSV: " + id);
                    }
                    seenIds.add(id);

                    // range validation
                    if (skillLevel < 1 || skillLevel > 10) {
                        throw new InvalidParticipantDataException(
                                "SkillLevel out of range (1-10) at line " + lineNumber);
                    }

                    Participant participant = new Participant(id, name, email, gameType, skillLevel, roleType, personalityScore, personalityType);

                    participants.add(participant);

                } catch (IllegalArgumentException e) {

                    throw new InvalidParticipantDataException(
                            "Invalid data format at line " + lineNumber + ": " + e.getMessage());
                }
            }

        } catch (IOException e) {
            throw new ParticipantFileException(
                    "Error reading participant CSV file: " + csvPath, e);
        }

        return participants;
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }


    public void saveParticipantsToCsv(List<Participant> participants, String csvPath)
            throws ParticipantFileException {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvPath))) {

            //header
            bw.write("ID,Name,Email,PreferredGame,SkillLevel,PreferredRole,PersonalityScore,PersonalityType");
            bw.newLine();


            for (Participant p : participants) {
                String line = String.join(",",
                        safe(p.getId()),
                        safe(p.getName()),
                        safe(p.getEmail()),
                        p.getPreferredGame().name(),
                        String.valueOf(p.getSkillLevel()),
                        p.getPreferredRole().name(),
                        String.valueOf(p.getPersonalityScore()),
                        p.getPersonalityType().name()
                );
                bw.write(line);
                bw.newLine();
            }

        } catch (IOException e) {
            throw new ParticipantFileException(
                    "Error writing to CSV file: " + csvPath, e
            );
        }
    }



}
