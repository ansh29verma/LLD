package com.ansh.PieceFactoryPackage.ConcretePieces;

import com.ansh.Board;
import com.ansh.Cell;
import com.ansh.MovementStrategypattern.ConcreteMovementStrategy.KingMovementStrategy;
import com.ansh.PieceFactoryPackage.Piece;

public class King extends Piece {

    public King(boolean isWhite) {
        super(isWhite,new KingMovementStrategy());
    }

    @Override
    public boolean canMove(Board board, Cell startCell, Cell endCell) {
        return super.canMove(board, startCell, endCell);
    }
}
