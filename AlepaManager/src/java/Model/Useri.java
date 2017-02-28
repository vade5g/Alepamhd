/*
 * This class contains everything pertaining to one user
 * One user is an instance of this class
 */
package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
public class Useri implements Serializable {
    
    @Id @GeneratedValue
    private int id;
    private String firstname, lastname;
    private String username, password;
    private String category;
    private String email;

    public Useri() {
    }
    
    public Useri(String firstname, String lastname, String username, String password, String category, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.category = category;
        this.email = email;
    }
    //GETTERS
    public int getId() {
        return id;
    }
    
    @XmlElement
    public String getFirstname() {
        return firstname;
    }
    
    @XmlElement
    public String getLastname() {
        return lastname;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getCategory() {
        return category;
    }
    public String getEmail() {
        return email;
    }
    

    //SETTERS
    public void setId(int id) {
        this.id = id;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
