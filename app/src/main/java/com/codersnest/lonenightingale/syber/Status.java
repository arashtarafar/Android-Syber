package com.codersnest.lonenightingale.syber;

/**
 * Created by Arash on 6/1/2015.
 */
public class Status extends Post {

    private int numberOfComments;
    private int numberOfShares;

    public Status (int authorId, String text, String author, String time, String date, int numberOfLikes, int numberOfHashTags, int numberOfComments, int numberOfShares) {
        this.text = text;
        this.author = author;
        this.time = time;
        this.date = date;
        this.authorId = authorId;
        this.numberOfLikes = numberOfLikes;
        this.numberOfHashTags = numberOfHashTags;
        this.numberOfComments = numberOfComments;
        this.numberOfShares = numberOfShares;
    }

    public int getNumberOfComments() {
        return numberOfComments;
    }

    public void setNumberOfComments(int numberOfComments) {
        this.numberOfComments = numberOfComments;
    }

    public int getNumberOfShares() {
        return numberOfShares;
    }

    public void setNumberOfShares(int numberOfShares) {
        this.numberOfShares = numberOfShares;
    }
}
