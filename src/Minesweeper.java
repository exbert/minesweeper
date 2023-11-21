import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Minesweeper {

    private static final int SIZE = 10;
    private static final int MINES = 15;

    private char[][] board;
    private char[][] mineField;

    public Minesweeper() {
        board = new char[SIZE][SIZE];
        mineField = new char[SIZE][SIZE];
        initializeBoard();
        placeMines();
        placeNumbers();
    }

    private void initializeBoard() {
        for (char[] row : board) {
            Arrays.fill(row, '*');
        }
    }

    private void placeMines() {
        Random random = new Random();
        int minesPlaced = 0;

        while (minesPlaced < MINES) {
            int x = random.nextInt(SIZE);
            int y = random.nextInt(SIZE);

            if (mineField[x][y] != 'X') {
                mineField[x][y] = 'X';
                minesPlaced++;
            }
        }
    }

    private void placeNumbers() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (mineField[i][j] == 'X') {
                    continue;
                }

                int count = countAdjacentMines(i, j);
                mineField[i][j] = (char) (count + '0');
            }
        }
    }

    private int countAdjacentMines(int x, int y) {
        int count = 0;
        for (int i = Math.max(0, x - 1); i <= Math.min(SIZE - 1, x + 1); i++) {
            for (int j = Math.max(0, y - 1); j <= Math.min(SIZE - 1, y + 1); j++) {
                if (mineField[i][j] == 'X') {
                    count++;
                }
            }
        }
        return count;
    }

    private void printBoard() {
        System.out.println("  0 1 2 3 4 5 6 7 8 9");
        for (int i = 0; i < SIZE; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    private boolean uncoverCell(int x, int y) {
        if (mineField[x][y] == 'X') {
            return true; // Hit a mine, game over
        }

        board[x][y] = mineField[x][y];

        if (mineField[x][y] == '0') {
            // Uncover adjacent cells if the current cell has no adjacent mines
            for (int i = Math.max(0, x - 1); i <= Math.min(SIZE - 1, x + 1); i++) {
                for (int j = Math.max(0, y - 1); j <= Math.min(SIZE - 1, y + 1); j++) {
                    if (board[i][j] == '*') {
                        uncoverCell(i, j);
                    }
                }
            }
        }

        return false; // Game continues
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printBoard();

            System.out.print("Enter row and column (e.g., 3 4): ");
            int row = scanner.nextInt();
            int col = scanner.nextInt();

            if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
                System.out.println("Invalid input. Please enter valid coordinates.");
                continue;
            }

            if (uncoverCell(row, col)) {
                printBoard();
                System.out.println("Game over! You hit a mine.");
                break;
            }

            boolean allSafeCellsUncovered = true;
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (board[i][j] == '*' && mineField[i][j] != 'X') {
                        allSafeCellsUncovered = false;
                        break;
                    }
                }
                if (!allSafeCellsUncovered) {
                    break;
                }
            }

            if (allSafeCellsUncovered) {
                printBoard();
                System.out.println("Congratulations! You uncovered all safe cells.");
                break;
            }
        }

        scanner.close();
    }

    public static void main(String[] args) {
        Minesweeper minesweeper = new Minesweeper();
        minesweeper.play();
    }
}
