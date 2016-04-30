package team.fcisquare;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Andrew on 4/29/2016.
 */
public class Comment implements Serializable {
    private int id;
    private String body;
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Double date) {
        this.date = new Date(String.valueOf(date));
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
