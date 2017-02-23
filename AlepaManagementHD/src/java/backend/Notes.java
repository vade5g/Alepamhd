/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
This Note class stores all the information the notes have.

*/
package backend;

/**
 *
 * @author Jonttu
 */

        
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Notes {
    private String content; //Content of the note (written task)
    private String category; //The category the note was assigned to
    private boolean active; //Is the task done or not
    private String targetUser; //The name the note is targeted at (optional)
    private String author; //The one who made the note
    private boolean expired; //Value that checks if the note is expired
    private Date expireDate; //The given date the note expires (optional)
    private SimpleDateFormat sdf; //Used to format the date
    private Long id; //ID for database table
    
    public Notes() {
        
    }
    
    public Notes(String content, String category, String targetUser, String author, String expireDate) throws ParseException {
        this.content = content;
        this.category = category;
        this.active = true;    
        //Checks if any input was given
        if (targetUser != null) {
            this.targetUser = targetUser; 
        }
        this.author = author;
        this.expired = false;
        this.sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        //Checks if any input was given
        if(expireDate != null) {
            this.expireDate = this.sdf.parse(expireDate);
        }
    }
    
    //Checks if the expiration date has passed the current date
    public boolean checkExpiration() {
        this.expired = this.expireDate.before(new Date());       
        return this.expired;        
    }
    
    public String getContent() {
        return this.content;
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
    
    public Date getExpireDate() {
        return this.expireDate;
    }
    
    @Id
    @GeneratedValue
    public Long getId() {
        return this.id;
    }
    
    public void setContent(String input) {
        this.content = input;
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
        this.expireDate = this.sdf.parse(input);
    }
    
    public void setId(Long input) {
        this.id = input;
    }
    
    //Prints out all the information
    public String toString() {
        return "Content: " + getContent() + "\nCategory: " + getCategory() + 
                "\nTarget User: " + getTargetUser() + "\nAuthor: " + getAuthor() + 
                "\nExpire Date: " + getExpireDate();
    }
}
