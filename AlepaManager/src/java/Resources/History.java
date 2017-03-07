/*
 * Querying for history-related things
 */
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

/**
 *
 * @author Markus
 */
@Path("/history")
public class History {

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<Note> getHistoryByID(@PathParam("id") int userID) {
        //basic initializement
        SessionFactory sf = HibernateStuff.getInstance().getSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
        
        //stuff begins
        Criteria criteria = session.createCriteria(Useri.class);
        criteria.add(Restrictions.like("id", userID));
        List<Useri> userList = new ArrayList<>(); Useri user;
        List<Note> notes = new ArrayList<>();
        userList = criteria.list();
        if (!userList.isEmpty()) {
            //get the user by this ID
            user = userList.get(0);
        } else {
            return notes;
        }
        String target = user.getFirstname() + " " + user.getLastname();
        //reset criteria
        criteria = session.createCriteria(Note.class);
        criteria.add(Restrictions.like("targetUser", target));
        criteria.add(Restrictions.eq("active", false));
        notes = criteria.list();
        session.getTransaction().commit();
        return notes;
    }
    
    @Path("/category/{category}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<Note> getNotesByCategory(@PathParam("category") String category) {
        //basic initializement
        SessionFactory sf = HibernateStuff.getInstance().getSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
        
        //stuff begins
//        Criteria criteria = session.createCriteria(Useri.class);
//        criteria.add(Restrictions.like("id", userID));
//        List<Useri> userList = new ArrayList<>(); Useri user;
        List<Note> notes = new ArrayList<>();
//        userList = criteria.list();
//        if (!userList.isEmpty()) {
//            //get the user by this ID
//            user = userList.get(0);
//        } else {
//            return notes;
//        }
//        String author = user.getFirstname() + " " + user.getLastname();
        //reset criteria
        Criteria criteria = session.createCriteria(Note.class);
        criteria.add(Restrictions.like("category", category));
        criteria.add(Restrictions.eq("active", false));
        notes = criteria.list();
        session.getTransaction().commit();
        return notes;
    }
    
}
