package chess.engine;

/**
 * The six kinds of piece, each with the letter used to print it.
 *
 * <p>How a piece moves lives in the {@link Piece} subclasses. This enum is
 * only a label, for code that needs to know what a piece is (drawing it,
 * printing it) without caring how it moves.
 */
public enum PieceType {
    KING('K'),
    QUEEN('Q'),
    ROOK('R'),
    BISHOP('B'),
    KNIGHT('N'),
    PAWN('P');

    private final char symbol;

    PieceType(char symbol) {
        this.symbol = symbol;
    }

    /** Upper-case letter for this piece. The knight is N, since K is the king. */
    public char getSymbol() {
        return symbol;
    }
}
