/*
This Note class stores all the information the notes have.

*/
package Model;
  
import java.io.Console;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private String deadline; //The given date the note expires (optional)
    private String category; //The category the note was assigned to
    private boolean active; //Is the task done or not
    private boolean expired; //Value that checks if the note is expired

    public Note() {
    }

    public Note(String title, String targetUser, String author, String message,
            String deadline, String category) {
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        this.title = title;
        if (targetUser != null) { //Checks if any input was given
            this.targetUser = targetUser; 
        } else {
            this.targetUser = "-";
        }
        this.author = author;
        this.message = message;
        //set expired to false, if deadline is given it's changed after
        this.expired = false;
        if(deadline != null) {
            this.deadline = deadline;
            try {
                Date deadlineDate = dateFormat.parse(this.deadline);
                if (checkIfExpired(deadlineDate)==true) {
                    this.expired = true;
                }
            } catch(ParseException p) {
                this.deadline = "PARSING ERROR";
            }
        } else {
            this.deadline = "-";
        }
        this.category = category;
        this.active = true;  
    }
    
    public Note(String title, String targetUser, String author,
            String message, String deadline, String category, boolean active) {
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        this.title = title;
        if (targetUser != null) { //Checks if any input was given
            this.targetUser = targetUser; 
        } else {
            this.targetUser = "-";
        }
        this.author = author;
        this.message = message;
        //set expired to false, if deadline is given it's changed after
        this.expired = false;
        if(deadline != null) {
            this.deadline = deadline;
            try {
                Date deadlineDate = dateFormat.parse(this.deadline);
                if (checkIfExpired(deadlineDate)==true) {
                    this.expired = true;
                }
            } catch(ParseException p) {
                this.deadline = "PARSING ERROR";
            }
        } else {
            this.deadline = "-";
        }
        this.category = category;
        this.active = active;  
    }
    
    //Checks if the expiration date has passed the current date
    public boolean checkIfExpired(Date otherDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date currentDate;
        try {
            currentDate = dateFormat.parse(dateFormat.format(new Date()));
            System.out.println("Tried to compare current date("+currentDate+") and "+otherDate);
        } catch (ParseException ex) {
            return false;
        }
            System.out.println("is current date after this date? "+currentDate.after(otherDate));
        return currentDate.after(otherDate);   
    }
    
    public void deactivate() {
        this.active = false;
    }
    
    public void updateExpired() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        try {
            Date deadlineDate = dateFormat.parse(this.deadline);
            this.expired = checkIfExpired(deadlineDate);
        } catch(ParseException p) {}
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
    public String getDeadline() {
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
    public void setDeadline(String deadline) {
        this.deadline = deadline;
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
    @Override
    public String toString() {
        return "Content: " + getMessage() + "\nCategory: " + getCategory() + 
                "\nTarget User: " + getTargetUser() + "\nAuthor: " + getAuthor() + 
                "\nExpire Date: " + getDeadline();
    }
}
