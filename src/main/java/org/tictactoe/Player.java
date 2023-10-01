package org.tictactoe;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Player {

	private String sign;
	private List<Integer> fieldsTaken = new ArrayList<>();
	private boolean isComputer;

	public Player(String sign, boolean isComputer) {
		this.sign = sign;
		this.isComputer = isComputer;
	}
}
