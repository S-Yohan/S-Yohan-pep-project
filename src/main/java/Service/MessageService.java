package Service;

import java.sql.SQLException;
import java.util.ArrayList;

import DAO.DAOMessage;
import Model.Message;

public class MessageService {

    DAOMessage dm;

    public MessageService() {
        this.dm = new DAOMessage();
    }

    public Message postMessage(Message newMessage) throws SQLException {
        if (dm.checkPostedBy(newMessage.getPosted_by()) == false || newMessage.getMessage_text() == ""
                || newMessage.getMessage_text().length() > 255
                || newMessage.getMessage_text() == null) {
            return null;
        } else {
            Message UserMsg = dm.insertMessage(newMessage);
            return UserMsg;
        }

    }

    public ArrayList<Message> getMessageList() throws SQLException {
        ArrayList<Message> AllMessages = dm.getAllMessages();
        return AllMessages;
    }

    public Message getMessageById(int id) throws SQLException {
        Message msg = dm.getMsgUsingId(id);
        if (msg == null) {
            return null;
        } else {
            return msg;
        }

    }

    /*
     * This function checks for an existing id and then deletes the message if it
     * exists
     * if no id exists the null is returned.
     */
    public Message messageDeletor(int id) throws SQLException {
        Message messageTobeDeleted = dm.getMsgUsingId(id);

        if (messageTobeDeleted != null) {
            dm.deleteMessage(id);
            return messageTobeDeleted;
        }
        return null;

    }

    /*
     * This condition statement checks to ensure the id is found in the database
     * and that the new message is less than 255 characters and is not blank.
     */
    public Message messageReplacer(int id, String newMsg) throws SQLException {
        Message checkMsg = this.dm.getMsgUsingId(id);
        if (checkMsg == null || newMsg.length() >= 255 || newMsg == "" || newMsg == null) {
            return null;
        } else {
            dm.UpdateMsgTxt(id, newMsg);
            Message updatedMsg = dm.getMsgUsingId(id);
            return updatedMsg;
        }

    }

    public ArrayList<Message> userMessageList(int acc_id) throws SQLException {
        ArrayList<Message> userMessageList = dm.getAllUserMessages(acc_id);
        return userMessageList;
    }

}
