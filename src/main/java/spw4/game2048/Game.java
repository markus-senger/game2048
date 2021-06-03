package spw4.game2048;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.Random;

public class Game {
    private static final int size = 4;
    private int countNumbers = 0;
    private int gameBoard[][];
    private Random rand;
    private int score = 0;
    private boolean statusIsWon = false;
    private boolean statusIsOver = false;

    public Game() {
        gameBoard = new int[size][size];
        rand = new Random();
    }

    public Game (Random rand) {
        this();
        this.rand = rand;
    }

    public int getScore() {
        return score;
    }

    public boolean isOver() {
        return statusIsOver;
    }

    public boolean isWon() {
        return statusIsWon;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Formatter fm = new Formatter(sb);
        for(int row = 0; row < size; row ++) {
            for(int col = 0; col < size; col++) {
                if(gameBoard[row][col] == 0) {
                    fm.format("    .");
                }
                else {
                    fm.format("%5d", gameBoard[row][col]);
                }
            }
            fm.format("\n");
        }
        return String.valueOf(sb);
    }

    public void initialize() {
        for(int i = 0; i < 2; i++) {
            spawnNewNumber();
        }
    }

    public void moveLeft() {
        move(Direction.left);
    }

    public void moveRight() {
        move(Direction.right);
    }

    public void moveUp() {
        move(Direction.up);
    }

    public void moveDown() {
        move(Direction.down);
    }

    public int getSize() {
        return gameBoard.length;
    }

    public int[][] getGameBoard() {
        return gameBoard;
    }

    public int getNumberOnPosition(int row, int col) {
        return gameBoard[row][col];
    }

    public int getValueAt(int row, int col) {
        return gameBoard[row][col];
    }

    public int getNumbersCount() {
        return countNumbers;
    }

    public void reset() {
        for(int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                gameBoard[row][col] = 0;
            }
        }
        countNumbers = 0;
    }

    public void setGameBoard(int[][] board) {
        for(int row = 0; row < size; row++) {
            for(int col = 0; col < size; col++) {
                if(board[row][col] != 0) {
                    countNumbers++;
                }
                gameBoard[row][col] = board[row][col];
            }
        }
    }

    public int createRandomNumber(int bound) {
        return rand.nextInt(bound);
    }

    public void move(Direction direction) {
        int prevBoard[][] = copyBoard();
        int treatedFields = 0;
        switch(direction) {
            case up:
                for (int col = 0; col < size; col++) {
                    treatedFields = 0;
                    for(int row = 1; row < size; row++) {
                        if (gameBoard[row][col] != 0) {
                            int curNumber = gameBoard[row][col];
                            gameBoard[row][col] = 0;
                            for(int i = row - 1; i >= treatedFields; i--) {
                                if (i == treatedFields && gameBoard[i][col] == 0) {
                                    gameBoard[i][col] += curNumber;
                                    break;
                                }
                                if(gameBoard[i][col] != 0) {
                                    if (gameBoard[i][col] == curNumber) {
                                        gameBoard[i][col] += curNumber;
                                        handleScore(gameBoard[i][col]);
                                        treatedFields++;
                                        countNumbers--;
                                    }
                                    else {
                                        gameBoard[i + 1][col] += curNumber;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
                break;
            case down:
                for (int col = size - 1; col >= 0; col--) {
                    treatedFields = 0;
                    for(int row = size - 2; row >= 0; row--) {
                        if (gameBoard[row][col] != 0) {
                            int curNumber = gameBoard[row][col];
                            gameBoard[row][col] = 0;
                            for(int i = row + 1; i < size - treatedFields; i++) {
                                if (i == size - 1 - treatedFields && gameBoard[i][col] == 0) {
                                    gameBoard[i][col] += curNumber;
                                    break;
                                }
                                if(gameBoard[i][col] != 0) {
                                    if (gameBoard[i][col] == curNumber) {
                                        gameBoard[i][col] += curNumber;
                                        handleScore(gameBoard[i][col]);
                                        treatedFields++;
                                        countNumbers--;
                                    }
                                    else {
                                        gameBoard[i - 1][col] += curNumber;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
                break;
            case left:
                for(int row = 0; row < size; row++) {
                    treatedFields = 0;
                    for (int col = 1; col < size; col++) {
                        if (gameBoard[row][col] != 0) {
                            int curNumber = gameBoard[row][col];
                            gameBoard[row][col] = 0;
                            for(int i = col - 1; i >= treatedFields; i--) {
                                if (i == treatedFields && gameBoard[row][i] == 0) {
                                    gameBoard[row][i] += curNumber;
                                    break;
                                }
                                if(gameBoard[row][i] != 0) {
                                    if (gameBoard[row][i] == curNumber) {
                                        gameBoard[row][i] += curNumber;
                                        handleScore(gameBoard[row][i]);
                                        treatedFields++;
                                        countNumbers--;
                                    }
                                    else {
                                        gameBoard[row][i + 1] += curNumber;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
                break;
            case right:
                for(int row = size - 1; row >= 0; row--) {
                    treatedFields = 0;
                    for (int col = size - 2; col >= 0; col--) {
                        if (gameBoard[row][col] != 0) {
                            int curNumber = gameBoard[row][col];
                            gameBoard[row][col] = 0;
                            for(int i = col + 1; i < size - treatedFields; i++) {
                                if (i == size - 1 - treatedFields && gameBoard[row][i] == 0) {
                                    gameBoard[row][i] += curNumber;
                                    break;
                                }
                                if(gameBoard[row][i] != 0) {
                                    if (gameBoard[row][i] == curNumber) {
                                        gameBoard[row][i] += curNumber;
                                        handleScore(gameBoard[row][i]);
                                        treatedFields++;
                                        countNumbers--;
                                    }
                                    else {
                                        gameBoard[row][i - 1] += curNumber;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
                break;
        }
        if(!Arrays.deepEquals(prevBoard, gameBoard)) {
            spawnNewNumber();
        }
    }

    private void spawnNewNumber() {
        if(countNumbers < size * size) {
            Position pos = findRandomEmptyPos();
            gameBoard[pos.row][pos.col] = createNewRandomNumber();
            countNumbers++;
        }

        if(countNumbers == size * size && !findIfPossibleMovesExists()) {
            statusIsOver = true;
        }
    }

    private boolean findIfPossibleMovesExists() {
        for(int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if(row - 1 >= 0 && gameBoard[row][col] == gameBoard[row - 1][col]) {
                    return true;
                }
                if(row + 1 < size && gameBoard[row][col] == gameBoard[row + 1][col]) {
                    return true;
                }
                if(col - 1 >= 0 && gameBoard[row][col] == gameBoard[row][col - 1]) {
                    return true;
                }
                if(col + 1 < size && gameBoard[row][col] == gameBoard[row][col + 1]) {
                    return true;
                }
            }
        }
        return false;
    }

    private int createNewRandomNumber() {
        int probability = createRandomNumber(10);
        int number = 0;
        if (probability <= 0) {
            number = 4;
        } else {
            number = 2;
        }

        return number;
    }

    private Position findRandomEmptyPos() {
        ArrayList<Position> emptyPos = findAllEmptyPos();
        Position curPos = emptyPos.get(createRandomNumber(emptyPos.size()));

        if(gameBoard[curPos.row][curPos.col] != 0) {
            throw new InvalidEmptyPosition("Error: Empty Position is not empty!");
        }

        return curPos;
    }

    private ArrayList<Position> findAllEmptyPos() {
        ArrayList<Position> emptyPos = new ArrayList<Position>();
        for(int row = 0; row < size; row++) {
            for(int col = 0; col < size; col++) {
                if(gameBoard[row][col] == 0) {
                    emptyPos.add(new Position(row, col));
                }
            }
        }
        return emptyPos;
    }

    private void handleScore(int value) {
        score += value;
        if(value == 2048) {
            statusIsOver = true;
            statusIsWon = true;
        }
    }

    private int[][] copyBoard() {
        int[][] board = new int[size][size];
        for(int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                board[row][col] = gameBoard[row][col];
            }
        }

        return board;
    }
}
