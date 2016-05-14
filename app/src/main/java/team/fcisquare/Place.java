package team.fcisquare;

import java.io.Serializable;

/**
 * Created by lenovo on 21/04/2016.
 */
public class Place implements Serializable {
    private String name;
    private String description;
    private Integer id;
    private Double lon;
    private Double lat;
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getId() {
        return id;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }



    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

}
