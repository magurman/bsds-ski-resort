package com.bsds.server.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Resort")
public class ResortEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer resortID;

    private String name;

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