package com.ansh;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        TicTacToe game = new TicTacToe();
        game.intializeGame();
        System.out.println("game winner is: " + game.startGame());
    }
}

//
//class symbol
//class Board
//class Player
//Managing Game state
//Ensure proper allocation
//Strategy pattern for player interaction
//Move
//state pattern(not that we can use)

//observer pattern  for game state

//factory for player creation




