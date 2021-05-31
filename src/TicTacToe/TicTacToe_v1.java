package TicTacToe;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;

public class TicTacToe_v1 {
    private static final char EMPTY_MARK = ' ';
    private static final char[] PLAYER_MARKS = {'X', 'O'};

    public static void main(String[] args) {

        int boardSize = Integer.parseInt(readString("Table size: "));
        int winSize = computeAndDisplayWinSize(boardSize);

        String[] playerNames = readPlayerNames();

        int[] score = {0, 0};
        int startPlayer = 0; //index of start player (0/1)

        do {
            playOneGame(startPlayer, playerNames, score, boardSize, winSize);

            startPlayer = (startPlayer + 1) % 2; //switch to other player

        } while (userWantsToContinue());
    }

    private static String readString(String message) {
        System.out.println(message);
        Scanner sc = new Scanner(System.in);
        return sc.next();
    }

    private static int computeAndDisplayWinSize(int boardSize) {
        int winSize = boardSize >= 5 ? 5 : 3;
        System.out.println("Players win by having " + winSize + " marks in a row.\n");
        return winSize;
    }

    private static String[] readPlayerNames() {
        String player1 = readString("Player 1 name: ");
        String player2 = readString("Player 2 name: ");
        return new String[]{player1, player2};
    }

    private static boolean userWantsToContinue() {
        return readString("Continue with another game? (Y/N): ")
                .equalsIgnoreCase("Y");
    }

    /**
     * Starting method for a single game.
     * Receives all required data from outside as method params.
     */
    private static void playOneGame(int crtPlayer, String[] playerNames,
                                    int[] score, int boardSize, int winSize) {

        char[][] board = buildNewBoard(boardSize);
        displayBoard(board);

        int winner;
        do {

            playMove(playerNames[crtPlayer], PLAYER_MARKS[crtPlayer], board);

            crtPlayer = (crtPlayer + 1) % 2; //turn goes to next player

            winner = checkGameEndedAndGetWinner(board, winSize);

        } while (winner == -1); //not ended

        displayWinnerAndUpdateScore(winner, score, playerNames);
    }

    /**
     * Builds a new board, sets all cells to empty char
     */
    private static char[][] buildNewBoard(int size) {
        char[][] board = new char[size][size];
        for (char[] row : board) {
            Arrays.fill(row, EMPTY_MARK);
        }
        return board;
    }

    /**
     * Displays the board, in text mode, with borders
     */
    private static void displayBoard(char[][] board) {
        displayHeaderRow(board.length);

        char rowLabel = 'A';
        for (char[] row : board) {
            if (rowLabel == 'A') {
                horizontalLine(board.length, "┌", "┬", "┐");
            } else {
                horizontalLine(board.length, "├", "┼", "┤");
            }

            System.out.println(rowLabel + " " + new String(row).chars().mapToObj(i -> " " + ((char) i) + " ").collect(joining("│", "│", "│")));

            rowLabel++;
        }

        horizontalLine(board[0].length, "└", "┴", "┘");
    }

    private static void displayHeaderRow(int cellCount) {
        System.out.println("    " +
                IntStream.rangeClosed(1, cellCount)
                        .mapToObj(i -> i + "   ")
                        .collect(joining()));
    }

    private static void horizontalLine(int cellCount, String startCorner, String middleCorner, String endCorner) {
        System.out.println("  " +
                IntStream.range(0, cellCount)
                        .mapToObj(i -> "───")
                        .collect(joining(middleCorner, startCorner, endCorner)));
    }

    /**
     * Reads one move from player and tries to play it, repeating it until
     * this is a valid/allowed move.
     */
    private static void playMove(String playerName, char playerMark, char[][] board) {

        while (true) {
            Position pos = readUserMove(playerName, playerMark, board.length);

            if (pos != null && isEmpty(pos, board)) { //is valid move
                updateAndDisplayBoard(pos, playerMark, board);
                break;
            }

            System.err.println("Move is invalid, please retry!");
        }
    }

    /**
     * Reads user input representing a move (like 'A2') and converts it to a
     * board position, if possible. Returns the position, or null if invalid.
     */
    private static Position readUserMove(String playerName, char playerMark, int boardSize) {
        String input = readString("\n" + playerName + "'s move (" + playerMark + "): ");
        return parseMove(input.trim().toUpperCase(), boardSize);
    }

    /**
     * Tries to convert a string representing a move (like 'A2') to a board
     * Position(row+column), checking the string format and that the resulted
     * row,col indices are in valid range (inside the board).
     * Returns null for invalid move (invalid format or outside board)
     */
    private static Position parseMove(String move, int boardSize) {
        if (move.length() >= 2) {
            int row = move.charAt(0) - 'A';

            String columnPart = move.substring(1);
            int col;
            try {
                col = Integer.parseInt(columnPart) - 1;
                if (row >= 0 && row < boardSize && col >= 0 && col < boardSize) {
                    return new Position(row, col);
                }
            } catch (NumberFormatException ignore) {
            }
        }
        return null;
    }

    private static boolean isEmpty(Position position, char[][] board) {
        return board[position.row][position.column] == EMPTY_MARK;
    }

    /**
     * Updates the board, marking a new position as played, and also displays it
     */
    private static void updateAndDisplayBoard(Position position, char mark, char[][] board) {
        board[position.row][position.column] = mark;
        displayBoard(board);
    }


    /**
     * Checks the board for end conditions, returns:
     * 0/1 - if game ended due to player 0/1 winning
     * 2   - if game ended but with no winner (board is full)
     * -1  - if game is not finished yet
     */
    private static int checkGameEndedAndGetWinner(char[][] board, int winSize) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                int winner = winnerStartingAt(i, j, board, winSize);
                if (winner != -1) {
                    return winner;
                }
            }
        }
        if (!hasEmptyCells(board)) {
            return 2;
        }
        return -1;
    }

    private static boolean hasEmptyCells(char[][] board) {
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
     * Checks the possible lines which start at a given board position and go in one of the possible directions
     * (row, column, diagonals) and returns:
     * - the player (0/1) which has a full line on one of these directions
     * - or -1 if none of the players has such a line there
     */
    private static int winnerStartingAt(int startRow, int startCol, char[][] board, int winSize) {
        char start = cell(startRow, startCol, board);
        if (start != EMPTY_MARK) {
            boolean wonRow = IntStream.range(0, winSize)
                    .map(i -> cell(startRow, startCol + i, board))
                    .boxed()
                    .allMatch(v -> v == start);
            boolean wonCol = IntStream.range(0, winSize)
                    .map(i -> cell(startRow + i, startCol, board))
                    .boxed()
                    .allMatch(v -> v == start);
            boolean wonDiag1 = IntStream.range(0, winSize)
                    .map(i -> cell(startRow + i, startCol + i, board))
                    .boxed()
                    .allMatch(v -> v == start);
            boolean wonDiag2 = IntStream.range(0, winSize)
                    .map(i -> cell(startRow + i, startCol - i, board))
                    .boxed()
                    .allMatch(v -> v == start);
            if (wonRow || wonCol || wonDiag1 || wonDiag2) {
                return start == PLAYER_MARKS[0] ? 0 : 1; //player index for start mark
            }
        }
        return -1;
    }

    /**
     * Returns value of a cell from board, or '?' if indices are outside the table
     */
    private static char cell(int row, int col, char[][] board) {
        return (row >= 0 && row < board.length && col >= 0 && col < board.length) ?
                board[row][col] :
                '?';
    }

    private static void displayWinnerAndUpdateScore(int winner, int[] score, String[] playerNames) {
        if (winner == 0 || winner == 1) {
            score[winner] = score[winner] + 1;
            System.out.println("Game ended, WINNER: " + playerNames[winner] + "(" + PLAYER_MARKS[winner] + ") !");
        } else {
            System.out.println("Game ended with NO WINNER (table full)");
        }
        System.out.println("Score: " + playerNames[0] + ":" + score[0] + "  " + playerNames[1] + ":" + score[1]);
    }

    /**
     * Small class representing a position on the board - just the (row,column)
     * coordinates. Useful to pass both values around as a single object.
     */
    static class Position {
        final int row;
        final int column;

        Position(int row, int column) {
            this.row = row;
            this.column = column;
        }
    }
}
