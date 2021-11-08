package com.example.minesweeper.Logic;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.example.minesweeper.R;
import com.example.minesweeper.Views.TileView;

public class Tile {

    private int numOfAdjMine;
    private boolean hasMine;
    private boolean isFlagged;
    private boolean isRevealed;
    private int row;
    private int col;
    private Board host;
    private TileView tileView;

    public Tile(int row, int col, Board host, boolean hasMine) {
        setRow(row);
        setCol(col);
        this.host = host;
        this.hasMine = hasMine;
        this.numOfAdjMine = 0;
    }


    public void setTileView(TileView tileView) {
        this.tileView = tileView;
    }

    @SuppressLint("ResourceAsColor")
    public void notifyTileView(){
        if(isRevealed && !isFlagged){
            if(isHasMine()){
                tileView.setText("M");
            }else {
                if(numOfAdjMine > 0){
                    tileView.setText(numOfAdjMine+"");
                }else if(numOfAdjMine == 0) {
                    tileView.setText("");
                }
                tileView.setBackgroundColor(R.color.colorLightGray);
            }
        }
        else if (isFlagged){
            tileView.setText("F");
        }
        else if (!isRevealed()){
            tileView.setText("");
            tileView.setBackgroundColor(Color.DKGRAY);
        }

        else {
            tileView.setText("");
        }

    }

    public void setNumOfAdjMine(int numOfAdjMine) {
        this.numOfAdjMine = numOfAdjMine;
    }
    public void incramentNumOfAdjMine(){
        this.numOfAdjMine++;
    }

    public void setHasMine(boolean hasMine) {
        this.hasMine = hasMine;

    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public boolean isHasMine() {
        return hasMine;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getNumOfAdjMine() {
        return numOfAdjMine;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed(boolean revealed) {
            isRevealed = revealed;
            if (!this.isHasMine() && revealed) {
                host.increaseNumOfRevaledTiles();
            }

    }

    private void checkHelper(int row, int col) {
        if (this.host.getTile(row, col) != null && this.host.getTile(row, col).isHasMine()) {
            this.numOfAdjMine++;
        }
    }

    public void checkAdjTile() {
        if (!this.isHasMine()) {
            checkHelper(this.row - 1, this.col - 1);
            checkHelper(this.row - 1, this.col);
            checkHelper(this.row - 1, this.col + 1);
            checkHelper(this.row, this.col - 1);
            checkHelper(this.row, this.col + 1);
            checkHelper(this.row + 1, this.col - 1);
            checkHelper(this.row + 1, this.col);
            checkHelper(this.row + 1, this.col + 1);
        } else {
            this.numOfAdjMine = -1;
        }
    }


    @Override
    public String toString() {
        return "[" + this.row + "][" + this.col + "]/N:" + this.numOfAdjMine + "/R:" + this.isRevealed + "/F:" + this.isFlagged;
    }
}
