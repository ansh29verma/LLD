package com.ansh;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Move {

    private Cell startCell;
    private Cell endCell;

    public boolean isValid(){
        if(endCell.getPiece()  == null){
            return true;
        }
        else return !(startCell.getPiece().isWhitePiece() && endCell.getPiece().isWhitePiece());
    }



}
