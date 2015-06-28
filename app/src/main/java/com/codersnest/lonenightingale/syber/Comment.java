package com.codersnest.lonenightingale.syber;

/**
 * Created by Arash on 6/1/2015.
 */
public class Comment extends Post{

    private int numberOfReplies;
    private Reply[] replies;

    public Comment (int postId, int authorId, String text, String author, String time, String date, int numberOfLikes, int numberOfHashTags, int numberOfReplies) {
        this.text = text;
        this.author = author;
        this.time = time;
        this.date = date;
        this.postId = postId;
        this.authorId = authorId;
        this.numberOfLikes = numberOfLikes;
        this.numberOfHashTags = numberOfHashTags;
        this.numberOfReplies = numberOfReplies;
    }

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
