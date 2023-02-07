package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Model.Message;
import Util.ConnectionUtil;

public class DAOMessage {

    public Boolean checkPostedBy(int existingId) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "SELECT * from account WHERE account_id = ?;";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, existingId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return true;
        }
        return false;

    }

    public Message insertMessage(Message msg) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) values(? , ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, msg.getPosted_by());
        ps.setString(2, msg.getMessage_text());
        ps.setLong(3, msg.getTime_posted_epoch());
        ps.executeUpdate();

        ResultSet id_key = ps.getGeneratedKeys();
        if (id_key.next()) {
            int generated_message_id = (int) id_key.getLong(1);
            Message usermsg = new Message(generated_message_id, msg.getPosted_by(), msg.getMessage_text(),
                    msg.getTime_posted_epoch());
            return usermsg;
        }
        return null;
    }

    public ArrayList<Message> getAllMessages() throws SQLException {
        ArrayList<Message> messageList = new ArrayList<Message>();
        Connection conn = ConnectionUtil.getConnection();
        String sql = "SELECT * from message;";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Message msg = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            messageList.add(msg);

        }
        return messageList;
    }

    /*
     * This method will retrive a message using the message_id
     * the Message object is returned if found. Otherwise, null is returned.
     */
    public Message getMsgUsingId(int id) throws SQLException {

        Connection conn = ConnectionUtil.getConnection();
        String sql = "SELECT * from message WHERE message_id = ?;";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {

            Message msg = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));

            return msg;

        }
        return null;
    }

    /*
     * This method will delete a message using the message_id
     * the Message object is returned if found. Otherwise, null is returned.
     */
    public void deleteMessage(int id) throws SQLException {

        Connection conn = ConnectionUtil.getConnection();
        String sql = "DELETE from message WHERE message_id = ?;";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();

    }

    /*
     * This method will update a message using the message_id
     * the Message object is returned if found. Otherwise, null is returned.
     */
    public void UpdateMsgTxt(int id, String newMessage) throws SQLException {

        Connection conn = ConnectionUtil.getConnection();
        String sql = "UPDATE message SET message_text = ? WHERE message_id = ?;";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, newMessage);
        ps.setInt(2, id);
        ps.executeUpdate();

    }
    /*
     * Returns a list of messages for a particular user as provided by an account_id
     * this endpoint should return an empty list.
     */

    public ArrayList<Message> getAllUserMessages(int acc_id) throws SQLException {

        ArrayList<Message> AllMessagesByUser = new ArrayList<>();
        Connection conn = ConnectionUtil.getConnection();
        String sql = "SELECT *  from message WHERE posted_by = ?;";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, acc_id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {

            Message msg = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
            AllMessagesByUser.add(msg);

        }
        return AllMessagesByUser;

    }
}
