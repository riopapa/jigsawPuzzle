package com.riopapa.zigsawpuzzle;

public interface ZItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);

    void onItemSwiped(int position);
}