package com.yegor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board implements IBoard {
    private final int rows = 10;
    private final int cols = 10;
    private final Cell[][] board = new Cell[rows][cols];
    private int lives = 0;
    private List<Integer[]> shipLives = new ArrayList<>();
    private List<List<Integer[]> > ships = new ArrayList<>();



    public Board() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                this.board[i][j] = new Cell(i,j,State.EMPTY);
            }
        }
    }


    public void drawToPlayer() {
        System.out.println("  | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 |");
        System.out.println("--+---+---+---+---+---+---+---+---+---+---|");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            sb.delete(0, sb.length());
            sb.append(i).append(" |");
            for (int j = 0; j < board[i].length; j++) {
                if (this.board[i][j].getState() == State.HIT) {
                    sb.append("\033[31;1m X\033[0m |");
                } else if (this.board[i][j].getState() == State.SHIP) {
                    sb.append("\u001B[32m O \033[0m|");
                } else if (this.board[i][j].getState() == State.MISS) {
                    sb.append(" * |");
                } /*else if (this.board[i][j].getState() == State.AREA) {
                    sb.append(" # |");
                }*/ else {
                    sb.append(" \033[34m~ \033[0m|");
                }
            }
            System.out.println(sb.toString());
            System.out.println("--+---+---+---+---+---+---+---+---+---+---|");
        }
    }



    public void drawToEnemy(Board enemy) {
        System.out.println("  | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 |");
        System.out.println("--+---+---+---+---+---+---+---+---+---+---|");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            sb.delete(0, sb.length());
            sb.append(i).append(" |");
            for (int j = 0; j < board[i].length; j++) {
                if (enemy.board[i][j].getState() == State.HIT) {
                    sb.append("\033[31;1m X\033[0m |");
                } else if (enemy.board[i][j].getState() == State.SHIP) {
                    sb.append(" \033[34m~ \033[0m|");
                } else if (enemy.board[i][j].getState() == State.MISS) {
                    sb.append(" * |");
                } /*else if (this.board[i][j].getState() == State.AREA) {
                    sb.append(" # |");
                }*/ else {
                    sb.append(" \033[34m~ \033[0m|");
                }
            }
            System.out.println(sb.toString());
            System.out.println("--+---+---+---+---+---+---+---+---+---+---|");
        }
    }

    public boolean attack(Board enemy, int row, int col) {
        if (enemy.board[row][col].getState() == State.SHIP) {
            enemy.board[row][col].setState(State.HIT);
            enemy.lives--;
            return true;
        } else if (enemy.board[row][col].getState() != State.SHIP && enemy.board[row][col].getState() != State.HIT) {
            enemy.board[row][col].setState(State.MISS);
            return false;
        }
        return false;
    }

//    private void decreaseShipHealth(Board enemy, int row, int col) {
//        if (enemy.board[row][col].getState() == State.HIT) {
//
//            for (int i=0; i < enemy.ships.size(); i++) {
//                for (int j = 0; j < enemy.ships.get(i).size(); j++) {
//                    System.out.println("heck");
//                }
//            }
//        }
//    }

    private void autoPosition(int size) {
        boolean a = false;
        while (!a) {
            a = fits(size);
        }
    }


    /**
     * @param size - size of the ship.
     * @return - returns true if ship can fit on a board.
     */


    private boolean fits(int size) {
        Random r = new Random();
        int row = r.nextInt(rows);
        int col = r.nextInt(cols);
        boolean vertical = r.nextBoolean();

        if ((vertical && row + size >= rows) || (!vertical && col + size >= cols)) return false;

        for(int i = 0; i < size; i++){
            if (vertical) {
                if (board[row + i][col].getState() != State.EMPTY) return false;
            } else if (board[row][col + i].getState() != State.EMPTY) return false;
        }

        addShip(row, col, size, vertical);
        return true;
    }

    /**
     *
     * @param row - row on a board
     * @param col - col on a board
     * @param size - size of the ship that is going to be placed on a board
     * @param vertical - boolean to determine direction of the ship, if TRUE - vertical, FALSE - horizontal
     */

    public void addPlayerShip (int row, int col, int size, boolean vertical) {
        if ((vertical && row + size > rows) || (!vertical && col + size > cols)) return;

        for(int i = 0; i < size; i++){
            if (vertical) {
                if (board[row + i][col].getState() != State.EMPTY) {
                    System.out.println("Invalid ship location! Pls try again");
                    return;
                }
            } else if (board[row][col + i].getState() != State.EMPTY) {
                System.out.println("Invalid ship location! Pls try again");
                return;
            }
        }


        addShip(row, col, size, vertical);
        setLives();
    }



    private void addShip(int row, int col, int size, boolean vertical) {
            if (!vertical) {
                List<Integer[]> coordinates = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    board[row][col + i].setState(State.SHIP);
                    addShipArea(row, col + i);
                    setCoordinates(row, col);
                    board[row][col + i].setHealth(size);
                }
                ships.add(coordinates);

            } else {
                List<Integer[]> coordinates = new ArrayList<>();
                for (int i = 0; i < size; i++){
                    board[row + i][col].setState(State.SHIP);
                    addShipArea(row + i, col);
                    setCoordinates(row, col);
                    coordinates.add(new Integer[] {row + i, col});
                    board[row + i][col].setHealth(size);
                }
                ships.add(coordinates);
            }
    }

    private void addShipArea(int row, int col) {
            for (int i = row - 1; i <= row + 1; i++) {
                for (int j = col - 1; j <= col + 1; j++) {
                    Cell cell = neighbor(i, j);
                    if (cell != null && cell.getState() == State.EMPTY)
                        board[i][j].setState(State.AREA);
                }
            }
    }

    private Cell neighbor(int row, int col) {
        if (row >= 0 && row < rows && col >= 0 && col < cols)
            return board[row][col];
        return null;
    }

    private void setCoordinates(int row, int col) {
        board[row][col].setCol(col);
        board[row][col].setRow(row);
    }

    void deployRandom() {
        autoPosition(5);
        autoPosition(4);
        autoPosition(3);
        autoPosition(3);
        autoPosition(2);

        setLives();
    }

    public int getLives() {
        return lives;
    }

    private void setLives() {
        int sum = 0;
        for (int i=0; i < ships.size(); i++) {
            sum += ships.get(i).size();
        }
        this.lives = sum;
    }

    public void logShips(int row, int col) {

        System.out.println(this.board[row][col].getHealth());

        for (int i = 0; i < ships.size(); i++) {
            for (int j = 0; j < ships.get(i).size(); j++) {
                System.out.println();
            }
        }
    }

    public int getShipsLength() {
        return ships.size();
    }
}