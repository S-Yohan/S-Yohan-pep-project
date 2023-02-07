package Service;

import java.sql.SQLException;

import DAO.DAOAccount;
import Model.Account;

public class AccountService {

    DAOAccount DAOacc;

    public AccountService() {
        this.DAOacc = new DAOAccount();
    }

    /*
     * This method adds a new account after ensuring that he username is not blank
     * and that the password is not less than
     * 4 characters.
     */
    public Account addNewAccount(Account NewAccount) {
        if (NewAccount.getUsername() == "" || NewAccount.getPassword().length() < 4) {
            return null;
        } else {
            return DAOacc.addUsernameAndPassword(NewAccount);
        }

    }

    /* This method retrievs an account object from the database */
    public Account getUserAccount(Account userLogin) throws SQLException {

        Account UserAccess = DAOacc.getUserAccountwithNameandPassowrd(userLogin);
        return UserAccess;
    }
}
