package com.ansh;

import com.ansh.PieceFactoryPackage.Piece;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Cell {
    private int row;
    private int col;
    private String  label;
    private Piece piece;

    public Cell(int row, int col, Piece piece) {
        this.row = row;
        this.col = col;
        this.piece = piece;
    }
}
