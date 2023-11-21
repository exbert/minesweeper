import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Minesweeper {

    private int SIZE;
    private int MINES;

    private char[][] board;
    private char[][] mineField;

    public Minesweeper() {
        getBoardSize();
        board = new char[SIZE][SIZE];
        mineField = new char[SIZE][SIZE];
        initializeBoard();
        placeMines();
        placeNumbers();
    }

    private void getBoardSize() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Mayın Tarlası büyüklüğünü girin: ");
        SIZE = scanner.nextInt();
        setMineAmount();
    }

    private void setMineAmount() {
        MINES = SIZE / 4;
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
        System.out.print("  ");
        for (int k = 0; k < SIZE; k++) System.out.print(k + " " );

        System.out.println();

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

        System.out.println("Mayın Tarlası Oyuna Hoşgeldiniz !");
        while (true) {
            printBoard();

            System.out.print("Satır ve Sütün olarak giriş yapınız (örn., 3 4): ");
            int row = scanner.nextInt();
            int col = scanner.nextInt();

            if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
                System.out.println("Geçersiz Konum. Lütfen geçerli konum giriniz.");
                continue;
            }

            if (uncoverCell(row, col)) {
                printBoard();
                System.out.println("Oyun bitti! Mayına çarptınız.");
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
                System.out.println("Tebrikler! Tüm mayınları buldunuz.");
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
