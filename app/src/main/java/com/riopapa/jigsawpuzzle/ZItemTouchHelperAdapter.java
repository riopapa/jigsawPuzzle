package com.riopapa.jigsawpuzzle;

public interface ZItemTouchHelperAdapter {

    abstract boolean onItemMove(int fromPosition, int toPosition);

    void onItemSwiped(int position);
}