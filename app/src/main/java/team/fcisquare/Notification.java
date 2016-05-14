package team.fcisquare;

import java.util.Date;

/**
 * Created by lenovo on 25/04/2016.
 */
public class Notification {
    private Integer id;
    private String description;
    private Integer type;
    private Date date;
    private Integer target;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }

    public Integer getId() {
        return id;

    }

    public String getDescription() {
        return description;
    }

    public Integer getType() {
        return type;
    }

    public Date getDate() {
        return date;
    }

    public Integer getTarget() {
        return target;
    }
}
