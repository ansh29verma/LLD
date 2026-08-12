package com.ansh.MovementStrategypattern;

import com.ansh.Board;
import com.ansh.Cell;

public interface MovementStrategy {
    boolean canMove(Board board, Cell startCell, Cell endCell);
}
