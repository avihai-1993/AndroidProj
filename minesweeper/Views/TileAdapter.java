package com.example.minesweeper.Views;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.minesweeper.Logic.Board;
import com.example.minesweeper.Logic.Tile;

public class TileAdapter extends BaseAdapter {

    private static final String TAG = "Tile Adapter";
    private Context mContext;
    private Board mBoard;

    public TileAdapter(Context context, Board board) {
        mContext = context;
        mBoard = board;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TileView tileView;

        tileView = (TileView) convertView;

        if (tileView == null) {

            tileView = new TileView(mContext);
        }

        int x = position % mBoard.getBoardSize();
        int y = position / mBoard.getBoardSize();

        Tile tile = mBoard.getTile(y, x);
        tileView.setText("");
        tile.setTileView(tileView);

        return tileView;
    }

    @Override
    public Object getItem(int position) {
        int x = position % mBoard.getBoardSize();
        int y = position / mBoard.getBoardSize();

        return mBoard.getTile(y, x);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return mBoard.getBoardSize() * mBoard.getBoardSize();
    }
}
