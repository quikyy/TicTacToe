package org.tictactoe;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Player {

	private String sign;
	private List<Integer> fieldsTaken;
	private boolean isComputer;

}
