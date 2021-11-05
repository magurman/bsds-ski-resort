package com.bsds.server.db;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Resort")
public class ResortEntity {
    
    @Id
    private Integer resortID;

    private String name;

    public void setResortID(Integer resortID) {
        this.resortID = resortID;
    }

    public Integer getResortID() {
        return this.resortID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}