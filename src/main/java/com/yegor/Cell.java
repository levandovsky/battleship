package com.yegor;

public class Cell {

    private int row;
    private int col;
    private State state;
    public int health;


    public Cell(int row, int col, State state) {
        this.row = row;
        this.col = col;
        this.state = state;
    }


    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }



    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

}