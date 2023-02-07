package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Model.Account;

import Util.ConnectionUtil;

public class DAOAccount {

    /*
     * This method adds the username and password given by the user to the database
     * it returns an Account object.
     */

    public Account addUsernameAndPassword(Account useraccount) {
        try {
            Connection conn = ConnectionUtil.getConnection();
            String sql = "INSERT INTO account (username, password) VALUES(?, ?);";
            PreparedStatement ps;
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, useraccount.getUsername());
            ps.setString(2, useraccount.getPassword());
            ps.executeUpdate();
            ResultSet pkeyrs = ps.getGeneratedKeys();
            if(pkeyrs.next()){
                Account addedNewUser = new Account(pkeyrs.getInt("account_id"), useraccount.getUsername(), useraccount.getPassword());
                return addedNewUser;
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return null;

    }

    /*
     * This method should retrieve all users from the database (as Account objects)
     * and returns all the objects in a list
     */
    public ArrayList<Account> getAllUsers() throws SQLException {
        ArrayList<Account> userAccountList = new ArrayList<>();
        Connection conn = ConnectionUtil.getConnection();
        String sql = "SELECT * from account;";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Account user = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            userAccountList.add(user);
        }
        return userAccountList;
    }

    public Account getUserAccountwithNameandPassowrd(Account UserLogin) throws SQLException{
        Connection conn = ConnectionUtil.getConnection();
        String sql = "SELECT * from account WHERE username = ? AND password = ?;";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, UserLogin.getUsername());
        ps.setString(2, UserLogin.getPassword());

        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            Account userSession = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            return userSession;
        } return null;

    }

}
