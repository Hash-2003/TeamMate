package com.iit.cm2601.teammate;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TeamCSVHandler {
    public void saveTeamsToCsv(List<Team> teams, String filePath) throws IOException {

        try (FileWriter writer = new FileWriter(filePath)) {

            writer.write("TeamID,ParticipantID,Name,PreferredGame,PreferredRole,SkillLevel,PersonalityType\n");

            for (Team team : teams) {
                for (Participant p : team.getMembers()) {

                    String row = String.format(
                            "%d,%s,%s,%s,%s,%d,%s\n",
                            team.getTeamId(),
                            p.getId(),
                            escapeCsv(p.getName()),
                            p.getPreferredGame(),
                            p.getPreferredRole(),
                            p.getSkillLevel(),
                            p.getPersonalityType()
                    );

                    writer.write(row);
                }
            }
        }
    }

    //to skip problematic rows
    private String escapeCsv(String value) {
        if (value.contains(",") || value.contains("\"")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}

