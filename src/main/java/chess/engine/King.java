package chess.engine;

import java.util.ArrayList;
import java.util.List;

/** Moves one square in any direction. */
public final class King extends Piece {

    public King(PieceColor color) {
        super(color, PieceType.KING);
    }

    @Override
    public List<Move> pseudoLegalMoves(Board board, Position from) {
        List<Move> moves = new ArrayList<>();
        for (int[] direction : ALL_DIRECTIONS) {
            addStepMove(board, from, direction[0], direction[1], moves);
        }
        // TODO: castling
        return moves;
    }
}
