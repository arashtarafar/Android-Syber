package com.codersnest.lonenightingale.syber;

/**
 * Created by Arash on 6/1/2015.
 */
public class Comment extends Post{

    private int numberOfReplies;
    private Reply[] replies;

    public int getNumberOfReplies() {
        return numberOfReplies;
    }

    public Reply[] getReplies() {
        return replies;
    }

    public void setReplies(Reply[] replies) {
        this.replies = replies;
    }

    public void setNumberOfReplies(int numberOfReplies) {
        this.numberOfReplies = numberOfReplies;
    }
}
