package chess.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A game in progress: a board plus whose turn it is.
 *
 * <p>This is the class the user interface and the AI talk to. They ask for
 * the legal moves, choose one, and hand it back to {@link #makeMove(Move)}.
 */
public class Game {

    private final Board board;
    private PieceColor turn;

    /** Starts a new game from the standard position with White to move. */
    public Game() {
        this(Board.startingPosition(), PieceColor.WHITE);
    }

    /** Starts from any position, with the given side to move. */
    public Game(Board board, PieceColor turn) {
        this.board = Objects.requireNonNull(board, "board");
        this.turn = Objects.requireNonNull(turn, "turn");
    }

    /** The current position. Read from it freely; change it only through {@link #makeMove(Move)}. */
    public Board getBoard() {
        return board;
    }

    /** The side whose move it is. */
    public PieceColor getTurn() {
        return turn;
    }

    /**
     * Every move available to the side whose turn it is.
     *
     * <p>TODO: these are still pseudo-legal moves. Once check detection
     * exists, moves that leave the mover's king in check get filtered out
     * here, and callers will not need to change.
     */
    public List<Move> legalMoves() {
        return board.pseudoLegalMoves(turn);
    }

    /**
     * The moves available to the piece on one square. Empty if the square is
     * empty or holds a piece of the side that is not on move.
     */
    public List<Move> legalMovesFrom(Position from) {
        Piece piece = board.getPiece(from);
        if (piece == null || piece.getColor() != turn) {
            return new ArrayList<>();
        }
        return board.pseudoLegalMovesFrom(from);
    }

    /**
     * Plays a move and passes the turn to the other side.
     *
     * @throws IllegalArgumentException if the move is not one of {@link #legalMoves()}
     */
    public void makeMove(Move move) {
        if (!legalMoves().contains(move)) {
            throw new IllegalArgumentException("Illegal move: " + move);
        }
        board.makeMove(move);
        turn = turn.opposite();
    }
}
