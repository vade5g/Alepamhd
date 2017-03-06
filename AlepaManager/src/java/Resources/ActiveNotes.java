
package Resources;

import Model.Note;
import Model.Useri;
import Util.HibernateStuff;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

@Path("/activenotes")
public class ActiveNotes {
    
    @Path("{title}/{target}/{author}/{message}/{deadline}/{category}")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String addNote(@PathParam("title") String title,
                        @PathParam("target") String target,
                        @PathParam("author") String author,
                        @PathParam("message") String message,
                        @PathParam("deadline") String deadline,
                        @PathParam("category") String category) {
        //basic initializement
        SessionFactory sf = HibernateStuff.getInstance().getSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
        //actual stuff begins
        if (target.equals("none")) {
            target=null;
        } else if (deadline.equals("none")) {
            deadline=null;
        }
        Note newNote = new Note(title, target, author,  message, deadline, category);
        session.saveOrUpdate(newNote);
        session.getTransaction().commit();
        
        return title;
    }
    
    @Path("{title}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Note getNote(@PathParam("title") String title) {
        
        //basic initializement
        SessionFactory sf = HibernateStuff.getInstance().getSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
        //actual stuff begins
        List<Note> matchList = new ArrayList<>();
        Note note = null;
        Criteria criteria = session.createCriteria(Note.class);
        criteria.add(Restrictions.like("title", title));
        matchList = criteria.list();
        if (!matchList.isEmpty()) {
            note = matchList.get(0);
        }
        session.getTransaction().commit();
        return note;
    }
    
//    @Path("{id}")
//    @GET
//    @Produces(MediaType.APPLICATION_XML)
//    public List<Note> getNotesOfAuthor(@PathParam("id") int userID) {
//        //basic initializement
//        SessionFactory sf = HibernateStuff.getInstance().getSessionFactory();
//        Session session = sf.openSession();
//        session.beginTransaction();
//        
//        //stuff begins
//        Criteria criteria = session.createCriteria(Useri.class);
//        criteria.add(Restrictions.like("id", userID));
//        List<Useri> userList = new ArrayList<>(); Useri user;
//        List<Note> notes = new ArrayList<>();
//        userList = criteria.list();
//        if (!userList.isEmpty()) {
//            //get the user by this ID
//            user = userList.get(0);
//        } else {
//            return notes;
//        }
//        String category = user.getCategory();
//        //reset criteria
//        criteria = session.createCriteria(Note.class);
//        criteria.add(Restrictions.like("category", category));
//        //criteria.add(Restrictions.like("expired", true));
//        notes = criteria.list();
//        session.getTransaction().commit();
//        return notes;
//    }
}
