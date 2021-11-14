/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.Role;
import models.User;
import services.RoleService;
import services.UserService;

/**
 *
 * @author Jay
 */
public class UserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserService us = new UserService();
        String action = (String) request.getParameter("action");
        try{
            HttpSession session = request.getSession();
            List<User> users = us.getAll();
            request.setAttribute("users", users);
        } 
        catch (Exception ex){
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception gotten");
        }
        
        if(action!=null)
        {
            if (action.equals("edit"))
            {
                System.out.println("ACTION: EDIT");
                try{
                    HttpSession session = request.getSession();
                    String email = request.getParameter("selectedUser");
                    session.setAttribute("sessionSelectedUser", email);
                    System.out.println(session.getAttribute("sessionSelectedUser"));
                    User user = us.getUser(email);
                    System.out.println(email);
                    request.setAttribute("Eemail", user.getEmail());
                    request.setAttribute("Efirstname", user.getFirstName());
                    request.setAttribute("Elastname", user.getLastName());
                    request.setAttribute("Epassword", user.getPassword());
                    request.setAttribute("isActive", user.getActive());
                    request.setAttribute("eRole", user.getRole());
                    System.out.println(user.getActive());
                }
                catch (Exception ex){
                    Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Unable to retrieve user");
                }
            }
            else if(action.equals("delete"))
            {
                System.out.println("ACTION: DELETE");
                try{
                    String email = request.getParameter("selectedUser");
                    System.out.println("Deleting: " + email);
                    us.delete(email);
                    request.setAttribute("selectedUser", null);
                    request.setAttribute("action", null);
                    action = null;
                    doPost(request, response);

                } catch(Exception ex) {
                    Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Unable to delete user");

                }
            }
        }
        
        getServletContext().getRequestDispatcher("/WEB-INF/users.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserService us = new UserService();
        HttpSession session = request.getSession();
        String action = (String)request.getParameter("action");
        try{
            List<User> users = us.getAll();
            request.setAttribute("users", users);
        } catch (Exception ex){
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception gotten");
        }
        
        if(action != null)
        {
            if(action.equals("add")){
                System.out.println("ACTION: ADD");
                addUser(request, response);
            }
            else if(action.equals("save"))
            {
                System.out.println("ACTION: SAVE");
                editUser(request, response);
            }
        }
        
        getServletContext().getRequestDispatcher("/WEB-INF/users.jsp").forward(request, response);

    }
    
    void addUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        UserService us = new UserService();
        RoleService rs = new RoleService();
        try{
            HttpSession session = request.getSession();
            String email = request.getParameter("email");
            String firstname = request.getParameter("firstname");
            String lastname = request.getParameter("lastname");
            String password = request.getParameter("password");
            boolean active;
            if(request.getParameter("active") != null)
            {
                active = true;
            }
            else
            {
                active = false;
            }
            int role;
            String roleReader = request.getParameter("role"); 
            if(roleReader.equals("systemadmin"))
            {
                role = 1;
            }
            else if(roleReader.equals("regularuser"))
            {
                role = 2;
            }
            else
            {
                role = 3;
            }

            if(!email.equals("") && firstname!=null && lastname!=null && password!=null && roleReader!=null)
            {
                System.out.println("email:" + email);
                User user = new User(email, active, firstname, lastname, password);
                user.setRole(rs.getRole(role));
                us.insert(user);
                doGet(request, response);
            }
            else {
                System.out.println("Values needed");
            }
        } catch (Exception ex){
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Errors occurred");
        }
    }
    
    void editUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        RoleService rs = new RoleService();
        UserService us = new UserService();
        try{
            HttpSession session = request.getSession();
            String email = (String)session.getAttribute("sessionSelectedUser");
            System.out.println("selected user: " + email);
            String firstname = request.getParameter("firstnameedit");
            String lastname = request.getParameter("lastnameedit");
            String password = request.getParameter("passwordedit");
            boolean active;
            if(request.getParameter("activeedit") != null)
            {
                active = true;
            }
            else
            {
                active = false;
            }
            int role;
            String roleReader = request.getParameter("roleedit"); 
            if(roleReader.equals("systemadmin"))
            {
                role = 1;
            }
            else if(roleReader.equals("regularuser"))
            {
                role = 2;
            }
            else
            {
                role = 3;
            }

            if(!email.equals("") && firstname!=null && lastname!=null && password!=null && roleReader!=null)
            {
                System.out.println("email:" + email);
                User user = new User(email, active, firstname, lastname, password);
                user.setRole(rs.getRole(role));
                us.update(user);
                session.setAttribute("sessionSelectedUser", null);
                doGet(request, response);
            }
            else {
                System.out.println("Values needed");
            }
        } catch (Exception ex){
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error occurred");
        }
    }
}
