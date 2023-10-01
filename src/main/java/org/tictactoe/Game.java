package org.tictactoe;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Game {
	private final Board board = new Board();
	private final List<List<Integer>> winsCombinations = buildWinsCombinations();
	private final List<Player> players;
	private Player winner;
	private List<Integer> winnerWinCombination;
	private final SecureRandom secureRandom = new SecureRandom();
	private final Scanner sc = new Scanner(System.in);

	public Game(boolean withComputer) {
		players = buildPlayers(withComputer);
	}

	public void play() {
		boolean gameEnd = false;

		while (!gameEnd) {
			for (Player player : players) {
				gameEnd = checkIfTie();
				if (gameEnd) {
					System.out.println("Game ended as a tie");
					break;
				}
				board.draw();
				System.out.println("Available fields: " + board.getAvailableFields());
				boolean isPlayerTookAvailableField = false;
				if (player.isComputer()) {
					choiceFieldComputer();
				} else {
					while (!isPlayerTookAvailableField) {
						System.out.println("Turn: " + player.getSign());
						System.out.print("Please choice available field: ");
						String playerInput = sc.next();
						int playerFieldChoice = 0;
						try {
							playerFieldChoice = Integer.parseInt(playerInput);
						} catch (NumberFormatException ignored) {}

						if (playerFieldChoice < 1 || playerFieldChoice > 9) {
							continue;
						}
						isPlayerTookAvailableField = board.takeField(playerFieldChoice, player);
						if (!isPlayerTookAvailableField) {
							System.out.println("Field: " + playerFieldChoice + " is already taken");
						}

						if (Objects.nonNull(winner)) {
							gameEnd = true;
							break;
						}
					}
				}

				checkIfPlayerWonGame(player);
				if (Objects.nonNull(winner)) {
					gameEnd = true;
					break;
				}
			}
		}
		board.draw();
		gameEndsMessage();
	}

	private void choiceFieldComputer() {
		Player computer = players.stream()
				.filter(Player::isComputer)
				.findFirst()
				.orElse(null);
		Player opponent = players.stream()
				.filter(player -> !player.isComputer())
				.findFirst()
				.orElse(null);
		if (computer == null || opponent == null) {
			return;
		}


		List<Integer> availableFields = board.getAvailableFields();
		int computerChoice = 0;

		// Computer moves as first and will take corner field
		if (availableFields.size() == 9) {
			computerChoice = choiceCornerField();
			board.takeField(computerChoice, computer);
			return;
		}

		// Opponent moved first, check if he picked center field. If no - pick center field
		if (availableFields.size() == 8) {
			Player playerWhoTookCentreField = board.getTakenFields().get(5);
			if (playerWhoTookCentreField == opponent) {
				board.takeField(choiceCornerField(), computer);
			} else {
				board.takeField(5, computer);
			}
			return;
		}

		// Win the game if possible
		computerChoice = winMove(computer);
		if (computerChoice != 0) {
			board.takeField(computerChoice, computer);
			return;
		}

		// Defend if player has 2 fields taken in any win combination
		computerChoice = defendMove(opponent);
		if (computerChoice != 0) {
			board.takeField(computerChoice, computer);
			return;
		}

		computerChoice = analyzeWinCombinations(computer, opponent);
		if (computerChoice != 0) {
			board.takeField(computerChoice, computer);
			return;
		}

		// Computer had no idea what to do. Computer picked random available field
		int randomInt = secureRandom.nextInt(availableFields.size());
		computerChoice = availableFields.get(randomInt);
		board.takeField(computerChoice, computer);
	}

	private int winMove(Player computer) {
		int computerFieldChoice = 0;
		List<Integer> computerTakenFields = computer.getFieldsTaken();
		for (List<Integer> winCombination : winsCombinations) {
			long computerFieldsInWinCombination = computerTakenFields.stream()
					.filter(winCombination::contains)
					.count();
			if (computerFieldsInWinCombination == 2) {
				computerFieldChoice = winCombination.stream()
						.filter(a -> !computerTakenFields.contains(a))
						.findFirst()
						.orElse(0);
				Player playerWhoHasFieldChosenByComputer = board.getTakenFields().get(computerFieldChoice);
				if (playerWhoHasFieldChosenByComputer == null) {
					return computerFieldChoice;
				} else {
					computerFieldChoice = 0;
				}
			}
		}
		return computerFieldChoice;
	}

	private int defendMove(Player opponent) {
		int computerFieldChoice = 0;
		List<Integer> opponentTakenFields = opponent.getFieldsTaken();
		for (List<Integer> winCombination : winsCombinations) {
			long opponentFieldsInWinCombination = opponentTakenFields.stream()
					.filter(winCombination::contains)
					.count();
			if (opponentFieldsInWinCombination == 2) {
				computerFieldChoice = winCombination.stream()
						.filter(a -> !opponentTakenFields.contains(a))
						.findFirst()
						.orElse(0);
				Player playerWhoHasFieldChosenByComputer = board.getTakenFields().get(computerFieldChoice);
				if (playerWhoHasFieldChosenByComputer == null) {
					return computerFieldChoice;
				} else {
					computerFieldChoice = 0;
				}
			}
		}
		return computerFieldChoice;
	}

	private int analyzeWinCombinations(Player computer, Player opponent) {
		List<Integer> computerTakenFields = computer.getFieldsTaken();
		List<Integer> opponentTakenFields = opponent.getFieldsTaken();
		List<List<Integer>> possibleWinCombinations = new ArrayList<>();
		for (List<Integer> winCombination : winsCombinations) {
			boolean isComputerHasAnyFieldFromWinCombination = winCombination.stream()
					.anyMatch(computerTakenFields::contains);
			if (isComputerHasAnyFieldFromWinCombination) {
				boolean isOpponentHasAnyFieldFromWimCombination = winCombination.stream()
						.anyMatch(opponentTakenFields::contains);
				if (!isOpponentHasAnyFieldFromWimCombination) {
					possibleWinCombinations.add(winCombination);
				}
			}
		}
		if (possibleWinCombinations.size() == 0) {
			return 0;
		}

		int computerChoice = 0;
		if (possibleWinCombinations.size() == 1) {
			List<Integer> possibleWinCombination = possibleWinCombinations.get(0);
			computerChoice = possibleWinCombination.stream()
					.filter(a -> board.getCornerFields().contains(a))
					.findFirst()
					.orElse(0);
			if (computerChoice == 0) {
				for (Integer fieldInThisWinCombination : possibleWinCombination) {
					if (board.getTakenFields().get(fieldInThisWinCombination) == null) {
						return fieldInThisWinCombination;
					}
				}
			}
		} else {
			for (List<Integer> possibleWinCombination : possibleWinCombinations) {
				List<Integer> cornerFieldsInThisCombination = possibleWinCombination.stream()
						.filter(i -> board.getCornerFields().contains(i))
						.toList();
				computerChoice = cornerFieldsInThisCombination.stream()
						.filter(i -> !computerTakenFields.contains(i))
						.findFirst()
						.orElse(0);
				if (computerChoice != 0) {
					return computerChoice;
				}
			}

			List<Integer> possibleWinCombination = possibleWinCombinations.get(secureRandom.nextInt(possibleWinCombinations.size()));
			computerChoice = possibleWinCombination.stream()
					.filter(i -> !computerTakenFields.contains(i))
					.findFirst()
					.orElse(0);
		}
		return computerChoice;
	}

	private int choiceCornerField() {
		int randomInt = secureRandom.nextInt(board.getCornerFields().size());
		return board.getCornerFields().get(randomInt);
	}

	private boolean checkIfTie() {
		return board.getAvailableFields().size() == 0;
	}

	private void checkIfPlayerWonGame(Player player) {
		List<Integer> playerTakenFields = player.getFieldsTaken();
		for (List<Integer> winCombination : winsCombinations) {
			if (new HashSet<>(playerTakenFields).containsAll(winCombination)) {
				this.winner = player;
				this.winnerWinCombination = winCombination;
			}
		}
	}

	private void gameEndsMessage() {
		if (winner.isComputer()) {
			System.out.println("Computer has won the game. Win combination: " + winnerWinCombination);
		} else {
			System.out.println("Player with " + winner.getSign() + " sign has won the game. Win combination: " + winnerWinCombination);
		}
	}

	private List<Player> buildPlayers(boolean withComputer) {
		Player p1 = new Player("X", false);
		Player secondPlayer;
		if (withComputer) {
			secondPlayer = new Player("O", true);
		} else {
			secondPlayer = new Player("O", false);
		}
		List<Player> currPlayers = new ArrayList<>(List.of(p1, secondPlayer));
		Collections.shuffle(currPlayers);
		return currPlayers;
	}

	private List<List<Integer>> buildWinsCombinations() {
		return List.of(
				List.of(1, 2, 3),
				List.of(4, 5, 6),
				List.of(7, 8, 9),
				List.of(1, 4, 7),
				List.of(2, 5, 8),
				List.of(3, 6, 9),
				List.of(1, 5, 9),
				List.of(3, 5, 7));
	}
}
