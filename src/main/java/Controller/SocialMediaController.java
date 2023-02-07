package Controller;

import java.sql.SQLException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
public class SocialMediaController {

    // AccountService and MessageService objects are instantiated here
    AccountService as;
    MessageService ms;

    public SocialMediaController() {
        this.as = new AccountService();
        this.ms = new MessageService();

    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in
     * the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * 
     * @return a Javalin app object which defines the behavior of the Javalin
     *         controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::newUserRegistration);// this handles new user registration.
        app.post("/login", this::UserLogin);// this handles user login.
        app.post("/messages", this::createNewMessage);// this handles the creation of a new message.
        app.get("/messages", this::getAllMessages);// this handles the retrieval of all messages.
        app.get("messages/{message_id}", this::getMessagebyId);// this handles the retrieval of a message by id.
        app.delete("messages/{message_id}", this::deleteMessageById);// this handles a message delete request by id.
        app.patch("messages/{message_id}", this::replaceMessage);// this handles a message update request by id.
        app.get("/accounts/{account_id}/messages", this::allMessagesByUser);/*
                                                                             * this handles a request to get all
                                                                             * messages by a user
                                                                             */

        return app;
    }

    /**
     * Handler to post a new user account
     * 
     * @param context The Javalin Context object manages information about both the
     *                HTTP request and response.
     * @throws JsonProcessingException
     * @throws JsonMappingException
     * @throws SQLException
     */
    private void newUserRegistration(Context ctx) throws JsonMappingException, JsonProcessingException {

        ObjectMapper om = new ObjectMapper();
        Account newUser = om.readValue(ctx.body(), Account.class);
        Account addedUser = as.addNewAccount(newUser);

        if (addedUser != null) {
            ctx.json(om.writeValueAsString(addedUser));
            ctx.status(200);

        } else {
            ctx.status(400);

        }
    }

    /**
     * Handler for a post request to login, given username and password.
     * 
     * @param context The Javalin Context object manages information about both the
     *                HTTP request and response.
     * @throws JsonProcessingException
     * @throws JsonMappingException
     * @throws SQLException
     */
    private void UserLogin(Context ctx) throws JsonMappingException, JsonProcessingException, SQLException {
        ObjectMapper om = new ObjectMapper();
        Account userLogin = om.readValue(ctx.body(), Account.class);

        /*
         * The AccountService method getUserAccount is called here to return an Account
         * object.
         * If no account object is found, null is returned.
         */
        Account userlogged = as.getUserAccount(userLogin);
        if (userlogged == null) {
            ctx.status(401);
        } else {
            ctx.status(200);
            ctx.json(om.writeValueAsString(userlogged));
        }

    }

    /**
     * Handler for a post to create a new message by a specific user. The JSON
     * request
     * should contain an int representing posted_by and a String representing
     * the message.
     * The message is persisted in the data base if it is not NULL, less than 255
     * characters, and posted by an existing user as referenced
     * in the posted_by field. A successful post should return a JSON
     * 
     * @param context The Javalin Context object manages information about both the
     *                HTTP request and response.
     * @throws JsonProcessingException
     * @throws JsonMappingException
     * @throws SQLException
     */

    private void createNewMessage(Context ctx) throws JsonProcessingException, SQLException {
        ObjectMapper om = new ObjectMapper();
        Message Message = om.readValue(ctx.body(), Message.class);
        Message postedMessage = ms.postMessage(Message);
        if (postedMessage == null) {
            ctx.status(400);
        } else {
            ctx.status(200);
            ctx.json(om.writeValueAsString(postedMessage));

        }

    }

    /**
     * Handler for a GET request. The JSON representation of a list containing all
     * messages is returned in the body of the response.
     * Status response = 200
     * 
     * @param context The Javalin Context object manages information about both the
     *                HTTP request and response.
     * @throws JsonProcessingException
     * @throws JsonMappingException
     * @throws SQLException
     */
    private void getAllMessages(Context ctx) throws JsonProcessingException, SQLException {
        ObjectMapper om = new ObjectMapper();
        ArrayList<Message> MessageList = ms.getMessageList();
        ctx.status(200);
        ctx.json(om.writeValueAsString(MessageList));

    }

    /**
     * Handler for a GET request. The JSON representation of a message is returned.
     * This
     * message is retrieved by a request which
     * includes the message_id as a path parameter.
     * Status response = 200
     * 
     * @param context The Javalin Context object manages information about both the
     *                HTTP request and response.
     * @throws JsonProcessingException
     * @throws JsonMappingException
     * @throws SQLException
     */
    private void getMessagebyId(Context ctx) throws JsonProcessingException, SQLException {
        ObjectMapper om = new ObjectMapper();
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message msg = ms.getMessageById(id);
        if (msg == null) {
            ctx.status(200);
        } else {
            ctx.status(200);
            ctx.json(om.writeValueAsString(msg));
        }

    }

    /**
     * Handler for a DELETE request. The JSON representation of the deleted message
     * is returned. The request includes the message_id as a path parameter.
     * Status response = 200
     * 
     * @param context The Javalin Context object manages information about both the
     *                HTTP request and response.
     * @throws JsonProcessingException
     * @throws JsonMappingException
     * @throws SQLException
     */
    private void deleteMessageById(Context ctx) throws SQLException, JsonProcessingException {

        ObjectMapper om = new ObjectMapper();
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message msg = ms.messageDeletor(id);

        if (msg == null) {
            ctx.status(200);
        } else {
            ctx.status(200);
            ctx.json(om.writeValueAsString(msg));
        }

    }

    /**
     * Handler for a PATCH request. The request body contains the message_text value
     * and the message_id as a path parameter.
     * Status response = 200
     * 
     * @param context The Javalin Context object manages information about both the
     *                HTTP request and response.
     * @throws JsonProcessingException
     * @throws JsonMappingException
     * @throws SQLException
     */
    private void replaceMessage(Context ctx) throws SQLException, JsonProcessingException {

        ObjectMapper om = new ObjectMapper();
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message NewMessageText = om.readValue(ctx.body(), Message.class);
        Message msg = ms.messageReplacer(id, NewMessageText.getMessage_text());
        if (msg == null) {
            ctx.status(400);
        } else {
            ctx.status(200);
            ctx.json(om.writeValueAsString(msg));
        }

    }

    /**
     * Handler for a GET request. The response body contains a list of all messages
     * by a particular user.
     * The account_id (which maps to posted_by in Message)is given as a paramter.
     * Status response = 200
     * 
     * @param context The Javalin Context object manages information about both the
     *                HTTP request and response.
     * @throws JsonProcessingException
     * @throws JsonMappingException
     * @throws SQLException
     */
    private void allMessagesByUser(Context ctx) throws SQLException, JsonProcessingException {

        ObjectMapper om = new ObjectMapper();
        int acc_id = Integer.parseInt(ctx.pathParam("account_id"));
        ArrayList<Message> userMessages = ms.userMessageList(acc_id);

        ctx.status(200);
        ctx.json(om.writeValueAsString(userMessages));

    }

}
