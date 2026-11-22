package chess.engine;

import static chess.engine.PieceColor.BLACK;
import static chess.engine.PieceColor.WHITE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class GameTest {

    /** Plays the move between two squares, the way a user interface would. */
    private static void play(Game game, String from, String to) {
        for (Move move : game.legalMovesFrom(Position.of(from))) {
            if (move.getTo().equals(Position.of(to))) {
                game.makeMove(move);
                return;
            }
        }
        throw new AssertionError("No legal move from " + from + " to " + to);
    }

    @Test
    void whiteMovesFirstAndTheTurnAlternates() {
        Game game = new Game();
        assertEquals(WHITE, game.getTurn());

        play(game, "e2", "e4");
        assertEquals(BLACK, game.getTurn());

        play(game, "e7", "e5");
        assertEquals(WHITE, game.getTurn());
    }

    @Test
    void newGameOffersTwentyMoves() {
        assertEquals(20, new Game().legalMoves().size());
    }

    @Test
    void offersMovesOnlyForTheSideToMove() {
        Game game = new Game();
        assertEquals(2, game.legalMovesFrom(Position.of("e2")).size());
        assertTrue(game.legalMovesFrom(Position.of("e7")).isEmpty());  // Black's pawn, White to move
        assertTrue(game.legalMovesFrom(Position.of("e4")).isEmpty());  // empty square

        play(game, "e2", "e4");
        assertEquals(2, game.legalMovesFrom(Position.of("e7")).size());
        assertTrue(game.legalMovesFrom(Position.of("d2")).isEmpty());  // White's pawn, Black to move
    }

    @Test
    void rejectsAMoveThePieceCannotMake() {
        Game game = new Game();
        Move rookThroughItsOwnPawn = new Move(Position.of("a1"), Position.of("a5"), new Rook(WHITE), null);

        assertThrows(IllegalArgumentException.class, () -> game.makeMove(rookThroughItsOwnPawn));

        assertEquals(WHITE, game.getTurn());  // nothing changed
        assertEquals(new Rook(WHITE), game.getBoard().getPiece(Position.of("a1")));
        assertTrue(game.getBoard().isEmpty(Position.of("a5")));
    }

    @Test
    void rejectsAMoveMadeOutOfTurn() {
        Game game = new Game();
        Move blackMovesFirst = new Move(Position.of("e7"), Position.of("e5"), new Pawn(BLACK), null);

        assertThrows(IllegalArgumentException.class, () -> game.makeMove(blackMovesFirst));

        assertEquals(WHITE, game.getTurn());
        assertEquals(new Pawn(BLACK), game.getBoard().getPiece(Position.of("e7")));
    }

    @Test
    void canStartFromAnyPositionWithEitherSideToMove() {
        Board board = new Board();
        board.setPiece(Position.of("e1"), new King(WHITE));
        board.setPiece(Position.of("e8"), new King(BLACK));
        board.setPiece(Position.of("a8"), new Rook(BLACK));

        Game game = new Game(board, BLACK);

        assertEquals(BLACK, game.getTurn());
        assertEquals(5 + 10, game.legalMoves().size());  // king: 5 squares, rook: 7 down the file + 3 along the rank
    }

    @Test
    void playsAnOpeningWithACapture() {
        Game game = new Game();
        play(game, "e2", "e4");
        play(game, "d7", "d5");
        play(game, "e4", "d5");  // pawn takes pawn
        play(game, "d8", "d5");  // queen takes back

        Board board = game.getBoard();
        assertEquals(new Queen(BLACK), board.getPiece(Position.of("d5")));
        assertTrue(board.isEmpty(Position.of("d8")));
        assertTrue(board.isEmpty(Position.of("e4")));
        assertTrue(board.isEmpty(Position.of("d7")));
        assertEquals(WHITE, game.getTurn());
    }
}
