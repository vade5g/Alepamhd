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
        
        //initialize the tools we need to search the database
        entry = entry.trim();
        String[] entryList = entry.split(" ");
        
        Criteria criteria = session.createCriteria(Useri.class);
        List<Useri> matchList = new ArrayList<>();
        session.getTransaction().commit();
        
        if (entryList.length > 2) {
            return matchList;
        } else if (entryList.length == 1) {
            String word = entryList[0];
            criteria.add(Restrictions.or(Restrictions.like("firstname", "%"+word+"%"),
                                        Restrictions.like("lastname", "%"+word+"%")));
            matchList = criteria.list();
            return matchList;
        } else if (entryList.length == 2) {
            //the user entered for example "Jannu Pekka", we must try both words
            String word = entryList[0];
            criteria.add(Restrictions.or(Restrictions.like("firstname", "pekka"),
                                        Restrictions.like("lastname", "%"+word+"%")));
            matchList = criteria.list();
            if (!matchList.isEmpty()) {
                //if something was found with first word, return list
                return matchList;
            } else {
                //nothing found in first or last names with this word!
                //create new criteria and try again with the other word
                word = entryList[1];
                criteria = session.createCriteria(Useri.class);
                criteria.add(Restrictions.or(Restrictions.like("firstname", "%"+word+"%"),
                                        Restrictions.like("lastname", "%"+word+"%")));
                matchList = criteria.list();
            }
        }
        //return whether it came out empty or not
        return matchList;
    }
}
