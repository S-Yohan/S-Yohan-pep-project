package Service;
import java.sql.SQLException;


import DAO.DAOAccount;
import Model.Account;

public class AccountService {
       
    
    DAOAccount DAOacc;

    public AccountService(){
        this.DAOacc = new DAOAccount();
    }

    

    public Account addNewAccount(Account NewAccount){
        if(NewAccount.getUsername() == "" || NewAccount.getPassword().length() < 4){
            return null;
        } else{
            return DAOacc.addUsernameAndPassword(NewAccount);
        }       
        
        
    }

    public Account getUserAccount(Account userLogin) throws SQLException{
        
        Account UserAccess = DAOacc.getUserAccountwithNameandPassowrd(userLogin);
        return UserAccess;
    }
}
