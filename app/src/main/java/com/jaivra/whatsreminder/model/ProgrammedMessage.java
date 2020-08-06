package com.jaivra.whatsreminder.model;

import com.jaivra.whatsreminder.inc.gui.ViewItemModel;

import java.util.Date;

public class ProgrammedMessage implements ViewItemModel {

    private String toContact;
    private String toNumber;
    private String body;
    private Date date;
    private boolean periodic;
    private boolean active;

    private ProgrammedMessage(Builder builder) {
        toContact = builder.toContact;
        toNumber = builder.toNumber;
        body = builder.body;
        date = builder.date;
        periodic = builder.periodic;
        active = builder.active;
    }

    public String getToContact() {
        return toContact;
    }

    public String getToNumber() {
        return toNumber;
    }

    public String getBody() {
        return body;
    }

    public Date getDate() {
        return date;
    }

    public boolean isPeriodic() {
        return periodic;
    }

    public boolean isActive() {
        return active;
    }

    public void setToContact(String toContact) {
        this.toContact = toContact;
    }

    public void setToNumber(String toNumber) {
        this.toNumber = toNumber;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setPeriodic(boolean periodic) {
        this.periodic = periodic;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public static final class Builder {
        private String toContact;
        private String toNumber;
        private String body;
        private Date date;
        private boolean periodic;
        private boolean active;

        public Builder() {
        }

        public Builder toContact(String toContact) {
            this.toContact = toContact;
            return this;
        }

        public Builder toNumber(String toNumber) {
            this.toNumber = toNumber;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder date(Date date) {
            this.date = date;
            return this;
        }

        public Builder periodic(boolean periodic) {
            this.periodic = periodic;
            return this;
        }

        public Builder active(boolean active) {
            this.active = active;
            return this;
        }

        public ProgrammedMessage build() {
            return new ProgrammedMessage(this);
        }
    }
}
