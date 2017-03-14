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
    private int id; 
    
    private String title; 
    private String targetUser;  //User that note is targeted to
    private String author;
    private String message; //Content of the note (written task)
    private String deadline; //The given date the note expires (optional)
    private String category;
    private boolean active; //Is the task done or not
    private boolean expired; //Is the date expired in terms of date

    public Note() {
    }

    //constructor for notes created from client rather than populate
    public Note(String title, String targetUser, String author, String message,
            String deadline, String category) {
        //the date format is enforced by regex on the javascript side
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        this.title = title;
        //target user can be left empty, put as "-" if this is the case
        if (targetUser != null) { 
            this.targetUser = targetUser; 
        } else {
            this.targetUser = "-";
        }
        this.author = author;
        this.message = message;
        //set expired to false, if deadline is given it's changed after
        this.expired = false;
        //deadline can be left empty, in this case put as "-"
        if(deadline != null) {
            this.deadline = deadline;
            //if the date isnt null, check against current date to see if ecpired
            try {
                Date deadlineDate = dateFormat.parse(this.deadline);
                if (checkIfExpired(deadlineDate)==true) {
                    this.expired = true;
                }
            } catch(ParseException p) {
                this.deadline = "-";
            }
        } else {
            this.deadline = "-";
        }
        this.category = category;
        this.active = true;  
    }
    
    //constructor with option to set inactive right off the bat (for populate only)
    //other than that exactly the same
    public Note(String title, String targetUser, String author,
            String message, String deadline, String category, boolean active) {
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        this.title = title;
        if (targetUser != null) { 
            this.targetUser = targetUser; 
        } else {
            this.targetUser = "-";
        }
        this.author = author;
        this.message = message;
        this.expired = false;
        if(deadline != null) {
            this.deadline = deadline;
            try {
                Date deadlineDate = dateFormat.parse(this.deadline);
                if (checkIfExpired(deadlineDate)==true) {
                    this.expired = true;
                }
            } catch(ParseException p) {
                this.deadline = "-";
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
            //new date is always current date
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
    
    //check if the date has expired
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

    //Prints out all the information about Note
    @Override
    public String toString() {
        return "Content: " + getMessage() + "\nCategory: " + getCategory() + 
                "\nTarget User: " + getTargetUser() + "\nAuthor: " + getAuthor() + 
                "\nExpire Date: " + getDeadline();
    }
}
