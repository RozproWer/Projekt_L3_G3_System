package com.giga;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Singleton Class that manages access to Hibernate sessionFactory Object
 *
 * @author GigaNByte
 * @since 1.0
 */

public class HibernateConnection {

    private static SessionFactory sessionFactory;

    private HibernateConnection() {
    }

    /**
     * @return Hibernate Session Factory Object
     */
    public static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            //jar driver not found fix
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {

            }

            sessionFactory = new Configuration()
                    .configure().buildSessionFactory();
        }
        return sessionFactory;
    }

}
