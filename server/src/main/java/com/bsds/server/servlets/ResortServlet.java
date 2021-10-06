package com.bsds.server.servlets;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bsds.server.model.Resort;

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
        String dummyResort = gson.toJson(new Resort(1, "dummy resort"));
        res.getWriter().append(dummyResort);
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
            res.sendError(400, "invalid resort ID!");
            return;
        }

        // mock for resort lookup
        boolean foundResort = true;
        if (!foundResort) {
            res.setStatus(404);
            res.sendError(404, "resort not found!");
            return;
        }

        // set response status code to SC_OK
        res.setStatus(HttpStatus.OK.value());

        // mirror request type in response 
        res.getWriter().append("Request type: " + HttpMethod.GET + "\n");

        // append resort ID to response body 
        res.getWriter().append("resort ID: " + resortID);
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

        boolean foundResort = true;
        if (!foundResort) {
            // set media type
            res.setContentType("application/json");

            res.sendError(404, "resort not found!");
            return;
        }
        
        // set response status code to SC_OK
        res.setStatus(HttpStatus.CREATED.value());

        // mirror request type in response 
        res.getWriter().append("Request type: " + HttpMethod.POST + "\n");

        // append resort ID to response body 
        res.getWriter().append("resort ID: " + resortID);
    }

}
