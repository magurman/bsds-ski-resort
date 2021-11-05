package com.bsds.server.db;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Skier")
public class SkierEntity {

    @Id
    private Integer skierID;

    public void setSkierID(Integer skierID) {
        this.skierID = skierID;
    }
}
