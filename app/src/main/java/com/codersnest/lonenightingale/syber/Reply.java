package com.codersnest.lonenightingale.syber;

/**
 * Created by Arash on 6/1/2015.
 */
public class Reply extends Post{

    private int depth;
    private Reply[] replies;

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public Reply[] getReplies() {
        return replies;
    }

    public void setReplies(Reply[] replies) {
        this.replies = replies;
    }
}
