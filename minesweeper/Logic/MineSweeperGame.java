package com.example.minesweeper.Logic;

import java.util.Scanner;

//this can be the game Activity
public class MineSweeperGame implements GameStatusListener{

    private Board board;

    public MineSweeperGame(Diff diff){
        switch (diff)
        {
            case easy:
                board = new Board(4,3);
                break;
            case madium:
                board = new Board(8,12);
                break;
            case Hard:
                board = new Board(9,24);
                break;
            default:
                System.out.println("No game can Play");
        }

        board.setGameStatusListener(this);
    }

    public void startGame(){
        Scanner scanner = new Scanner(System.in);
        int i,j;
        showGameStatus();
        while (true){
            choosePlay();
            switch (scanner.nextInt()){
                case 1:
                    System.out.println("give i :");
                    i = scanner.nextInt();
                    System.out.println("give j :");
                    j = scanner.nextInt();
                    flag(i,j);
                    break;
                case 2:
                    System.out.println("give i :");
                    i = scanner.nextInt();
                    System.out.println("give j :");
                    j = scanner.nextInt();
                    reveal(i,j);
                    break;
                case 3:
                    System.exit(6);
            }
            showGameStatus();
        }
    }

    private void choosePlay(){
        System.out.println("flag 1 / reveal 2 / exit 3");
    }


    public void showGameStatus(){
        System.out.println(board);
    }

    public void flag(int i , int j){
        board.flagTile(i,j);
    }  //onClick

    public void reveal(int i , int j ){
        board.revealedTile(i,j);
    } //ondoubleClick

    private void startClock(){

        // give the intraction to start th Chronemeter
    }


    @Override
    public void onMineStep() {
        System.out.println("BOOOOOM");
        System.exit(0);
    }

    // winning the game all the board is finished
    @Override
    public void onGameFinish() {
        System.out.println("WanderFull");
        System.exit(0);
    }
}


enum Diff{ Hard , easy , madium }