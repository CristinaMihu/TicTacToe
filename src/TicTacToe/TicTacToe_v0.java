package TicTacToe;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;

public class TicTacToe_v0 {
    private static final char[][] board = newBoard(3);

    private static final String[] PLAYER_NAMES = {"Player1", "Player2"};
    private static final char[] PLAYER_MARKS = {'X', 'O'};
    private static final char EMPTY_MARK = ' ';

    public static void main(String[] args) {

        System.out.println("Players win by having 3 marks in a row.\n");

        displayBoard();

        int crtPlayer = 0;
        while (true) {

            playMove(PLAYER_NAMES[crtPlayer], PLAYER_MARKS[crtPlayer]);

            int winner = detectWinner();
            if (winner != -1) {
                displayWinnerName(winner);
                break; //game ended
            }

            crtPlayer = (crtPlayer + 1) % 2;
        }
    }

    private static char[][] newBoard(int size) {
        char[][] board = new char[size][size];
        for (char[] row : board) {
            Arrays.fill(row, EMPTY_MARK);
        }
        return board;
    }

    private static void displayBoard() {
        displayHeaderRow(board.length);

        char rowLabel = 'A';
        for (char[] row : board) {
            System.out.print(rowLabel + " ");
            for (char cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
            rowLabel++;
        }
    }

    private static void displayHeaderRow(int cellCount) {
        System.out.println("  " +
                IntStream.rangeClosed(1, cellCount)
                        .mapToObj(i -> i + " ")
                        .collect(joining()));
    }

    private static void playMove(String name, char mark) {
        while (true) {

            System.out.println("\n" + name + "'s move (" + mark + "): ");
            String userInput = new Scanner(System.in).next().trim().toUpperCase();
            int row = userInput.charAt(0) - 'A';
            int col = userInput.charAt(1) - '1';

            if (board[row][col] == EMPTY_MARK) {
                board[row][col] = mark;
                displayBoard();
                break;
            }

            System.err.println("Move is invalid (cell not empty), please retry!");
        }
    }


    /**
     * Checks the board for end conditions, returns:
     * 0/1 - if game ended due to player 0/1 winning
     * 2   - if game ended but with no winner (board is full)
     * -1  - if game is not finished yet
     */
    private static int detectWinner() {
        for (int row = 0; row < 3; row++) {
            int winner = winnerOfLine(board[row][0], board[0][1], board[0][2]);
            if (winner != -1) {
                return winner;
            }
        }
        for (int col = 0; col < 3; col++) {
            int winner = winnerOfLine(board[0][col], board[1][col], board[2][col]);
            if (winner != -1) {
                return winner;
            }
        }
        int winner = winnerOfLine(board[0][0], board[1][1], board[2][2]);
        if (winner != -1) {
            return winner;
        }
        winner = winnerOfLine(board[0][2], board[1][1], board[2][0]);
        if (winner != -1) {
            return winner;
        }

        if (!boardHasEmptyCells()) {
            return 2; //tie
        }

        return -1; //no winner yet, game still playing
    }

    private static boolean boardHasEmptyCells() {
        for (char[] row : board) {
            for (char cell : row) {
                if (cell == EMPTY_MARK) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns the index of the winner of this line (0/1), or -1 if the line is not complete
     */
    private static int winnerOfLine(int mark1, int mark2, int mark3) {
        if (mark1 != EMPTY_MARK && mark1 == mark2 && mark2 == mark3) {
            return mark1 == PLAYER_MARKS[0] ? 0 : 1;
        }
        return -1;
    }

    private static void displayWinnerName(int winner) {
        if (winner == 0 || winner == 1) {
            System.out.println("Game ended, WINNER: " + PLAYER_NAMES[winner] + "(" + PLAYER_MARKS[winner] + ") !");
        } else {
            System.out.println("Game ended with NO WINNER (table full)");
        }
    }
}
