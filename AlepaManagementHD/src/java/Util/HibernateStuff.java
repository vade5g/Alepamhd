
package Util;

import org.hibernate.SessionFactory;
import org.hibernate.SessionFactoryObserver;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.registry.internal.StandardServiceRegistryImpl;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class HibernateStuff {
    private final SessionFactory sessionFactory;
    
    public HibernateStuff() {
        Configuration config = new Configuration();

        //config.addAnnotatedClass(Model.Car.class);
        //config.addAnnotatedClass(Model.Person.class);

        config = config.configure();
        
        new SchemaExport(config).create(true, true);
        
        StandardServiceRegistryBuilder serviceRegistryBuilder = 
                new StandardServiceRegistryBuilder();
        serviceRegistryBuilder.applySettings(config.getProperties());

        ServiceRegistry serviceRegistry = serviceRegistryBuilder.build();
        
        config.setSessionFactoryObserver(new SessionFactoryObserver() {
            @Override
            public void sessionFactoryCreated(SessionFactory factory) {}
            @Override
            public void sessionFactoryClosed(SessionFactory factory) {
                System.out.println("sessionFactoryClosed()");
                ((StandardServiceRegistryImpl) serviceRegistry).destroy();
            }
        });

        this.sessionFactory = config.buildSessionFactory(serviceRegistry);
    }
    
    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }
    
    public static HibernateStuff getInstance() {
        return HibernateStuffHolder.INSTANCE;
    }
    
    private static class HibernateStuffHolder {
        private static final HibernateStuff INSTANCE = new HibernateStuff();
    }
}  
