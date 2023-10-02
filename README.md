# TicTacToe

Computer implementation of a paper-and-pencil game for two players who take turns marking the spaces in a three-by-three grid with X or O. The player who succeeds in placing three of their marks in a horizontal, vertical, or diagonal row is the winner. It is a solved game, with a forced draw assuming the best play from both players.
<br> <br>
![core](https://github.com/quikyy/TicTacToe/assets/93215074/4f103d85-4610-4d68-9938-b8cfd50da1fb)

## Technologies Used
- Java 17
  
## Features
The game has three game modes: 
- Player versus Player
- Player versus Computer
- Computer versus Computer

## Setup

### IDE
- Clone the repository into your favorite IDE
- Run Main.java

### CMD
- Move into TicTacToeAI directory
- Run cmd
- `gradle -PmainClass=Main run`

## Win combinations
Player or computer has to have one of the following combinations to be considered the winner.

Horizontally win combinations:
<br> <br>
![horizontalwin](https://github.com/quikyy/TicTacToe/assets/93215074/f4844750-4058-4448-b11a-254d1122ecef)

- [1, 2, 3]
- [4, 5, 6]
- [7, 8, 9]


Vertically win combinations:
<br> <br>
![verticallywin](https://github.com/quikyy/TicTacToe/assets/93215074/e8ac5988-3b30-4793-8e98-4cc1ff0db5c2)

- [1, 4, 7]
- [2, 5, 6]
- [3, 6, 9]

Diagonal win combinations:
<br> <br>
![diagonalwin](https://github.com/quikyy/TicTacToe/assets/93215074/dbefc02e-dbf6-4748-a2e5-8349d461b2bf)

- [1, 5, 9]
- [3, 5, 7]

`If neither player manages to achieve one of the above combinations, the game is considered a tie.`

## Signs
- X
- O

## Player order
The order of players is random. Each player (X, O) has 50% to start as first.

## Game modes description

### Player versus Player
A game between two real players who are not computer players. They choose one of the nine available fields on the board. If the game detects that one of the players has one of the winning combinations in his list of occupied fields, he wins.
<br> <br>
![playervsplayer](https://github.com/quikyy/TicTacToe/assets/93215074/14a3cefc-0470-40da-8c17-959d4634586e)
<br> <br>
### Player versus Computer
A game between player and an invincible computer. The computer is programmed to never lose - minimum goal is a draw. The computer can take advantage of the player's mistakes and win the game.

| Scenario                                                                     | Action                              
| ---------------------------------------------------------------------------- | ----------------------------------------------------------------|
| Computer starts as first                                                     | Computer will choice one of the corner fields [1, 3, 7, 9]      |
| Computer starts as second and center field is taken by player                | Computer will choice one of the corner fields [1, 3, 7, 9]      |
| Computer starts as second and center field is free                           | Computer will take centre field [5]                             |
| Player has 2 [4, 5] field in win combination ex: [4, 5, 6] [DEFEND MOVE]     | Computer will block the player and choice remaining field [6] from win combinations [4, 5, 6] |
| Computer has 2 [4, 5] field in win combination ex: [4, 5, 6] [ATTACK MOVE]   | Computer will attack the player and choice remaining field [6] from win combinations [4, 5, 6] if free. If not - see analyze scenario (this field might be already taken by computer before) |
| Normal gameflow - there is no need to defend or attack right now             | Computer will analyze the board and look for possible win combinations based on current taken fields. Computer will try to take corner field in this combination to minimaze the risk, block the player or has two possible win combinations. |

<br> <br>
![playercscomputer](https://github.com/quikyy/TicTacToe/assets/93215074/a5011365-ebc9-4e28-a6a1-8e1caea618c0)
<br> <br>

### Computer versus Computer
A game between two invincible computer. This game will always end as a tie. If both players play perfectly, every single game is a tie.
<br> <br>
![compvscomp](https://github.com/quikyy/TicTacToe/assets/93215074/e2b399c6-5614-4ff9-a198-cf5eb5ef408c)
<br> <br>
