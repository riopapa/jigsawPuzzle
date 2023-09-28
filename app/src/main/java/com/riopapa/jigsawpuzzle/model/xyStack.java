package com.riopapa.jigsawpuzzle.model;

import android.util.Log;

public class xyStack {
    int maxSize;
    int top, maxTop;
    int [] stacks;

    public xyStack(int maxSize) {
        this.maxSize = maxSize;
        this.top = 0;
        this.maxTop = 0;
        stacks = new int [maxSize];
    }

    public void push(int x) {
        if (top < maxSize) {
            stacks[top] = x;
            top++;
            if (top > maxTop) {
                maxTop = top;
            }
        }
        else
            Log.w("x5 Stack", "full");
    }

    public int pop() {
        if (top > 0)
            return stacks[--top];
        else
            return 0;
    }
}
