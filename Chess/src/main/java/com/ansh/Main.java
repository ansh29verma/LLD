package com.ansh;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        // Create players
        Player player1 = new Player("Player1", true); // White
        Player player2 = new Player("Player2", false); // Black
        // Initialize game
        ChessGame chessGame = new ChessGame(player1, player2);
        // Start the game
        chessGame.start();
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
    }
}