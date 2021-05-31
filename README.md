### TicTacToe game

#### **Requirements:**

Implement the TicTacToe ('three in a row') game:
 - there is an empty board, size 3x3;
 - played by 2 players, each marking a cell of the board with his mark
   ('X' or '0'), taking turns;
 - first one which has 3 of his marks in a line (in any direction) wins;
 - if table fills up first, it's a tie (nobody wins).

___
a) Implement the basic game first:
 - should be able to play 1 game; 
 - for displaying the board after each move: use a simple text mode UI, 
   just display the rows each on a separate line, with cell contents
   separated by one space, each cell shown as: 'X', 'O' or '_' (for empty);
 - get the position which the player want to mark by reading from console
   2 integer values (one for the row and one for the column of the cell);
 - should validate the attempted move is valid;
 - should detect when game ends, and display the winner.

___
b) Improve the user experience:
 - allow players to specify the cell by a single string value like `'A2'`,
   where the first letter ('A'..'C') represents the row, and the number 
   after it ('1'..'3') represents the column;  
 - for better visualization, display the column number (1..3) above
   each of the board columns (like a header row), and the row letter (A..C)
   in front of each row.

___
c) Make the player names configurable:
 - read them at the start of game;
 - display them when reading the next move, and on final results.

___
d) Support playing multiple games in a row:

 - at the end of one game, ask the user if he wants to continue with
   another game (Y/N);
 - if he chooses Y, start a new game (resetting the board, etc);
 - you need to keep the **overall score** (considering all played games),
   which you should update and display at the end of each game;
 - the **starting player** should alternate between games: first game stars
   with Player1, next one with Player2, next one with Player1 again etc..

___
e) **[Optional]** Support boards of arbitrary size:
 - at the start of the program, ask the user for the __board size__, which
   must be a number between 3 and 26 (may use a default like 3)
 - it should then work with a board of that size (display it, read moves..)
 - the rule for winning should be adapted:
   - for board size <= 4: player wins with 3 in a row;
   - for board size >= 5: player wins with 5 in a row.

___
f) **[Optional]**  
  Improve your display of the board by using the special ASCII characters
  for displaying margins and corners. See: 
  [table of ascii codes](https://theasciicode.com.ar/extended-ascii-code/box-drawings-single-vertical-line-character-ascii-code-179.html)
   
  You can copy-paste them each from that page, and they should allow you
  to draw tables with nice margins/corners, like this (note that you should
  still include the numbers/letters indications near each row/column):  
 
```
  ┌───┬───┬───┐
  │ X │ O │ X │  
  ├───┼───┼───┤  
  │ X │ O │ X │  
  ├───┼───┼───┤  
  │ O │ X │ X │  
  └───┴───┴───┘
```  

___
