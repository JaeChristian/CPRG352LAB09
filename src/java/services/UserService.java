package services;

import dataaccess.UserDB;
import java.util.List;
import models.User;

public class UserService {
    public List<User> getAll() throws Exception
    {
        UserDB userDB = new UserDB();
        List<User> users = userDB.getAll();
        /*System.out.println("Getting all users");
        for(int i = 0; i<users.size();i++)
        {
            System.out.println(users.get(i).getEmail());
        }*/
        return users;
    }
    
    public User getUser(String email) throws Exception
    {
        UserDB userDB = new UserDB();
        User user = userDB.getUser(email);
        return user;
    }
    
    public void insert(User user) throws Exception
    {
         UserDB userDB = new UserDB();
         userDB.insert(user);
    }
    
    public void update(User user) throws Exception
    {
         UserDB userDB = new UserDB();
         userDB.update(user);
    }
    
    public void delete(String email) throws Exception
    {
         UserDB userDB = new UserDB();
         userDB.delete(email);
    }
}
