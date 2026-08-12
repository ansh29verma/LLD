package com.ansh.MovementStrategypattern.ConcreteMovementStrategy;

import com.ansh.Board;
import com.ansh.Cell;
import com.ansh.MovementStrategypattern.MovementStrategy;
import lombok.Data;

public class QueenMovementStrategy implements MovementStrategy {

    @Override
    public boolean canMove(Board board, Cell startCell, Cell endCell) {
        return true;
    }
}
