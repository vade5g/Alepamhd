/*
This Note class stores all the information the notes have.

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Temporal;

@XmlRootElement
@Entity
public class Note implements Serializable {
    @Id
    @GeneratedValue
    private int id; //ID for database table
    
    private String title;       //The title of the note
    private String targetUser;  //The name the note is targeted at (optional)
    private String author;      //The one who made the note
    private String message; //Content of the note (written task)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date deadline; //The given date the note expires (optional)
    private String category; //The category the note was assigned to
    private boolean active; //Is the task done or not
    private boolean expired; //Value that checks if the note is expired

    public Note() {
    }

    public Note(String title, String targetUser, String author, String message, String deadline, String category) {
        this.title = title;
        if (targetUser != null) { //Checks if any input was given
            this.targetUser = targetUser; 
        }
        this.author = author;
        this.message = message;
        if(deadline != null) {
            
        }
        this.category = category;
        
        this.active = true;    
        this.expired = false;
    }
    
    //Checks if the expiration date has passed the current date
    public boolean checkExpiration() {
        this.expired = this.deadline.before(new Date());       
        return this.expired;        
    }
    
    //GETTERS
    public int getId() {
        return this.id;
    }
    public String getTitle() {
        return title;
    }
    public String getMessage() {
        return this.message;
    }
    public String getCategory() {
        return this.category;
    }
    public boolean getActive() {
        return this.active;
    }
    public String getTargetUser() {
        return this.targetUser;
    }
    public String getAuthor() {
        return this.author;
    }
    public boolean getExpired() {
        return this.expired;
    }
    public Date getDeadline() {
        return this.deadline;
    }
    
    //SETTERS
    public void setId(int input) {
        this.id = input;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setMessage(String input) {
        this.message = input;
    }
    public void setCategory(String input) {
        this.category = input;
    }
    public void setActive(boolean input) {
        this.active = input;
    }
    public void setTargetUser(String input) {
        this.targetUser = input;
    }
    public void setAuthor(String input) {
        this.author = input;
    }
    public void setExpired(boolean input) {
        this.expired = input;
    }
    //takes the formal input of "dd-MM-yyyy hh:mm:ss" and sets it as the expiration date
    public void setExpireDate(String input) throws ParseException {
        
    }
    
    //Prints out all the information
    public String toString() {
        return "Content: " + getMessage() + "\nCategory: " + getCategory() + 
                "\nTarget User: " + getTargetUser() + "\nAuthor: " + getAuthor() + 
                "\nExpire Date: " + getDeadline();
    }
}
