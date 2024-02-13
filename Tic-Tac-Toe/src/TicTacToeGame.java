import java.util.Scanner;

// Holds the GameBoard for the and start the game
class PlayGame {
  static GameBoard gameBoard;

  public static void main(String[] args) {
    gameBoard = new GameBoard(new LetterX());

    // Plays the command line version of the game
    gameBoard.nextTurnCommandLine();

    // Plays the UI version of the game
    // gameBoard.nextTurnUI();
  }
}

class GameBoard {
  ILetter[][] board; // Will be 3 on 3 board
  ILetter playerTurn; // Turn of the current Player

  GameBoard(ILetter playerTurn) {
    this.playerTurn = playerTurn;

    board = new ILetter[3][3];

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        board[i][j] = new EmptyLetter();
      }
    }
  }

  // Based on the number value of the square 0-8, gets the row the square is in
  public int getRow(int sqrVal) {
    return sqrVal / 3;
  }

  // Based on the number value of the square 0-8, gets the column the square is in
  public int getCol(int sqrVal) {
    return sqrVal % 3;
  }

  // Checks if all 3 given Letters are the same Letter as the current playerTurn
  public boolean check3ForWin(ILetter l1, ILetter l2, ILetter l3) {
    return this.playerTurn.sameLetter(l1) && this.playerTurn.sameLetter(l2)
        && this.playerTurn.sameLetter(l3);
  }

  // Checks if there is a winner
  public boolean isWinner() {
    return this.check3ForWin(board[0][0], board[0][1], board[0][2]) || // Row 0
        this.check3ForWin(board[1][0], board[1][1], board[1][2]) || // Row 1
        this.check3ForWin(board[2][0], board[2][1], board[2][2]) || // Row 2
        this.check3ForWin(board[0][0], board[1][0], board[2][0]) || // Col 0
        this.check3ForWin(board[0][1], board[1][1], board[2][1]) || // Col 1
        this.check3ForWin(board[0][2], board[1][2], board[2][2]) || // Col 2
        this.check3ForWin(board[0][0], board[1][1], board[2][2]) || // Diagonal down right
        this.check3ForWin(board[0][2], board[1][1], board[2][0]); // Diagonal up left

  }

  // Changes the turn from X to O or O to X
  public ILetter changeTurn() {
    if (this.playerTurn instanceof LetterX) {
      return new LetterO();
    } else {
      return new LetterX();
    }
  }

  // Updates the board variable
  public void updateBoard(int sqrVal) {
    this.board[getRow(sqrVal)][getCol(sqrVal)] = playerTurn;

    // Checks if there is a winner
    if (isWinner()) {
      // If there is a winner, print the board and tell the players who won
      // Then clear the playerTurn variable
      System.out.println(this.toString());
      System.out.println(playerTurn.toString() + " Wins!");
      this.playerTurn = new EmptyLetter();
      return;
    }

    // Changes the playerTurn variable so the next player's turn can start
    this.playerTurn = this.changeTurn();
  }

  // Runs the next turn. Gets the number of the square to work in, updates the
  // board, then calls itself for the next player's turn
  public void nextTurnCommandLine() {
    // Print current board to console
    System.out.println(this.toString());

    // Create a scanner and request a number
    Scanner scan = new Scanner(System.in);
    System.out.print(playerTurn.toString() + "'s turn: ");

    int squarePicked = scan.nextInt();

    // Fail catch -> number not between 0 and 8
    while (squarePicked < 0 || squarePicked > 8) {
      System.out.print("Invalid Number: ");
      squarePicked = scan.nextInt();
    }

    // Fail catch -> Square already taken
    while (!(this.board[getRow(squarePicked)][getCol(squarePicked)] instanceof EmptyLetter)) {
      System.out.print("Square full: ");
      squarePicked = scan.nextInt();
    }

    // Updates the game board and checks for winner
    this.updateBoard(squarePicked);

    if (!(playerTurn instanceof EmptyLetter)) {
      nextTurnCommandLine();
    }

  }

  // Returns the board as a String
  public String toString() {
    /*
    0 | 1 | 2
    -----------
    3 | 4 | 5
    -----------
    6 | 7 | 8
     */
    String curStr = "";
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        curStr += " ";
        if (this.board[i][j] instanceof EmptyLetter) {
          curStr += Integer.valueOf(i * 3 + j).toString();
        } else {
          curStr += this.board[i][j].toString();
        }
        if (j < 2) {
          curStr += " |";
        }
      }
      if (i < 2) {
        curStr += "\n-----------\n";
      }
    }
    return curStr + "\n";
  }
}

// Represents a Letter X, O, or EmptyLetter
interface ILetter {
  // Determines if this Letter is the same as that Letter
  boolean sameLetter(ILetter that);
}

// Represents the Letter X
class LetterX implements ILetter {
  // Returns "X" as a String representation of LetterX
  public String toString() {
    return "X";
  }

  // Determines if that Letter is also a LetterX
  public boolean sameLetter(ILetter that) {
    return (that instanceof LetterX);
  }

}

// Represents the Letter O
class LetterO implements ILetter {
  // Returns "O" as a String representation of LetterO
  public String toString() {
    return "O";
  }

  // Determines if that Letter is also a LetterO
  public boolean sameLetter(ILetter that) {
    return (that instanceof LetterO);
  }
}

// Represents a Letter that is currently empty
class EmptyLetter implements ILetter {
  // Determines if that Letter is an EmptyLetter
  public boolean sameLetter(ILetter that) {
    return (that instanceof EmptyLetter);
  }

}