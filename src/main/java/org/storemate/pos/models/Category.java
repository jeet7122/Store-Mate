package org.storemate.pos.models;

import org.storemate.pos.models.enums.Status;

public class Category {
    private Integer category_id;
    private String category_name;
    private String category_description;
    private Status status;

    public Category(Integer category_id, String category_name, String category_description, Status status) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.category_description = category_description;
        this.status = status;
    }

    public Category() {
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_description() {
        return category_description;
    }

    public void setCategory_description(String category_description) {
        this.category_description = category_description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
