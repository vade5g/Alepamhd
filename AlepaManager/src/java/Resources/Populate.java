
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
        notes.add(new Note("Dirt on shelf", "Valtteri Vattula", "Valtteri Vattula", "Warehouse shelf is possibly cracked? Can you fix?", "08.03.2017", "warehouse", false));
        notes.add(new Note("Dip delivery", "Valtteri Vattula", "Tuuli Tuulimylly", "Special dip delivery coming in at some point next week", "15.03.2017", "warehouse", false));
        notes.add(new Note("Jenkki gum", "Valtteri Vattula", "Tuuli Tuulimylly", "Check up on weekly consumption of Jenkki gum", "06.03.2017", "warehouse", false));
        notes.add(new Note("Pepper ham", "Valtteri Vattula", "Tuuli Tuulimylly", "Check up on weekly consumption of peppered ham", "05.03.2017", "warehouse"));
        notes.add(new Note("Excess boxes in backroom", "Valtteri Vattula", "Tuuli Tuulimylly", "Stack of empty box cartons in the storage, pls clean", null, "warehouse"));
        notes.add(new Note("Meat delivery", "Valtteri Vattula", "Uuno Ukkola", "Meat delivery coming at 08:00", "10.03.2017", "warehouse"));
        notes.add(new Note("Paper delivery", "Valtteri Vattula", "Uuno Ukkola", "Paper delivery coming at 11:00", "10.03.2017", "warehouse"));
        notes.add(new Note("Out of apples", "Valtteri Vattula", "Tuija Tuijala", "Need apples to D5", null, "warehouse"));
        notes.add(new Note("Spices delivery", "Uuno Ukkola", "Valtteri Vattula", "Spices delivery coming at 12:00", "10.03.2017", "warehouse"));
        notes.add(new Note("Fish delivery", "Uuno Ukkola", "Valtteri Vattula", "Fish delivery coming at 07:00", "10.03.2017", "warehouse"));

        notes.add(new Note("Need assistance with cash register", null, "Pena Peippola", "I need some assistance using the cash register", null, "cashier", false));        
        notes.add(new Note("Fill shelves", "Pena Peippola", "Maria Muurila", "Fetch 30 boxes of chicken from warehouse to B2", null, "cashier", false));
        notes.add(new Note("Filling shelves", null, "Emmi Virtanen", "At B2 theres a few packed boxes that could be filled into shelves", "02.03.2017", "cashier"));
        notes.add(new Note("Make coffee", "Pena Peippola", "Maria Muurila", "Make sure there is coffee at the meeting tomorrow 13:00", "15.03.2017", "cashier"));        
        notes.add(new Note("Remove the sale add", null, "Valtteri Vattula", "Remove the sale add next to candy shelves", "04.03.2017", "cashier"));
        notes.add(new Note("Mark almost expired items", null, "Maria Muurila", "Put sale tags on expire", "10.03.2017", "cashier"));        
        notes.add(new Note("Fill meat shelves", null, "Emmi Virtanen", "The meat shelves need to be filled", null, "cashier"));
        notes.add(new Note("More cookies needed", null, "Maria Muurila", "Cookie shelves are almost empty and need filling", null, "cashier"));
        notes.add(new Note("Remove the add stand", null, "Maria Muurila", "The add stand in the meat section should be removed completely. Move it to the storage somewhere where it isn't blocking anything", "10.03.2017", "chashier"));
                
        notes.add(new Note("Ketchup accident", null, "Maria Muurila", "Ketchup accident in D1", null, "janitor", false));
        notes.add(new Note("Clean the soda", null, "Maria Muurila", "Some kids spilled soda next to the candy shelves", null, "janitor", false));
        notes.add(new Note("Clean break room", null, "Matti Miettinen", "Break room is a mess, please clean it up", "28.02.2017", "janitor"));
        notes.add(new Note("Move the cleaning wagons", null, "Sirpa Sirpola", "The cleaning wagon is left at A3, please move it somewhere else", "07.03.2017", "janitor"));
        notes.add(new Note("Clean cahsregister C1", null, "Emmi Virtanen", "Sirpa threw up in the cashier. I think you should clean it up as soon as possible", null, "janitor"));
        notes.add(new Note("Bums", "Pekka Mannunen", "Jonathan Johnson", "Take care of the bodies. It's pretty messy here", "08.03.2017", "janitor"));
        notes.add(new Note("Hooligans mess", null, "Kalle Koskinen", "The holigans left a huge mess in the break room. There is piss and shit everywhere", "11.03.2017", "janitor"));
        
        notes.add(new Note("Beavers!!!", null, "Tuuli Tuulimylly", "There are freaking beavers on the vegetable section! Remove them at once!!", null, "guard", false));
        notes.add(new Note("Need guard asap", null, "Tuuli Tuulimylly", "Suspicious people hanging around the front door", "07.03.2017", "guard"));        
        notes.add(new Note("Check slot machine users", null, "Tuuli Tuulimylly", "The users on the slot machine seem underage, please check", "01.03.2017", "guard"));
        notes.add(new Note("Bums on the front door", null, "Maria Muurila", "The bums are hanging in fornt of the store again. Finish them off!", "08.03.2017", "guard"));
        notes.add(new Note("Sirpa has been drinking again", null, "Uuno Ukkola", "Sirpa has been drinking again... She is dancing half naked in the halls and disturbing customers. Please take care of her", "01.02.2017", "guard"));
        notes.add(new Note("Hooligans stole cleaning wagon", null, "Maria Muurila", "Hooligans stole the cleaning wagon and are playing outside with it", "07.03.2017", "guard"));
        notes.add(new Note("Common shoplifter", null, "Maria Muurila", "Slick Tony is in the store again. Go keep an eye on him", null, "guard"));
        notes.add(new Note("Hooligan invasion", null, "Pena Peippola", "The hooligans have invaded again! They're barricaded in the break room", null, "guard"));
        notes.add(new Note("Raccoons", null, "Kalle Koskinen", "Raccoons in the dumpster again", null, "guard"));
        
        notes.add(new Note("More money pls boss!!!", "Matti Miettinen", "Pena Peippola", "Pls boss. More moneyz. I need it. Tymanty", "10.10.2017", "manager", false));
        notes.add(new Note("Sirpa's drinking problem", "Matti Miettinen", "Jonathan Johnson", "Please talk with Sirpa about her drinking. This is starting to affect us all", null, "manager", false));        
        notes.add(new Note("Cash register C1 not working", "Matti Miettinen", "Sirpa Sirpola", "Glass broken in section A4", null, "manager"));
        notes.add(new Note("Cleaning wagon missing", "Matti Miettinen", "Margetta Marjala", "Someone has stolen the cleaning wagon while I was in the toilet", null, "manager"));
        notes.add(new Note("New coffee machine", "Ma Miettinen", "Kalle Koskinen", "The coffee machine is starting to fall apart, we need a new one", null, "manager"));
                        
        for (Note s : notes) {
            session.saveOrUpdate(s);
        }
                
        session.getTransaction().commit();
        
        return "database populated!";
    }
    
}
