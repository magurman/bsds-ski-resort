package com.bsds.server.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bsds.server.model.ResponseMessage;
import com.bsds.server.model.SkierVertical;
import com.bsds.server.model.LiftRide;

import com.google.gson.Gson;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SkierServlet {
    private static final String PATH_PREFIX = "/skiers";

    // gson converts pojo to json string
    private Gson gson = new Gson();

    /**
     * 
     * @param resortID
     * @param seasonID
     * @param dayID
     * @param skierID
     * @param req
     * @param res
     * @throws IOException
     */
    @PostMapping(PATH_PREFIX + "/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}")
    public void writeLiftRide(@PathVariable int resortID, @PathVariable String seasonID, @PathVariable int dayID, @PathVariable int skierID,
                     HttpServletRequest req, HttpServletResponse res) throws IOException {
        LiftRide liftRide;
        try {
            String reqBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            liftRide = gson.fromJson(reqBody, LiftRide.class);
        } catch (Exception e) {
            // set media type
            res.setContentType("application/json");

            // set status code
            res.setStatus(HttpStatus.BAD_REQUEST.value());

            // append error message to response
            ResponseMessage responseMessage = new ResponseMessage("invalid request body! Req body must be LiftRide json object!");
            String messageJson = gson.toJson(responseMessage);
            res.getWriter().append(messageJson);
            return;
        }


        // validate dayID parameter
        if (!validateDayID(dayID)) {
            // set media type
            res.setContentType("application/json");

            // set status code
            res.setStatus(HttpStatus.BAD_REQUEST.value());

            // append error message to response
            ResponseMessage responseMessage = new ResponseMessage("invalid day ID!");
            String messageJson = gson.toJson(responseMessage);
            res.getWriter().append(messageJson);
            return;
        }

        // mock for data lookup
        boolean dataNotFound = false;
        if (dataNotFound) {
            // set media type
            res.setContentType("application/json");

            // set status code
            res.setStatus(HttpStatus.NOT_FOUND.value());

            // append error message to response
            ResponseMessage responseMessage = new ResponseMessage("data not found!!");
            String messageJson = gson.toJson(responseMessage);
            res.getWriter().append(messageJson);
            return;
        }

        // set response status code to CREATED
        res.setStatus(HttpStatus.CREATED.value());

        // send liftRide back in response
        res.getWriter().append(gson.toJson(liftRide));
    }

    @GetMapping(PATH_PREFIX + "/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}")
    public void getVerticalForSkiDay(@PathVariable int resortID, @PathVariable String seasonID, @PathVariable int dayID, @PathVariable int skierID,
                    HttpServletRequest req, HttpServletResponse res) throws IOException {

        // set media type
        res.setContentType("application/json");

        // validate dayID parameter
        if (!validateDayID(dayID)) {

            // set status code
            res.setStatus(HttpStatus.BAD_REQUEST.value());

            // append error message to response
            ResponseMessage responseMessage = new ResponseMessage("invalid day ID!");
            String messageJson = gson.toJson(responseMessage);
            res.getWriter().append(messageJson);
            return;
        }

        // mock for data lookup
        boolean dataNotFound = false;
        if (dataNotFound) {

            // set status code
            res.setStatus(HttpStatus.NOT_FOUND.value());

            // append error message to response
            ResponseMessage responseMessage = new ResponseMessage("data not found!");
            String messageJson = gson.toJson(responseMessage);
            res.getWriter().append(messageJson);
            return;
        }

        // set response status code to SC_OK
        res.setStatus(HttpStatus.OK.value());

        // append dummy data to response body
        res.getWriter().append("45353");
    }

    @GetMapping(PATH_PREFIX + "/{skierID}/vertical")
    public void vertical(@PathVariable String skierID, HttpServletRequest req, HttpServletResponse res) throws IOException {


        // TODO: validate parameters

        res.setContentType("application/json");

        // mock for data lookup
        boolean dataNotFound = false;
        if (dataNotFound) {

            // set status code
            res.setStatus(HttpStatus.NOT_FOUND.value());

            // append error message to response
            ResponseMessage responseMessage = new ResponseMessage("data not found!");
            String messageJson = gson.toJson(responseMessage);
            res.getWriter().append(messageJson);
            return;
        }

        // mock for input validation 
        boolean invalidInputs = false;
        if (invalidInputs) {
            // set status code
            res.setStatus(HttpStatus.BAD_REQUEST.value());

            // append error message to response
            ResponseMessage responseMessage = new ResponseMessage("invalid inputs!");
            String messageJson = gson.toJson(responseMessage);
            res.getWriter().append(messageJson);
            return;
        }

        // set response status code to SC_OK
        res.setStatus(HttpStatus.OK.value());

        // append dummy data to response body 
        List<SkierVertical> skierVerticalList = new ArrayList<>();
        skierVerticalList.add(new SkierVertical("1", 100));
        skierVerticalList.add(new SkierVertical("2", 150));
        String dummySkierVerticalList= gson.toJson(skierVerticalList);

        res.getWriter().append(dummySkierVerticalList);
    }

    /**
     * Private method to validate a dayID. Day IDs must be between 1 and 366
     * @param dayID
     * @return boolean, true if valid dayID
     */
    private boolean validateDayID(int dayID) {
        return dayID > 0 && dayID < 367;
    }

}
