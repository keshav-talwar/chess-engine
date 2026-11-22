# Chess

A chess engine in Java, built as an object-oriented design project. A JavaFX
board and an AI opponent will sit on top of it.

## Status

Working now:

- The board, the starting position and a text view of it
- Movement for all six pieces, including blocking and captures
- Turn handling and a console front end

Not yet:

- Check, checkmate and stalemate. Moves are *pseudo-legal* for now: a move
  that leaves your own king in check is still allowed.
- Castling, en passant and promotion
- Undo/redo and FEN
- GUI and AI

## Running it

You need JDK 11 or newer. From the project folder:

```
javac -d out -sourcepath src/main/java src/main/java/chess/Main.java
java -cp out chess.Main
```

Or open the folder in IntelliJ IDEA or VS Code (it is a Maven project) and
run `chess.Main`.

```
White to move > e2e4

  +------------------------+
8 | r  n  b  q  k  b  n  r |
7 | p  p  p  p  p  p  p  p |
6 | .  .  .  .  .  .  .  . |
5 | .  .  .  .  .  .  .  . |
4 | .  .  .  .  P  .  .  . |
3 | .  .  .  .  .  .  .  . |
2 | P  P  P  P  .  P  P  P |
1 | R  N  B  Q  K  B  N  R |
  +------------------------+
    a  b  c  d  e  f  g  h
Black to move >
```

## Tests

```
mvn test
```

The tests use JUnit 5, which Maven (or the IDE) downloads on the first run.
They cover every piece's movement and compare move counts from the starting
position against the published perft numbers (20, 400, 8,902).

## Design

Everything is in the `chess.engine` package, which has no dependency on any
user interface.

```
Piece (abstract)              colour, type, pseudoLegalMoves(board, from)
├── SlidingPiece (abstract)   slides along lines until something blocks it
│   ├── Rook
│   ├── Bishop
│   └── Queen
├── Knight
├── King
└── Pawn
```

| Class | Responsibility |
|---|---|
| `Piece` and subclasses | How each kind of piece moves |
| `Board` | Which piece is on which square; plays moves |
| `Move` | One move, including the piece it captured |
| `Position` | A square such as `e4` |
| `Game` | A board plus whose turn it is; rejects illegal moves |
| `PieceColor`, `PieceType` | Enums for the side and the kind of piece |

`Board` calls `piece.pseudoLegalMoves(...)` without knowing which kind of
piece it has, and each subclass answers in its own way. Pieces do not store
their own square. The board is the only place that knows where pieces are,
so the two can never disagree, and pieces can be immutable.

## Using the engine from other code

This is what a GUI or an AI needs:

| Call | Result |
|---|---|
| `new Game()` | A new game, White to move |
| `game.getTurn()` | `PieceColor.WHITE` or `PieceColor.BLACK` |
| `game.getBoard().getPiece(position)` | The `Piece` on a square, or `null` if it is empty |
| `piece.getType()`, `piece.getColor()` | What to draw |
| `game.legalMovesFrom(position)` | The moves of one piece, e.g. for highlighting squares |
| `game.legalMoves()` | Every move for the side to move |
| `game.makeMove(move)` | Plays a move and switches the turn |
| `new Board(board)` | An independent copy, for trying moves out |

Squares are `new Position(file, rank)` with both numbers from 0 to 7:
file 0 is the a-file and rank 0 is rank 1, White's back row.
`Position.of("e4")` does the same from text. To draw with row 0 at the top of
the screen, use `row = 7 - rank`.
