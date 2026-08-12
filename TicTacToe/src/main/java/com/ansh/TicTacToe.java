package com.ansh;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicTacToe {

    private Deque<Player> players;
    Board gameBoard;

    public void intializeGame(){
        players = new LinkedList<>();
        gameBoard = new Board(3);

        PieceX crossPiece = new PieceX();
        Player player1 = new Player("Player1", crossPiece);

        PieceO noughtsPiece = new PieceO();
        Player player2 = new Player("Player2", noughtsPiece);

        players.add(player1);
        players.add(player2);

    }

    public String startGame(){
        boolean gameOver = false;
        while (!gameOver){
            Player currentPlayer = players.removeFirst();
            gameBoard.printBoard();
            List<Pair<Integer, Integer>> freeSpaces = gameBoard.getFreeCells();
            if(freeSpaces.isEmpty()){
                gameOver = true;
                continue;

            }
            System.out.print("Player:" + currentPlayer.getName() + " Enter row,column: ");
            Scanner inputScanner = new Scanner(System.in);
            String s = inputScanner.nextLine();
            String[] values = s.split(",");
            int inputRow = Integer.valueOf(values[0]);
            int inputColumn = Integer.valueOf(values[1]);

            boolean pieceAddedSuccessfully = gameBoard.addPiece(inputRow,inputColumn, currentPlayer.getPlayingPiece());
            if(!pieceAddedSuccessfully) {
                //player can not insert the piece into this cell, player has to choose another cell
                System.out.println("Incorredt possition chosen, try again");
                players.addFirst(currentPlayer);
                continue;
            }
            players.addLast(currentPlayer);

            boolean winner = isThereWinner(inputRow, inputColumn, currentPlayer.getPlayingPiece().getPieceType());
            if(winner) {
                gameOver = true;
                return currentPlayer.getName();
            }

        }
        return "Game Over Tie";

    }


    public boolean isThereWinner(int row, int column, PieceType pieceType) {

        boolean rowMatch = true;
        boolean columnMatch = true;
        boolean diagonalMatch = true;
        boolean antiDiagonalMatch = true;

        //need to check in row
        for(int i=0;i<gameBoard.getSize();i++) {

            if(gameBoard.getBoard()[row][i] == null || gameBoard.getBoard()[row][i].getPieceType() != pieceType) {
                rowMatch = false;
            }
        }

        //need to check in column
        for(int i=0;i<gameBoard.getSize();i++) {

            if(gameBoard.getBoard()[i][column] == null || gameBoard.getBoard()[i][column].getPieceType() != pieceType) {
                columnMatch = false;
            }
        }

        //need to check diagonals
        for(int i=0, j=0; i<gameBoard.getSize();i++,j++) {
            if (gameBoard.getBoard()[i][j] == null || gameBoard.getBoard()[i][j].getPieceType() != pieceType) {
                diagonalMatch = false;
            }
        }

        //need to check anti-diagonals
        for(int i=0, j=gameBoard.getSize()-1; i<gameBoard.getSize();i++,j--) {
            if (gameBoard.getBoard()[i][j] == null || gameBoard.getBoard()[i][j].getPieceType() != pieceType) {
                antiDiagonalMatch = false;
            }
        }

        return rowMatch || columnMatch || diagonalMatch || antiDiagonalMatch;
    }

}





