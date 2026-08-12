package com.ansh.PieceFactoryPackage.ConcretePieces;

import com.ansh.Board;
import com.ansh.Cell;
import com.ansh.MovementStrategypattern.ConcreteMovementStrategy.QueenMovementStrategy;
import com.ansh.PieceFactoryPackage.Piece;

public class Queen extends Piece {
    public Queen(boolean isWhite) {
        super(isWhite, new QueenMovementStrategy());
    }

    @Override
    public boolean canMove(Board board, Cell startCell, Cell endCell) {
        return super.canMove(board, startCell, endCell);
    }
}
