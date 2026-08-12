package com.ansh.PieceFactoryPackage.ConcretePieces;

import com.ansh.Board;
import com.ansh.Cell;
import com.ansh.MovementStrategypattern.ConcreteMovementStrategy.RookMovementStrategy;
import com.ansh.PieceFactoryPackage.Piece;

public class Rook extends Piece {

    public Rook(boolean isWhite) {
        super(isWhite, new RookMovementStrategy());
    }

    @Override
    public boolean canMove(Board board, Cell startCell, Cell endCell) {
        return super.canMove(board, startCell, endCell);
    }
}
