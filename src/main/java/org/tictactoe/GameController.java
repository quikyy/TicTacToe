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
			System.out.println("3. Exit");
			System.out.println(" ");
			System.out.print("Choose option from menu: ");
			String userInput = sc.next();
			int userInputInt = 0;
			try {
				userInputInt = Integer.parseInt(userInput);
			} catch (NumberFormatException ignored) {}

			if (userInputInt < 1 || userInputInt > 3) {
				continue;
			}

			switch (userInputInt) {
				case 1 -> createPlayerVsPlayerGame();
				case 2 -> createPlayerVsComputerGame();
				case 3 -> exit();
			}

		}
	}

	private static void createPlayerVsPlayerGame() {
		Game game = new Game(false);
		game.play();
	}

	private static void createPlayerVsComputerGame() {
		Game game = new Game(true);
		game.play();
	}

	private static void exit() {
		keepPlaying = false;
		System.out.println("Good bye.");
	}



}
