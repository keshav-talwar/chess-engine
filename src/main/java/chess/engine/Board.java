package chess.engine;

import java.util.ArrayList;
import java.util.List;

/**
 * The 8x8 board: which piece stands on which square.
 *
 * <p>The grid is private. Code outside the engine can look at the board and
 * ask for moves, but can only change it through {@link #makeMove(Move)}.
 */
public class Board {

    public static final int SIZE = 8;

    /** Indexed as squares[rank][file]. An empty square is null. */
    private final Piece[][] squares = new Piece[SIZE][SIZE];

    /** Creates an empty board. */
    public Board() {
    }

    /**
     * Copy constructor: the new board can be changed without affecting the
     * original. Pieces are immutable, so both boards share the piece objects.
     */
    public Board(Board other) {
        for (int rank = 0; rank < SIZE; rank++) {
            System.arraycopy(other.squares[rank], 0, squares[rank], 0, SIZE);
        }
    }

    /** Creates a board with all 32 pieces on their starting squares. */
    public static Board startingPosition() {
        Board board = new Board();
        board.placeArmy(PieceColor.WHITE, 0, 1);
        board.placeArmy(PieceColor.BLACK, SIZE - 1, SIZE - 2);
        return board;
    }

    private void placeArmy(PieceColor color, int backRank, int pawnRank) {
        Piece[] backRow = {
            new Rook(color), new Knight(color), new Bishop(color), new Queen(color),
            new King(color), new Bishop(color), new Knight(color), new Rook(color)
        };
        for (int file = 0; file < SIZE; file++) {
            squares[backRank][file] = backRow[file];
            squares[pawnRank][file] = new Pawn(color);
        }
    }

    /** The piece on the given square, or null if the square is empty. */
    public Piece getPiece(Position position) {
        return squares[position.getRank()][position.getFile()];
    }

    public boolean isEmpty(Position position) {
        return getPiece(position) == null;
    }

    /**
     * Puts a piece on a square, or clears the square if {@code piece} is null.
     * Package-private on purpose: only the engine itself (and its tests)
     * may place pieces directly.
     */
    void setPiece(Position position, Piece piece) {
        squares[position.getRank()][position.getFile()] = piece;
    }

    /**
     * Plays a move on the board. Any piece on the destination square is
     * captured.
     *
     * <p>This only checks that the piece is where the move says it is.
     * Whether the move is allowed at all is decided by {@link Game}.
     *
     * @throws IllegalArgumentException if the moving piece is not on its starting square
     */
    public void makeMove(Move move) {
        Position from = move.getFrom();
        Piece piece = getPiece(from);
        if (!move.getPiece().equals(piece)) {
            throw new IllegalArgumentException("There is no " + move.getPiece() + " on " + from);
        }
        setPiece(move.getTo(), piece);
        setPiece(from, null);
    }

    /** All pseudo-legal moves for every piece of one colour. */
    public List<Move> pseudoLegalMoves(PieceColor color) {
        List<Move> moves = new ArrayList<>();
        for (int rank = 0; rank < SIZE; rank++) {
            for (int file = 0; file < SIZE; file++) {
                Piece piece = squares[rank][file];
                if (piece != null && piece.getColor() == color) {
                    // Polymorphism: the board does not know or care which
                    // kind of piece this is. Each subclass supplies its own moves.
                    moves.addAll(piece.pseudoLegalMoves(this, new Position(file, rank)));
                }
            }
        }
        return moves;
    }

    /** The pseudo-legal moves of the piece on one square; empty if the square is empty. */
    public List<Move> pseudoLegalMovesFrom(Position from) {
        Piece piece = getPiece(from);
        if (piece == null) {
            return new ArrayList<>();
        }
        return piece.pseudoLegalMoves(this, from);
    }

    /**
     * A text diagram seen from White's side. Upper-case letters are White's
     * pieces, lower-case are Black's, and a dot is an empty square.
     */
    @Override
    public String toString() {
        String border = "  +------------------------+\n";
        StringBuilder text = new StringBuilder(border);
        for (int rank = SIZE - 1; rank >= 0; rank--) {
            text.append(rank + 1).append(" |");
            for (int file = 0; file < SIZE; file++) {
                Piece piece = squares[rank][file];
                text.append(' ').append(piece == null ? '.' : piece.getSymbol()).append(' ');
            }
            text.append("|\n");
        }
        text.append(border);
        text.append("    a  b  c  d  e  f  g  h\n");
        return text.toString();
    }
}
