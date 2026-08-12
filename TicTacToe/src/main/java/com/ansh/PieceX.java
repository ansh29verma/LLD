package com.ansh;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class PieceX extends PlayingPiece{

    public PieceX(){
        super(PieceType.X);
    }


}
