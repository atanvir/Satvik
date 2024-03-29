package com.satvick.model;

import androidx.annotation.NonNull;

public class Notes {

    private String to;
    private String from;
    private String heading;
    private String body;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @NonNull
    @Override
    public String toString() {
        return "to : "+this.to+"\n " +
                "from: " +this.from+"\n"+
                "heading: " +this.heading+"\n"+
                "body: " +this.body;
    }
}
