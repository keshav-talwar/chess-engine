package chess.engine;

import java.util.ArrayList;
import java.util.List;

/**
 * Moves straight ahead but captures diagonally, and is the only piece whose
 * direction depends on its colour: White's pawns go up the board, Black's go
 * down.
 */
public final class Pawn extends Piece {

    public Pawn(PieceColor color) {
        super(color, PieceType.PAWN);
    }

    @Override
    public List<Move> pseudoLegalMoves(Board board, Position from) {
        List<Move> moves = new ArrayList<>();
        boolean white = getColor() == PieceColor.WHITE;
        int forward = white ? 1 : -1;
        int startRank = white ? 1 : Board.SIZE - 2;

        int file = from.getFile();
        int rankAhead = from.getRank() + forward;
        if (!Position.isValid(file, rankAhead)) {
            return moves;  // already on the last rank
        }

        // Straight ahead: one square, or two from the starting rank.
        // Both need empty squares, since a pawn cannot capture forwards.
        Position oneAhead = new Position(file, rankAhead);
        if (board.isEmpty(oneAhead)) {
            moves.add(new Move(from, oneAhead, this, null));
            if (from.getRank() == startRank) {
                Position twoAhead = new Position(file, rankAhead + forward);
                if (board.isEmpty(twoAhead)) {
                    moves.add(new Move(from, twoAhead, this, null));
                }
            }
        }

        // Diagonally ahead, only onto an enemy piece.
        for (int side = -1; side <= 1; side += 2) {
            int captureFile = file + side;
            if (Position.isValid(captureFile, rankAhead)) {
                Position target = new Position(captureFile, rankAhead);
                Piece occupant = board.getPiece(target);
                if (isEnemy(occupant)) {
                    moves.add(new Move(from, target, this, occupant));
                }
            }
        }

        // TODO: en passant and promotion
        return moves;
    }
}
