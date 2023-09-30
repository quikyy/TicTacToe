package org.tictactoe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Board {

	private List<Integer> allFields = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
	private List<Integer> availableFields = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
	private List<Integer> cornerFields = List.of(1, 3, 7, 9);
	private List<List<Integer>> oppositeCornerFields = List.of(List.of(1, 9), List.of(3, 7));

	private Map<Integer, Player> takenFields = new HashMap<>();

	public void drawBoard() {
		System.out.println("");
		System.out.println("Board:");
		System.out.println();

		String fieldNumber1 = takenFields.get(1) == null ? "-" : takenFields.get(1).getSign();
		drawField(fieldNumber1);
		String fieldNumber2 = takenFields.get(2) == null ? "-" : takenFields.get(2).getSign();
		drawField(fieldNumber2);
		String fieldNumber3 = takenFields.get(3) == null ? "-" : takenFields.get(3).getSign();
		drawField(fieldNumber3);

		System.out.println();

		String fieldNumber4 = takenFields.get(4) == null ? "-" : takenFields.get(4).getSign();
		drawField(fieldNumber4);
		String fieldNumber5 = takenFields.get(5) == null ? "-" : takenFields.get(5).getSign();
		drawField(fieldNumber5);
		String fieldNumber6 = takenFields.get(6) == null ? "-" : takenFields.get(6).getSign();
		drawField(fieldNumber6);

		System.out.println();

		String fieldNumber7 = takenFields.get(7) == null ? "-" : takenFields.get(7).getSign();
		drawField(fieldNumber7);
		String fieldNumber8 = takenFields.get(8) == null ? "-" : takenFields.get(8).getSign();
		drawField(fieldNumber8);
		String fieldNumber9 = takenFields.get(9) == null ? "-" : takenFields.get(9).getSign();
		drawField(fieldNumber9);

		System.out.println();
		System.out.println();

	}

	public boolean takeField(int fieldId, Player player) {
		if (availableFields.contains(fieldId)) {
			takenFields.put(fieldId, player);
			player.getFieldsTaken().add(fieldId);
			buildAvailableFields();
			return true;
		}
		return false;
	}

	private void drawField(String fieldNumber) {
		System.out.print(fieldNumber);
		System.out.print(" | ");
	}

	private void buildAvailableFields() {
		List<Integer> newAvailableFields = new ArrayList<>();
		for (Integer field : allFields) {
			if (takenFields.get(field) == null) {
				newAvailableFields.add(field);
			}
		}
		setAvailableFields(newAvailableFields);
	}

}
