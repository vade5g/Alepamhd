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
    
    @Path("{firstname}/{lastname}/{username}/{password}/{category}")
    @POST
    public String addUser(@PathParam("firstname") String firstname,
                        @PathParam("lastname") String lastname,
                        @PathParam("username") String username,
                        @PathParam("password") String password,
                        @PathParam("category") String category) {
        
        //basic initializement
        SessionFactory sf = HibernateStuff.getInstance().getSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
        //actual stuff begins
        
        Useri newUser = new Useri(firstname, lastname, username, password, category);
        
        session.saveOrUpdate(newUser);
        session.getTransaction().commit();
        
        return firstname+ " "+lastname +" "+ username +" "+ password +" "+ category;
        
    }
    
}
