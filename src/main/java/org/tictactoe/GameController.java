package org.tictactoe;

import java.util.Scanner;

public class GameController {

	private final static Scanner sc = new Scanner(System.in);
	private static boolean keepPlaying = true;

	public static void showGameMenu() {
		System.out.println("TicTacToe with AI");
		while (keepPlaying) {
			System.out.println(" ");
			System.out.println("1. Player vs Player");
			System.out.println("2. Player vs Computer");
			System.out.println("3. Computer vs Computer");
			System.out.println("4. Exit");
			System.out.println(" ");
			System.out.print("Choose option from menu: ");
			String userInput = sc.next();
			int userInputInt = 0;
			try {
				userInputInt = Integer.parseInt(userInput);
			} catch (NumberFormatException ignored) {}

			if (userInputInt < 1 || userInputInt > 4) {
				continue;
			}

			switch (userInputInt) {
				case 1 -> createGame(Gamemode.PLAYER_VS_PLAYER);
				case 2 -> createGame(Gamemode.PLAYER_VS_COMPUTER);
				case 3 -> createGame(Gamemode.COMPUTER_VS_COMPUTER);
				case 4 -> exit();
			}
		}
	}

	private static void createGame(Gamemode gamemode) {
		switch (gamemode) {
			case PLAYER_VS_PLAYER -> {
				Game game = new Game(Gamemode.PLAYER_VS_PLAYER);
				game.play();
			}
			case PLAYER_VS_COMPUTER -> {
				Game game = new Game(Gamemode.PLAYER_VS_COMPUTER);
				game.play();
			}
			case COMPUTER_VS_COMPUTER -> {
				Game game = new Game(Gamemode.COMPUTER_VS_COMPUTER);
				game.play();
			}
		}
	}


	private static void exit() {
		keepPlaying = false;
		System.out.println("Good bye.");
	}

}
