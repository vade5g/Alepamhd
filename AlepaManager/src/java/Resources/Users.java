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
            return "This username doesn't exist. Please register to continue.";
        }
        criteria.add(Restrictions.like("password", password));
        matchList = criteria.list();
        if (matchList.isEmpty()) {
            return "The password is wrong. Please try again.";
        }
        target = matchList.get(0);
        session.getTransaction().commit();
        return "Login successful. Welcome, " + target.getFirstname() + "!";
        
    }
    
}
