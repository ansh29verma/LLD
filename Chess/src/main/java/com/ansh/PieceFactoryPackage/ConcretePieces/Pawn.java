package com.ansh.PieceFactoryPackage.ConcretePieces;

import com.ansh.Board;
import com.ansh.Cell;
import com.ansh.MovementStrategypattern.ConcreteMovementStrategy.PawnMovementStrategy;
import com.ansh.PieceFactoryPackage.Piece;

public class Pawn extends Piece {

    public Pawn(boolean isWhite) {
        super(isWhite, new PawnMovementStrategy());
    }

    @Override
    public boolean canMove(Board board, Cell startCell, Cell endCell) {
        return super.canMove(board, startCell, endCell);
    }
}
