package chess.engine;

import java.util.ArrayList;
import java.util.List;

/**
 * A piece that slides any number of squares in a straight line until
 * something is in the way: the rook, the bishop and the queen.
 *
 * <p>The three differ only in which directions they slide, so each subclass
 * just passes its directions to this class and inherits the rest.
 */
public abstract class SlidingPiece extends Piece {

    private final int[][] directions;

    /**
     * @param directions the lines this piece slides along, as {file step, rank step}
     */
    protected SlidingPiece(PieceColor color, PieceType type, int[][] directions) {
        super(color, type);
        this.directions = directions;
    }

    @Override
    public List<Move> pseudoLegalMoves(Board board, Position from) {
        List<Move> moves = new ArrayList<>();
        for (int[] direction : directions) {
            int file = from.getFile() + direction[0];
            int rank = from.getRank() + direction[1];

            while (Position.isValid(file, rank)) {
                Position to = new Position(file, rank);
                Piece occupant = board.getPiece(to);

                if (occupant == null) {
                    moves.add(new Move(from, to, this, null));
                } else {
                    if (isEnemy(occupant)) {
                        moves.add(new Move(from, to, this, occupant));
                    }
                    break;  // any piece, friend or enemy, blocks the line
                }

                file += direction[0];
                rank += direction[1];
            }
        }
        return moves;
    }
}
