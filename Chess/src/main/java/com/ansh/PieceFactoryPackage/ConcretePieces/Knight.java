package com.ansh.PieceFactoryPackage.ConcretePieces;

import com.ansh.Board;
import com.ansh.Cell;
import com.ansh.MovementStrategypattern.ConcreteMovementStrategy.KnightMovementStrategy;
import com.ansh.PieceFactoryPackage.Piece;

public class Knight extends Piece {

    public Knight(boolean isWhite) {
        super(isWhite, new KnightMovementStrategy());
    }

    @Override
    public boolean canMove(Board board, Cell startCell, Cell endCell) {
        return super.canMove(board, startCell, endCell);
    }
}
