package com.iit.cm2601.teammate;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TeamCSVHandlerTest {

    public static void main(String[] args) {

        Participant p1 = new Participant(
                "P001",
                "Nimal Kamal",
                "nimal@example.com",
                GameType.VALORANT,
                7,
                RoleType.ATTACKER,
                90,
                PersonalityType.LEADER
        );

        Participant p2 = new Participant(
                "P002",
                "Amal Kamal",
                "amal@example.com",
                GameType.DOTA_2,
                5,
                RoleType.DEFENDER,
                75,
                PersonalityType.THINKER
        );

        Participant p3 = new Participant(
                "P003",
                "Upul Kapila",
                "upul@example.com",
                GameType.CS_GO,
                6,
                RoleType.STRATEGIST,
                65,
                PersonalityType.BALANCED
        );

        Team team1 = new Team(1, 3);
        team1.addMember(p1);
        team1.addMember(p2);

        Team team2 = new Team(2, 3);
        team2.addMember(p3);

        List<Team> testTeams = new ArrayList<>();
        testTeams.add(team1);
        testTeams.add(team2);

        TeamCSVHandler handler = new TeamCSVHandler();
        String testCsvPath = "C:\\Users\\Hasindu\\Documents\\IIT\\Level 5\\CM 2601\\CW\\TeamMate\\src\\main\\resources\\com\\iit\\cm2601\\teammate\\data\\test_teams_output.csv";

        try {
            handler.saveTeamsToCsv(testTeams, testCsvPath);
            System.out.println("Test passed: CSV exported successfully to:");
            System.out.println(testCsvPath);

            System.out.println("\n=== Teams Written ===");
            for (Team t : testTeams) {
                System.out.println("Team " + t.getTeamId() + ":");
                for (Participant p : t.getMembers()) {
                    System.out.println("  - " + p.getId() + " | " + p.getName());
                }
            }

        } catch (IOException e) {
            System.out.println("Test failed: CSV export threw an exception:");
            e.printStackTrace();
        }

    }
}