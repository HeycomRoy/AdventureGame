package model.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.character.Player;

public class GameTest {

    private Level board;
    private static final Scanner SCANNER = new Scanner(System.in);

    // used to represent player's move directions
    private enum Direction {
        N, E, S, W
    };

    public static void main(String[] args) {

        welcomeMsg();

        setupGame();

        SCANNER.close();
    }

    private static void welcomeMsg() {
        System.out.println("==============Welcome to the Froger Game==============");

    }

    private static void setupGame() {

        System.out.println("Enter Name!");

    }


    private static void parseFreeMove(Player player, int availableSteps) {

        String line = SCANNER.nextLine();

        int index = 0;
        int totalSteps = 0;
        List<Direction> moves = new ArrayList<>();
        Direction lastDirection;
        Location Loc = player.getLocation();

        while (index < line.length()) {
            index = skipWhiteSpace(index, line);
            char directionChar = line.charAt(index++);

            // deal with the direction character
            switch (directionChar) {
            case 'w':
            case 'W':
                lastDirection = Direction.N;
                break;
            case 'd':
            case 'D':
                lastDirection = Direction.E;
                break;
            case 's':
            case 'S':
                lastDirection = Direction.S;
                break;
            case 'a':
            case 'A':
                lastDirection = Direction.W;
                break;
            default:
                failMsg("invalid input, \"w, a, s, d\" expected at index " + (index - 1));
                return;
            }
            
            // then the steps
            index = skipWhiteSpace(index, line);
            char stepsChar = line.charAt(index++);
            int steps = 0;
            
            if (Character.isDigit(stepsChar)) {
                steps = Integer.parseInt("" + stepsChar);
            } else {
                failMsg("invalid input, digits expected at index " + (index - 1));
                return;
            }
          
            // check if the third character is still a digit
            char nextStepsChar = line.charAt(index);
            if (Character.isDigit(nextStepsChar)) {
                // this digit and the previous digit together indicates the number of steps
                steps = steps * 10 + Integer.parseInt("" + nextStepsChar);
                index++;
            }
            
            // now some sanity checks
            // 1. if the player's free input has more than available moves
            totalSteps += steps;
            if (totalSteps > availableSteps) {
                moves.clear();
                failMsg("You don't have enough moved to make, try to move no more than "
                        + availableSteps + " steps.");
                return;
            }

            // TODO need to do a simulated move, to see if the player will hit a wall
            // condition cannot be true, change it.
            // 2. if the player's free input will hit a wall
            for (int i = 0; i < steps; i++) {
                
            }
            
            
            
            if (true) {
                moves.clear();
                failMsg("Your free-move will hit a wall, try again.");
                return;
            }

        }

    }

    private static int skipWhiteSpace(int index, String line) {
        int newIndex = index;
        while ((line.charAt(newIndex) == ' ' || line.charAt(newIndex) == '\t')
                && newIndex < line.length()) {
            newIndex++;
        }
        return newIndex;
    }

    private static void failMsg(String msg) {
        // TODO Auto-generated method stub
        System.out.println(msg);
    }

}