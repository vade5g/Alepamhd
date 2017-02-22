/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author Vade
 */
public class User {
    
    private String department;
    private String username;
    private String password;
    
    private Long id;
    
    public User () {
        
    }
    
    public User (String a,String b, String c) {
        this.department = a;
        this.username = b;
        this.password = c;
    }

    /**
     * @return the department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    //Hibernate things
    
    @Id
    @GeneratedValue
    public Long getId() {
        return this.id;
    }
}
