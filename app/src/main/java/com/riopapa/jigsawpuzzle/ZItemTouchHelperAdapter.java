package com.riopapa.jigsawpuzzle;

public interface ZItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);

    void onItemSwiped(int position);
}