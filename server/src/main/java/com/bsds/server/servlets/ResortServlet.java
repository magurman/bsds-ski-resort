package com.bsds.server.servlets;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bsds.server.db.ResortEntity;
import com.bsds.server.db.UpicDbHelper;
import com.bsds.server.model.Season;

import java.util.List;

import com.google.gson.Gson;

/**
 * A collection of methods to handle http requests relating to Ski Resorts
 */
@RestController
public class ResortServlet {
    
    // this class will handle all servlets whose path begin with "/resorts"
    private static final String PATH_PREFIX = "/resorts";

    // gson converts pojo to json string
    private Gson gson = new Gson();

    @Autowired
    private UpicDbHelper upicDbHelper;

    /**
     * GET all ski resorts 
     * @param req - HttpServletRequest
     * @param res - HttpServletResponse
     * @throws IOException - 
     */
    @GetMapping(PATH_PREFIX)
    public void skiResorts(HttpServletRequest req, HttpServletResponse res) throws IOException {

        ArrayList<ResortEntity> allResorts = this.upicDbHelper.findAllResorts();
        String resortsList = gson.toJson(allResorts);
        ServletUtils.formatHttpResponse(res, resortsList, HttpStatus.OK.value(), MediaType.APPLICATION_JSON_VALUE, null);
    }

    /**
     * GET a list of seasons for a specified resort
     * @param resortID - id of resort to look up
     * @param req - HttpServletRequest
     * @param res - HttpServletResponse
     * @throws IOException
     */
    @GetMapping(PATH_PREFIX + "/{resortID}/seasons")
    public void seasons(@PathVariable int resortID, HttpServletRequest req, HttpServletResponse res) throws IOException {
        
        List<Season> seasons = new ArrayList<>() { {add(new Season("2021"));} };
        String seasonsList = gson.toJson(seasons);
        ServletUtils.formatHttpResponse(res, seasonsList, HttpStatus.OK.value(), MediaType.APPLICATION_JSON_VALUE, null);

    }

    /**
     * POST a new season for a specified resort 
     * @param resortID - id of resort to look up
     * @param req - HttpServletRequest
     * @param res - HttpServletResponse
     * @throws IOException
     */
    @PostMapping(PATH_PREFIX + "/{resortID}/seasons")
    public void addSeason(@PathVariable int resortID, HttpServletRequest req, HttpServletResponse res) throws IOException {
        
        // set response status code to CREATED
        res.setStatus(HttpStatus.CREATED.value());
    }

}
