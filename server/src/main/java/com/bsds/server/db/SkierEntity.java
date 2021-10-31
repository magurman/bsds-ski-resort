package com.bsds.server.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Skier")
public class SkierEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer skierID;
}
