package com.ansh.PieceFactoryPackage;



import com.ansh.Board;
import com.ansh.Cell;
import com.ansh.MovementStrategypattern.MovementStrategy;
import lombok.Data;

@Data
public abstract class Piece {

    private boolean isWhitePiece; // is the piece white piece or black piece
    private boolean killed = false;
    private MovementStrategy movementStrategy;

    public Piece(boolean isWhitePiece, MovementStrategy movementStrategy) {
        this.isWhitePiece = isWhitePiece;
        this.movementStrategy = movementStrategy;
    }

    public boolean canMove(Board board, Cell startBlock, Cell endBlock) {
        return movementStrategy.canMove(board, startBlock, endBlock);
    }

}
