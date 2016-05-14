package team.fcisquare;

import java.io.Serializable;

/**
 * Class used to handle user data
 *
 * @author Andrew Albert
 * @version 1.0
 * @since 24/3/2016
 */

public class User implements Serializable {
    private String name;
    private String email;
    private String pass;
    private Integer id;
    private Double lat;
    private Double lon;

    //// TODO: 3/22/2016 should we add followers here and fill it on login ??

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

}
