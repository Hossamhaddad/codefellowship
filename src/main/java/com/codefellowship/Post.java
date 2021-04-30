package com.codefellowship;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    ApplicationUser user;
    private String body;
    private String timeStamp;


    public Post() {
    }

    public Post(String body, String timeStamp, ApplicationUser user) {
        this.body = body;
        this.timeStamp = timeStamp;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }


    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
