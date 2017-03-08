/*
 * Resource address for adding new users in registering phase as well as
 * querying all users from the bottom right in overview screen 
 */
package Resources;

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

@Path("/users")
public class Users {
    
    @Path("register/{firstname}/{lastname}/{username}/{password}/{category}/{email}")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String addUser(@PathParam("firstname") String firstname,
                        @PathParam("lastname") String lastname,
                        @PathParam("username") String username,
                        @PathParam("password") String password,
                        @PathParam("category") String category,
                        @PathParam("email") String email) {
        
        //basic initializement
        SessionFactory sf = HibernateStuff.getInstance().getSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
        //actual stuff begins
        
        Useri newUser = new Useri(firstname, lastname, username, password, category, email);
        
        session.saveOrUpdate(newUser);
        session.getTransaction().commit();
        
        return "Registration successful. User "+username+" has been added to the database.";
    }
    
    @Path("login/{username}/{password}")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String tryLogin(@PathParam("username") String username,
                           @PathParam("password") String password) {
        
        //basic initializement
        SessionFactory sf = HibernateStuff.getInstance().getSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
        //actual stuff begins
        
        Criteria criteria = session.createCriteria(Useri.class);
        Useri target;
        criteria.add(Restrictions.like("username", username));
        List<Useri> matchList = criteria.list();
        if (matchList.isEmpty()) {
            return "bad username";
        }
        criteria.add(Restrictions.like("password", password));
        matchList = criteria.list();
        if (matchList.isEmpty()) {
            return "bad password";
        }
        target = matchList.get(0);
        session.getTransaction().commit();
        return target.getFirstname()+" "+target.getLastname();
    }
    
    @Path("find")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<Useri> getAllUsers() {
        
        //basic initializement
        SessionFactory sf = HibernateStuff.getInstance().getSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
        //actual stuff begins
        
        Criteria criteria = session.createCriteria(Useri.class);
        List<Useri> matchList = new ArrayList<>();
        matchList = criteria.list();
        session.getTransaction().commit();
        return matchList;
    }
    
    @Path("find/{entry}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<Useri> findUsers(@PathParam("entry") String entry) {
        //basic initializement
        SessionFactory sf = HibernateStuff.getInstance().getSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
        //actual stuff begins
        System.out.println(entry);
        //initialize the tools we need to search the database
        entry = entry.trim();
        String[] entryList = entry.split(" ");
        Criteria criteria = session.createCriteria(Useri.class);
        List<Useri> matchList = new ArrayList<>();
        
        //if the entry was invalid, return empty
        if (entryList.length > 2) {
            return matchList;
        //if the entry was one word
        } else if (entryList.length == 1) {
            String word = entryList[0];
            criteria.add(Restrictions.or(Restrictions.like("firstname", "%"+word+"%"),
                                        Restrictions.like("lastname", "%"+word+"%")));
            matchList = criteria.list();
            return matchList;
        } else if (entryList.length == 2) {
            //the user entered for example "Jannu Pekka", we must try both words
            String firstname = entryList[0];
            String lastname = entryList[1];
            System.out.println("Searched for "+firstname+" "+lastname);
            //first we see if first part of entry is in firstnames
            criteria = session.createCriteria(Useri.class);
            criteria.add(Restrictions.or(Restrictions.like("firstname", "%"+firstname+"%"),
                                        Restrictions.like("lastname", "%"+firstname+"%")));
            matchList = criteria.list();
            if (!matchList.isEmpty()) {
                //if something was found, move onto the second word and update more precise list
                criteria.add(Restrictions.or(Restrictions.like("firstname", "%"+lastname+"%"),
                                        Restrictions.like("lastname", "%"+lastname+"%")));
                matchList = criteria.list();
                return matchList;
            } else {
                return matchList;
            }
        }
        //return whether it came out empty or not
        session.getTransaction().commit();
        return matchList;
    }
    
    @Path("/getid/{entry}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public int getIdByName(@PathParam("entry") String entry) {
        //basic initializement
        SessionFactory sf = HibernateStuff.getInstance().getSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
        //actual stuff begins
        
        //initialize the tools we need to search the database
        entry = entry.trim();
        String[] entryList = entry.split(" ");
        
        Criteria criteria = session.createCriteria(Useri.class);
        List<Useri> matchList = new ArrayList<>();
        session.getTransaction().commit();
        Useri user; int id=-1;
        //if the entry was invalid, return empty array
        if (entryList.length != 2) {
            return -1;
            //if the entry was one word
        } else {
            String firstname = entryList[0];
            String lastname= entryList[1];
            criteria.add(Restrictions.like("firstname", firstname));
            criteria.add(Restrictions.like("lastname", lastname));
            matchList = criteria.list();
            //if nothing was found, return empty array
            if (matchList.isEmpty()) {
                return -1;
            } else {
                matchList = criteria.list();
                user = matchList.get(0);
                id = user.getId();
            }
        }
        return id;
    }
    
    @Path("/resetnotifications/{id}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public int getNotificationsByID(@PathParam("id") int userID) {
        
        //basic initializement
        SessionFactory sf = HibernateStuff.getInstance().getSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
        //actual stuff begins
        
        Criteria criteria = session.createCriteria(Useri.class);
        criteria.add(Restrictions.like("id", userID));
        List<Useri> matchList = new ArrayList<>();
        matchList = criteria.list();
        if (!matchList.isEmpty()) {
            Useri user = matchList.get(0);
            user.setNotifications(0);
        } else {
            return -1;
        }
        session.getTransaction().commit();
        return 0;
    }
}
