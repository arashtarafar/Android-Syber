package com.codersnest.lonenightingale.syber;

/**
 * Created by Arash on 6/1/2015.
 */
public class Post {
    protected String text;
    protected String author;
    protected String time;
    protected String date;

    protected int authorId;
    protected int postId;

    protected int numberOfLikes;
    protected int numberOfHashTags;

    protected String getText () {
        return text;
    }

    protected String getAuthor () {
        return author;
    }

    protected String getTime () {
        return time;
    }

    protected String getDate () {
        return date;
    }

    protected int getNumberOfLikes () {
        return numberOfLikes;
    }

    protected int getNumberOfHashTags () {
        return numberOfHashTags;
    }

    protected int getAuthorId () { return authorId; }

    protected void setAuthorId (int authorId) { this.authorId = authorId; }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    protected void setText (String text) {
        this.text = text;
    }

    protected void setAuthor (String author) {
        this.author = author;
    }

    protected void setTime (String time) {
        this.time = time;
    }

    protected void setDate (String date) {
        this.date = date;
    }

    protected void setNumberOfLikes (int numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    protected void setNumberOfHashTags (int numberOfHashTags) {
        this.numberOfHashTags = numberOfHashTags;
    }
}
