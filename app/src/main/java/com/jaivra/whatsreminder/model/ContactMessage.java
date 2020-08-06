package com.jaivra.whatsreminder.model;

import com.jaivra.whatsreminder.inc.gui.ViewItemModel;

public class ContactMessage implements ViewItemModel {
    private String id;
    private String name;
    private String number;
    private String namePreview;

    private ContactMessage(Builder builder) {
        id = builder.id;
        name = builder.name;
        number = builder.number;

        String[] tokens = name.split(" ");
        namePreview = String.valueOf(tokens[0].charAt(0));
        if (tokens.length > 1)
            namePreview += tokens[1].charAt(0);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getNamePreview() {
        return namePreview;
    }

    public static final class Builder {
        private String id;
        private String name;
        private String number;

        public Builder() {
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder number(String number) {
            this.number = number;
            return this;
        }

        public ContactMessage build() {
            return new ContactMessage(this);
        }
    }
}
