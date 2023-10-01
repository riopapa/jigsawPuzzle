package com.riopapa.jigsawpuzzle;

public interface ZItemTouchHelperListener {

    abstract boolean onItemMove(int fromPosition, int toPosition);

    void onItemSwiped(int position);
}