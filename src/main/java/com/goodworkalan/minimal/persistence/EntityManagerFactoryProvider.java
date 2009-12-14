package com.goodworkalan.minimal.persistence;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.goodworkalan.paste.Application;
import com.goodworkalan.paste.janitor.Janitor;
import com.goodworkalan.paste.janitor.JanitorQueue;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Provides a JPA entity manager factory. Bound to the Servlet scope, the
 * entity manager provided is effectively a singleton.
 *
 * @author Alan Gutierrez
 */
public class EntityManagerFactoryProvider implements Provider<EntityManagerFactory> {
    /** The Servlet scoped janitor queue. */
    private final JanitorQueue janitors;

    /**
     * Create an entity manager factory provider that closes the the entity
     * manager in a janitor that is added to the given Servlet scoped janitor queue.
     * 
     * @param janitors
     *            The Servlet scoped janitor queue.
     */
    @Inject
    public EntityManagerFactoryProvider(@Application JanitorQueue janitors) {
        this.janitors = janitors;
    }

    /**
     * Get the entity manager factory.
     * 
     * @return The entity manager factory.
     */
    public EntityManagerFactory get() {
        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        janitors.add(new Janitor() {
            public void cleanUp() {
                emf.close();
            }
        });
        return emf;
    }
}
