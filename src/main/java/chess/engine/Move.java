package chess.engine;

import java.util.Objects;

/**
 * One move: a piece going from one square to another, possibly capturing.
 * Immutable.
 *
 * <p>A move remembers the piece it captured, which is exactly the
 * information needed to take the move back later.
 */
public final class Move {

    private final Position from;
    private final Position to;
    private final Piece piece;
    private final Piece captured;

    /**
     * @param piece    the piece being moved
     * @param captured the piece taken on {@code to}, or null if nothing is captured
     */
    public Move(Position from, Position to, Piece piece, Piece captured) {
        this.from = Objects.requireNonNull(from, "from");
        this.to = Objects.requireNonNull(to, "to");
        this.piece = Objects.requireNonNull(piece, "piece");
        this.captured = captured;
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }

    /** The piece being moved. */
    public Piece getPiece() {
        return piece;
    }

    /** The piece this move captures, or null if it is not a capture. */
    public Piece getCaptured() {
        return captured;
    }

    public boolean isCapture() {
        return captured != null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Move)) {
            return false;
        }
        Move other = (Move) obj;
        return from.equals(other.from)
                && to.equals(other.to)
                && piece.equals(other.piece)
                && Objects.equals(captured, other.captured);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, piece, captured);
    }

    /** Coordinate notation, for example "e2e4". */
    @Override
    public String toString() {
        return from.toString() + to;
    }
}
