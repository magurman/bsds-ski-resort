package com.bsds.server.servlets;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bsds.server.model.Resort;
import com.bsds.server.model.ResponseMessage;
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

    /**
     * GET all ski resorts 
     * @param req - HttpServletRequest
     * @param res - HttpServletResponse
     * @throws IOException - 
     */
    @GetMapping(PATH_PREFIX)
    public void skiResorts(HttpServletRequest req, HttpServletResponse res) throws IOException {
        // set response status code to SC_OK
        res.setStatus(HttpStatus.OK.value());
        
        // append dummy data to response body 
        List<Resort> resorts = new ArrayList<>();
        resorts.add(new Resort("dummy resort 1", 1));
        resorts.add(new Resort("dummy resort 2", 2));

        String dummyResortList = gson.toJson(resorts);
        res.getWriter().append(dummyResortList);
    }

    /**
     * GET a list of seasons for a specified resort
     * @param resortID - id of resort to look up
     * @param req - HttpServletRequest
     * @param res - HttpServletResponse
     * @throws IOException
     */
    @GetMapping(PATH_PREFIX + "/{resortID}/seasons")
    public void seasons(@PathVariable String resortID, HttpServletRequest req, HttpServletResponse res) throws IOException {
        
        // set media type
        res.setContentType("application/json");

        // mock for id validation
        boolean invalidID = false;
        if (invalidID) {
            res.setStatus(HttpStatus.BAD_REQUEST.value());

            // append error message to response
            ResponseMessage responseMessage = new ResponseMessage("invalid resort ID!");
            String messageJson = gson.toJson(responseMessage);
            res.getWriter().append(messageJson);
            return;
        }

        // mock for resort lookup
        boolean foundResort = true;
        if (!foundResort) {
            res.setStatus(HttpStatus.NOT_FOUND.value());

            // append error message to response
            ResponseMessage responseMessage = new ResponseMessage("resort not found!");
            String messageJson = gson.toJson(responseMessage);
            res.getWriter().append(messageJson);
            return;
        }

        // set response status code to OK
        res.setStatus(HttpStatus.OK.value());

        // append resort ID to response body 

        List<Season> seasons = new ArrayList<>();
        seasons.add(new Season("season 1"));
        seasons.add(new Season("season 2"));

        String dummySeasonsList = gson.toJson(seasons);
        res.getWriter().append(dummySeasonsList);
    }

    /**
     * POST a new season for a specified resort 
     * @param resortID - id of resort to look up
     * @param req - HttpServletRequest
     * @param res - HttpServletResponse
     * @throws IOException
     */
    @PostMapping(PATH_PREFIX + "/{resortID}/seasons")
    public void addSeason(@PathVariable String resortID, HttpServletRequest req, HttpServletResponse res) throws IOException {
        
        // TODO: validate request body 

        boolean invalidInputs = false;
        if (invalidInputs) {
            // set media type
            res.setContentType("application/json");

            res.setStatus(HttpStatus.BAD_REQUEST.value());

            // append error message to response
            ResponseMessage responseMessage = new ResponseMessage("invalid inputs!");
            String messageJson = gson.toJson(responseMessage);
            res.getWriter().append(messageJson);
            return;
        }

        boolean foundResort = true;
        if (!foundResort) {
            // set media type
            res.setContentType("application/json");

            res.setStatus(HttpStatus.NOT_FOUND.value());

            // append error message to response
            ResponseMessage responseMessage = new ResponseMessage("resort not found!");
            String messageJson = gson.toJson(responseMessage);
            res.getWriter().append(messageJson);
            return;
        }
        
        // set response status code to CREATED
        res.setStatus(HttpStatus.CREATED.value());
    }

}
