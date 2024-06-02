package com.controlcash.app.exceptions;

import java.io.Serial;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class ResponseEntityException implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    private Instant moment;
    private String message;
    private String details;

    public ResponseEntityException(Instant moment, String message, String details) {
        this.moment = Instant.parse(simpleDateFormat.format(Date.from(moment)));
        this.message = message;
        this.details = details;
    }

    public Instant getMoment() {
        return this.moment;
    }

    public void setMoment(Instant moment) {
        this.moment = Instant.parse(simpleDateFormat.format(Date.from(moment)));
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
