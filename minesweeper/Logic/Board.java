package com.example.minesweeper.Logic;

import java.util.ArrayList;
import java.util.Random;

public class Board {

    private Tile[][] tiles;
    private int size;
    private int revealedTiels;
    private int numOfMines;
    private Random random = new Random();
    private GameStatusListener gameStatusListener;

    public Board(int size, int numOfMines) {
        setSize(size);
        setNumOfMines(numOfMines);
        boolean[][] matMineHelper = genareteMineCoordinates();
        this.tiles = new Tile[size][size];
        for (int i = 0; i < this.tiles.length; i++) {
            for (int j = 0; j < this.tiles[i].length; j++) {
                this.tiles[i][j] = new Tile(i, j, this, matMineHelper[i][j]);
            }
        }

        for (int i = 0; i < this.tiles.length; i++) {
            for (int j = 0; j < this.tiles[i].length; j++) {
                this.tiles[i][j].checkAdjTile();
            }
        }
    }

    public void setGameStatusListener(GameStatusListener gameStatusListener) {
        this.gameStatusListener = gameStatusListener;
    }

    public void increamentNumOfMines(){
        numOfMines++;
    }
    public void resetBoardAdjMineCount()
    {
        for (int i = 0; i <size ; i++) {
            for (int j = 0; j <size ; j++) {
                if(!tiles[i][j].isHasMine()){
                    tiles[i][j].setNumOfAdjMine(0);
                }else {
                    tiles[i][j].setNumOfAdjMine(-1);
                }
            }
        }

    }



    public void calNewBoardFromMines(){
        for (int i = 0; i < size; i++) {
            for (int j = 0; j <size ; j++) {
                if(tiles[i][j].isHasMine()){
                    calcHelper(getTile(i-1,j-1));
                    calcHelper(getTile(i - 1, j));
                    calcHelper(getTile(i-1,j+1));
                    calcHelper( getTile(i,j-1));
                    calcHelper( getTile(i,j+1));
                    calcHelper(getTile(i+1,j-1));
                    calcHelper(getTile(i+1,j));
                    calcHelper(getTile(i+1,j+1));
                }
            }
        }
    }

    private void calcHelper(Tile tile){
        if(tile != null && !tile.isHasMine())
        {
            tile.incramentNumOfAdjMine();
        }
    }



    private ArrayList<int[]> coordinates() {
        ArrayList<int[]> cor = new ArrayList<>();
        int index = 0;
        for (int j = 0; j < size; j++) {
            for (int k = 0; k < size; k++) {
                cor.add(new int[2]);
                cor.get(index)[0] = j;
                cor.get(index)[1] = k;
                index++;
            }
        }
        return cor;
    }

    private boolean[][] genareteMineCoordinates() {
        boolean[][] matrix = new boolean[size][size];
        ArrayList<int[]> matCor = coordinates();
        int index;
        for (int i = 0; i < numOfMines; i++) {
            index = random.nextInt(matCor.size() - 1);
            matrix[matCor.get(index)[0]][matCor.get(index)[1]] = true;
            matCor.remove(index);

        }
        return matrix;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setNumOfMines(int numOfMines) {
        this.numOfMines = numOfMines;
    }


    public int getNumOfMines() {
        return numOfMines;
    }

    //can be change if you want
    public int calculateScore() {
        return revealedTiels;
    }

    public void flagTile(int i, int j) {
        if (!this.tiles[i][j].isRevealed()) {
            this.tiles[i][j].setFlagged(true);
        }
    }

    public Tile getTile(int row, int col) {
        try {
            return this.tiles[row][col];
        } catch (Exception e) {
            return null;
        }
    }

    public void revealedTile(int i, int j) {
        if (!this.tiles[i][j].isRevealed()) {
            if (this.tiles[i][j].isHasMine()) {
                this.tiles[i][j].setRevealed(true);

                return;
            }
            this.tiles[i][j].setRevealed(true);

            if (this.tiles[i][j].getNumOfAdjMine() == 0) {
                revealedledTileLoop(i, j);
            }
        }

    }

    private void revealedledTileLoop(int i, int j) {
        for (int offsetRow = -1; offsetRow <= 1; offsetRow++) {
            for (int offsetCol = -1; offsetCol <= 1; offsetCol++) {
                if (!(offsetRow == 0 && offsetCol == 0)) {
                    Tile temp = getTile(i + offsetRow, j + offsetCol);
                    if (temp != null && !(temp.isRevealed())) {
                        if (temp.getNumOfAdjMine() == 0) {
                            temp.setRevealed(true);
                            revealedledTileLoop(i + offsetRow, j + offsetCol);
                        } else if (temp.getNumOfAdjMine() > 0) {
                            temp.setRevealed(true);

                        }
                    }
                }
            }
        }
    }


    public void increaseNumOfRevaledTiles() {
        this.revealedTiels++;
    }

    public boolean isAllBoardRevealed() {
        return (size * size - numOfMines == this.revealedTiels);
    }

    public int getBoardSize() {
        return size;
    }

    @Override
    public String toString() {

        for (int i = 0; i < this.tiles.length; i++) {
            for (int j = 0; j < this.tiles[i].length; j++) {
                System.out.print(this.tiles[i][j] + "         ");
            }
            System.out.println();
        }
        return "";
    }
}
