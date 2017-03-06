
package Resources;

import Model.Note;
import Model.Useri;
import Util.HibernateStuff;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

@Path("/populate")
public class Populate {
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String populate() {
        
        //basic initializement
        SessionFactory sf = HibernateStuff.getInstance().getSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
        //actual stuff begins
        //adding users to database
        List<Useri> users = new ArrayList<>();
        
        users.add(new Useri("Matti", "Miettinen", "masa", "maza", "manager", "masa@gmail.com"));        
        users.add(new Useri("Maria", "Muurila", "marge", "Muumilaakso", "cashier", "maria@gmail.com"));
        users.add(new Useri("Markus", "Markkula", "make", "maketsu", "cashier", "make@saunalahti.fi"));
        users.add(new Useri("Tuija", "Tuijala", "tuija", "tuijaa", "cashier", "tuija@gmail.com"));
        users.add(new Useri("Tuuli", "Tuulimylly", "tuksu", "tuksu123", "cashier", "tuuli@hotmail.com"));
        users.add(new Useri("Sirpa", "Sirpola", "sirppi", "sirppi123", "cashier", "sirppi@gmail.com"));
        users.add(new Useri("Emmi", "Virtanen", "emmi", "emmi123", "cashier", "emmi@elisa.fi"));
        users.add(new Useri("Pena", "Peippola", "pena", "penapena", "cashier", "pena@hotmail.com"));       
        users.add(new Useri("Valtteri", "Vattula", "vattu", "vattu123", "warehouse", "valtteri@gmail.com"));
        users.add(new Useri("Uuno", "Ukkola", "ude", "uunoo", "warehouse", "uuno@gmail.com"));
        users.add(new Useri("Kalle", "Koskinen", "kossu", "kalle123", "guard", "kalle@gmail.com"));
        users.add(new Useri("Jonathan", "Johnson", "johnson", "johnson123", "guard", "johnson@gmail.com"));
        users.add(new Useri("Pekka", "Mannunen", "peksu", "peksu123", "janitor", "pekka@gmail.com"));
        users.add(new Useri("Margetta", "Marjala", "margetta", "marjamaa", "janitor", "margetta@gmail.com"));
                                                                
        for (Useri s : users) {
            session.saveOrUpdate(s);
        }
        
        //adding notes to database
        List<Note> notes = new ArrayList<>();
        notes.add(new Note("Broken glass", null, "Tuuli", "Glass broken in section A4, please clean it", null, "janitor"));
        notes.add(new Note("Fill shelves", "Pena", "Maria", "Fetch 30 boxes of chicken from warehouse to B2", "10.3.2017, 10:00", "cashier"));
        notes.add(new Note("Ketchup accident", null, "Maria", "Ketchup accident in D1", null, "janitor"));
        notes.add(new Note("Out of apples", "Valtteri", "Tuija", "Need apples to D5", null, "warehouse"));
        notes.add(new Note("Need guard asap", null, "Tuuli", "Suspicious people hanging around the front door", "7.3.2017, 17:00", "guard"));
        notes.add(new Note("Make coffee", "Pena", "Maria", "Make sure there is coffee at the meeting tomorrow 13:00", "15.3.2017, 12:30", "janitor"));
        notes.add(new Note("Cash register C1 not working", "masa", "Sirpa", "Glass broken in section A4", null, "manager"));
        notes.add(new Note("Need assistance with cash register", null, "Pena", "I need some assistance using the cash register", null, "cashier"));
        notes.add(new Note("Filling shelves", null, "Emmi", "At B2 theres a few packed boxes that could be filled into shelves", null, "cashier"));
        notes.add(new Note("Meat delivery", "Valtteri", "Uuno", "Meat delivery coming at 08:00", "10.3.2017, 08:00", "warehouse"));
        notes.add(new Note("Fish delivery", "Uuno", "Valtteri", "Fish delivery coming at 07:00", "10.3.2017, 07:00", "warehouse"));
        notes.add(new Note("Paper delivery", "Valtteri", "Uuno", "Paper delivery coming at 11:00", "10.3.2017, 11:00", "warehouse"));
        notes.add(new Note("Spices delivery", "Uuno", "Valtteri", "Spices delivery coming at 12:00", "10.3.2017, 12:00", "warehouse"));
        notes.add(new Note("Clean break room", null, "masa", "Break room is a mess, please clean it up", null, "janitor"));
        notes.add(new Note("Move the cleaning wagons", null, "Sirpa", "The cleaning wagon is left at A3, please move it somewhere else", "7.3.2017, 10:00", "janitor"));
        notes.add(new Note("Cleaning wagon missing", "masa", "Margetta", "Someone has stolen the cleaning wagon while I was in the toilet", null, "manager"));
        notes.add(new Note("Hooligans stole cleaning wagon", null, "Maria", "Hooligans stole the cleaning wagon and are playing outside with it", "7.3.2017, 19:00", "guard"));
                        
        for (Note s : notes) {
            session.saveOrUpdate(s);
        }
                
        session.getTransaction().commit();
        
        return "database populated!";
    }
    
}
