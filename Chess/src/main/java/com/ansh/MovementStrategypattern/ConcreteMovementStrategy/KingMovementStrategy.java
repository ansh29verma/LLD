package com.ansh.MovementStrategypattern.ConcreteMovementStrategy;

import com.ansh.Board;
import com.ansh.Cell;
import com.ansh.MovementStrategypattern.MovementStrategy;

public class KingMovementStrategy implements MovementStrategy {
    @Override
    public boolean canMove(Board board, Cell startCell, Cell endCell) {
        return true;
    }
}
