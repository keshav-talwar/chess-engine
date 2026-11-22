package chess.engine;

import static chess.engine.PieceColor.WHITE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Perft ("performance test") counts every sequence of moves of a given
 * length from a position. The counts from the starting position are
 * published, so matching them is the standard way to prove a move generator
 * correct: one missing or extra move anywhere changes the total.
 *
 * <p>Published counts: 20, 400, 8,902, 197,281, 4,865,609, ...
 *
 * <p>During the first three moves nobody can castle, capture en passant,
 * promote, or be stopped from moving by a check, so the basic piece movement
 * on its own has to reproduce the first three counts. From depth 4 onwards
 * the check rules are needed.
 */
class PerftTest {

    private static long perft(Board board, PieceColor sideToMove, int depth) {
        if (depth == 0) {
            return 1;
        }
        long count = 0;
        for (Move move : board.pseudoLegalMoves(sideToMove)) {
            Board next = new Board(board);
            next.makeMove(move);
            count += perft(next, sideToMove.opposite(), depth - 1);
        }
        return count;
    }

    @Test
    void depthOne() {
        assertEquals(20L, perft(Board.startingPosition(), WHITE, 1));
    }

    @Test
    void depthTwo() {
        assertEquals(400L, perft(Board.startingPosition(), WHITE, 2));
    }

    @Test
    void depthThree() {
        assertEquals(8902L, perft(Board.startingPosition(), WHITE, 3));
    }
}
