package com.bsds.server.servlets;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
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

        // get auth credentials from request and perform default authorization test
        final String[] creds = getCredentialsFromRequest(req);

        // validate credentials 
        if (creds.length == 0 || !creds[0].equals("admin") || !creds[1].equals("admin")) {
            res.setHeader(HttpHeaders.WWW_AUTHENTICATE, "invalid authorization credentials");
            res.setStatus(HttpStatus.UNAUTHORIZED.value());

            ResponseMessage responseMessage = new ResponseMessage("invalid username/password provided. Please retry with new credentials.");
            String messageJson = gson.toJson(responseMessage);
            res.getWriter().append(messageJson);
            return;
        }

        // validate request body
        // this only works for application/json content type
        LiftRide liftRide;
        try {
            byte[] inputStreamBytes = req.getInputStream().readAllBytes();
            String reqBody = new String(inputStreamBytes, StandardCharsets.UTF_8);
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
    public void vertical(@PathVariable int skierID, HttpServletRequest req, HttpServletResponse res) throws IOException {

        // set response content type
        res.setContentType("application/json");

        // get and validate existence of resort query param -- it is required
        String resortQueryParamter = req.getParameter("resort");
        if (resortQueryParamter == null) {
            // set status code
            res.setStatus(HttpStatus.BAD_REQUEST.value());

            // append error message to response
            ResponseMessage responseMessage = new ResponseMessage("missing required resort parameter!");
            String messageJson = gson.toJson(responseMessage);
            res.getWriter().append(messageJson);
            return;
        }

        // this param is not required -- no need to validate but will be null if not included
        String seasonQueryParameter = req.getParameter("season");

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

    /**
     * Get credentials from HttpServletRequest and return them as a String[] in the format: 
     * ["username", "password"]
     * 
     * This method currently only supports Basic auth
     * @param req - the request which contains authentication credentials 
     * @return the authentication credentials extracted from the request
     */
    private String[] getCredentialsFromRequest(HttpServletRequest req) {

        final String auth = req.getHeader(HttpHeaders.AUTHORIZATION);
        String[] credValues = {};
        // for now, only support Basic auth
        if (auth != null && auth.toLowerCase().startsWith("basic")) {
            // Authorization: Basic base64credentials
            String base64Credentials = auth.substring("Basic".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String creds = new String(credDecoded, StandardCharsets.UTF_8);

            credValues = creds.split(":", 2);
        }

        return credValues;
    }
}
