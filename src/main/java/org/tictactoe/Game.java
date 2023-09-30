package org.tictactoe;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import lombok.Getter;

@Getter
public class Game {

	private final Board board = new Board();
	private final List<List<Integer>> winsConditions = buildWinsCombinations();
	private final List<Player> players;
	private final SecureRandom secureRandom = new SecureRandom();


	public Game(boolean withComputer) {
		players = buildPlayers(withComputer);
	}

	public void play() {
		Scanner sc = new Scanner(System.in);
		boolean gameWon = false;

		while (!gameWon) {
			for (Player player : players) {

				if (board.getAvailableFields().size() == 0) {
					System.out.println("It's a tie! Game ends.");
					gameWon = true;
					break;
				}

				board.drawBoard();
				System.out.println("Player playing: " + player.getSign());
				boolean isPlayerTookAvailableField = false;
				if (player.isComputer()) {
					isPlayerTookAvailableField = choiceFieldComputer();
					gameWon = checkWin(player);
					if (gameWon) {
						System.out.println("Computer won the game!");
						break;
					}
					continue;
				}
				while (!isPlayerTookAvailableField) {
					System.out.println("Available fields: " + board.getAvailableFields());
					System.out.print("Choice field: ");
					int playerFieldChoice = sc.nextInt();
					if (playerFieldChoice < 1 || playerFieldChoice > 9) {
						continue;
					}
					isPlayerTookAvailableField = board.takeField(playerFieldChoice, player);
					if (!isPlayerTookAvailableField) {
						System.out.println("Field: " + playerFieldChoice + " is already taken. Select another one.");
					}
					gameWon = checkWin(player);
					if (gameWon) {
						break;
					}
				}
			}
		}

		board.drawBoard();
	}


	private boolean checkWin(Player player) {
		List<Integer> playerTakenFields = player.getFieldsTaken();
		for (List<Integer> winCombination : winsConditions) {
			if (playerTakenFields.containsAll(winCombination)) {
				return true;
			}
		}
		return false;
	}

	private List<Player> buildPlayers(boolean withComputer) {
		List<Player> currPlayers = new ArrayList<>();
		if (withComputer) {
			Player firstPlayer = new Player("X", new ArrayList<>(), false);
			Player secondPlayer = new Player("O", new ArrayList<>(), true);
			currPlayers.addAll(List.of(firstPlayer, secondPlayer));
		} else {
			Player firstPlayer = new Player("X", new ArrayList<>(), false);
			Player secondPlayer = new Player("O", new ArrayList<>(), false);
			currPlayers.addAll(List.of(firstPlayer, secondPlayer));
		}
		Collections.shuffle(currPlayers);
		return currPlayers;
	}



	private boolean choiceFieldComputer() {
		Player computer = players.stream()
				.filter(Player::isComputer)
				.findFirst()
				.orElse(null);

		Player opponent = players.stream()
				.filter(player -> !player.isComputer())
				.findFirst()
				.orElse(null);

		if (computer == null || opponent == null) {
			return false;
		}

		List<Integer> availableFields = board.getAvailableFields();
		int computerChoice = 0;

		// Computer moves as first
		if (availableFields.size() == 9) {
			System.out.println("Computer starts and computer will take corner field");
			return board.takeField(choiceCornerField(), computer);
		}

		// Opponent moved first, check if he picked center field
		if (availableFields.size() == 8) {
			Player playerWhoTookCentreField = board.getTakenFields().get(5);
			if (playerWhoTookCentreField == opponent) {
				System.out.println("Player moved first and took cent");
				return board.takeField(choiceCornerField(), computer);
			}
		}

		// try to attack
		computerChoice = attackMove(computer);
		if (computerChoice != 0) {
			return board.takeField(computerChoice, computer);
		}

		// try to defend
		computerChoice = defendMove(computer, opponent);
		if (computerChoice != 0) {
			return board.takeField(computerChoice, computer);
		}

		computerChoice = checkOppositeCornerFields(computer, opponent);
		if (computerChoice != 0 ) {
			return board.takeField(computerChoice, computer);
		}

		if (computerChoice == 0) {
			int randomInt = secureRandom.nextInt(availableFields.size());
			computerChoice = availableFields.get(randomInt);
			System.out.println("There was 0 matches in win combinations. Computer will pick random field");
		}


		System.out.println("Computer final choice: " + computerChoice);
		return board.takeField(computerChoice, computer);
	}

	private int choiceCornerField() {
		int randomInt = secureRandom.nextInt(board.getCornerFields().size());
		return board.getCornerFields().get(randomInt);
	}

	private int attackMove(Player computer) {
		List<Integer> computerTakenFields = computer.getFieldsTaken();
		int computerChoice = 0;
		for (List<Integer> winCombination : winsConditions) {
			int matches = 0;
			for (Integer fieldInWinCombination : winCombination) {
				for (Integer computerTakenField : computerTakenFields) {
					if (computerTakenField.equals(fieldInWinCombination)) {
						matches++;
					}
				}
			}
			if (matches == 2) {
				System.out.println("");
				System.out.println("Computer should pick field from: " + winCombination + " to win the game");
				System.out.println("");

				computerChoice = winCombination.stream()
						.filter(a -> !computerTakenFields.contains(a))
						.findFirst()
						.orElse(0);

				System.out.println("Computer initial attack choice: " + computerChoice);

				Player takenFieldPlayer = board.getTakenFields().get(computerChoice);

				if (takenFieldPlayer == null) {
					System.out.println("This field is free - computer will pick field: " + computerChoice);
					break;
				} else if (takenFieldPlayer != computer) {
					System.out.println("Computer should pick " + computerChoice + " but is already taken by computer");
					computerChoice = 0;
					continue;
				}
				break;
			}
		}
		System.out.println("Computer final attack choice: " + computerChoice);
		return computerChoice;
	}

	private int defendMove(Player computer, Player opponent) {
		int computerChoice = 0;
		List<Integer> opponentTakenFields = opponent.getFieldsTaken();
		for (List<Integer> winCombination : winsConditions) {
			int matches = 0;
			for (Integer fieldInWinCombination : winCombination) {
				for (Integer opponentTakenField : opponentTakenFields) {
					if (opponentTakenField.equals(fieldInWinCombination)) {
						matches++;
					}
				}
			}
			if (matches == 2) {

				System.out.println("");
				System.out.println("Computer should pick field from: " + winCombination + " to block from winning");
				System.out.println("");

				computerChoice = winCombination.stream()
						.filter(a -> !opponentTakenFields.contains(a))
						.findFirst()
						.orElse(0);

				System.out.println("Computer initial defend choice: " + computerChoice);

				Player takenFieldPlayer = board.getTakenFields().get(computerChoice);

				if (takenFieldPlayer == null) {
					System.out.println("This field is free - computer will pick field: " + computerChoice);
					break;
				} else if (takenFieldPlayer == computer) {
					System.out.println("Computer should pick " + computerChoice + " but is already taken by computer");
					computerChoice = 0;
					continue;
				}
				break;
			}
		}
		return computerChoice;
	}

	private int checkOppositeCornerFields(Player computer, Player opponent) {
		int computerChoice = 0;
		List<List<Integer>> oppositeCornerFields = board.getOppositeCornerFields();

		for (List<Integer> cornerFields : oppositeCornerFields) {
			for (Integer playerTakenField : opponent.getFieldsTaken()) {
				if (cornerFields.contains(playerTakenField)) {
					System.out.println("Opponent has corer field taken: " + playerTakenField + " computer will take opposite one");

					computerChoice = cornerFields.stream()
							.filter(a -> !opponent.getFieldsTaken().contains(a))
							.findFirst()
							.orElse(0);

					System.out.println("Computer initial corner choice: " + computerChoice);

					Player takenFieldPlayer = board.getTakenFields().get(computerChoice);

					if (takenFieldPlayer == null) {
						System.out.println("This field is free - computer will pick field: " + computerChoice);
						break;
					} else if (takenFieldPlayer == computer) {
						System.out.println("Computer should pick " + computerChoice + " but is already taken by computer");
						computerChoice = 0;
						continue;
					}
					break;
				}

			}

		}
		return computerChoice;
	}


	private List<List<Integer>> buildWinsCombinations() {
		List<Integer> winCombination1 = List.of(1, 2, 3);
		List<Integer> winCombination2 = List.of(4, 5, 6);
		List<Integer> winCombination3 = List.of(7, 8, 9);

		List<Integer> winCombination4 = List.of(1, 4, 7);
		List<Integer> winCombination5 = List.of(2, 5, 8);
		List<Integer> winCombination6 = List.of(3, 6, 9);

		List<Integer> winCombination7 = List.of(1, 5, 9);
		List<Integer> winCombination8 = List.of(3, 5, 7);
		return List.of(winCombination1, winCombination2, winCombination3, winCombination4, winCombination5, winCombination6, winCombination7, winCombination8);
	}




}
