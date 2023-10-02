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

	private final List<Integer> allFields = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
	private final List<Integer> cornerFields = List.of(1, 3, 7, 9);
	private List<Integer> availableFields = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
	private Map<Integer, Player> takenFields = new HashMap<>();

	public void draw() {
		System.out.println();
		System.out.println("Game board:");
		System.out.println();
		for (Integer field : allFields) {
			if (field == 4 || field == 7) {
				System.out.println();
			}
			String fieldSign = takenFields.get(field) == null ? "-" : takenFields.get(field).getSign();
			drawField(fieldSign);
		}
		System.out.println();
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
