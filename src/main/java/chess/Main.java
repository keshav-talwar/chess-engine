package chess;

import chess.engine.Game;
import chess.engine.Move;
import chess.engine.PieceColor;
import chess.engine.Position;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Text front end for trying the engine out: prints the board and plays the
 * moves you type.
 */
public class Main {

    public static void main(String[] args) {
        Game game = new Game();
        Scanner input = new Scanner(System.in);

        System.out.println("Chess engine: basic moves only (no check, castling, en passant or promotion yet)");
        System.out.println("  e2e4       play a move");
        System.out.println("  moves e2   list the moves of the piece on a square");
        System.out.println("  moves      list every move for the side to move");
        System.out.println("  quit       exit");

        boolean showBoard = true;
        while (true) {
            if (showBoard) {
                System.out.println();
                System.out.print(game.getBoard());
                showBoard = false;
            }
            System.out.print(sideName(game.getTurn()) + " to move > ");
            if (!input.hasNextLine()) {
                break;  // end of input
            }
            String command = input.nextLine().trim().toLowerCase(Locale.ROOT);

            if (command.equals("quit") || command.equals("exit")) {
                break;
            }
            if (command.isEmpty()) {
                continue;
            }
            try {
                if (command.equals("moves")) {
                    printMoves(game.legalMoves());
                } else if (command.startsWith("moves ")) {
                    Position from = Position.of(command.substring("moves ".length()).trim());
                    printMoves(game.legalMovesFrom(from));
                } else if (command.length() == 4) {
                    Position from = Position.of(command.substring(0, 2));
                    Position to = Position.of(command.substring(2, 4));
                    Move move = findMove(game.legalMovesFrom(from), to);
                    if (move == null) {
                        System.out.println("Not a legal move: " + command);
                    } else {
                        game.makeMove(move);
                        showBoard = true;
                    }
                } else {
                    System.out.println("Unknown command: " + command);
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /** The move in the list that ends on the given square, or null if there is none. */
    private static Move findMove(List<Move> moves, Position to) {
        for (Move move : moves) {
            if (move.getTo().equals(to)) {
                return move;
            }
        }
        return null;
    }

    private static void printMoves(List<Move> moves) {
        if (moves.isEmpty()) {
            System.out.println("No moves.");
            return;
        }
        String label = moves.size() == 1 ? " move:" : " moves:";
        StringBuilder line = new StringBuilder(moves.size() + label);
        for (Move move : moves) {
            line.append(' ').append(move);
        }
        System.out.println(line);
    }

    private static String sideName(PieceColor color) {
        return color == PieceColor.WHITE ? "White" : "Black";
    }
}
