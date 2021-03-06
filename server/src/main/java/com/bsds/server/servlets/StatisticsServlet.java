package com.bsds.server.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bsds.server.db.StatisticsEntity;
import com.bsds.server.db.UpicDbHelper;
import com.bsds.server.model.Statistics;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsServlet {

    @Autowired
    private UpicDbHelper upicDbHelper;

    private Gson gson = new Gson();

    @GetMapping("/statistics")
    public void getStatistics(HttpServletRequest req, HttpServletResponse res) throws IOException {

        // ArrayList<StatisticsEntity> stats = upicDbHelper.findAllStatistics();
        // String statsList = gson.toJson(stats);

        // ServletUtils.formatHttpResponse(res, statsList, HttpStatus.OK.value(), MediaType.APPLICATION_JSON_VALUE, null);
    }   
}