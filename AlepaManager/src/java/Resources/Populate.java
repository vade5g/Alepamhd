
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
        List<Useri> list = new ArrayList<>();
        list.add(new Useri("Jonathan", "Muroma", "Jonathan666", "perkele", "manager", "jonathan@gmail.com"));
        list.add(new Useri("Markus", "Keinänen", "Markus666", "perkele", "janitor", "markus@gmail.com"));
        list.add(new Useri("Hannu", "Junno", "Hannu666", "perkele", "guard", "hannu@gmail.com"));
        list.add(new Useri("Valtteri", "Ukkola", "Vade666", "perkele", "cashier", "vade@gmail.com"));
        list.add(new Useri("mannu", "pekkanen", "mannu666", "perkele", "janitor", "mannu@gmail.com"));
        list.add(new Useri("pekka", "mannunen", "pekka666", "perkele", "janitor", "pekka@gmail.com"));
        
        
        for (Useri s : list) {
            session.saveOrUpdate(s);
        }
        
        session.getTransaction().commit();
        
        return "database populated!";
    }
    
}
