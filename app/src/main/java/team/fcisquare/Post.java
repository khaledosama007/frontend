package team.fcisquare;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Andrew on 4/29/2016.
 */
public class Post implements Serializable {
    private int id;
    private Date date;
    private String body;
    private int likes;
    private Place place;
    private ArrayList<Comment> comments;
    private boolean isLikedByUser = false;

    public boolean isLikedByUser() {
        return isLikedByUser;
    }

    public void setLikedByUser(boolean likedByUser) {
        isLikedByUser = likedByUser;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = new Date(date);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

}
