package com.goodworkalan.minimal.persistence;

import javax.persistence.EntityManager;

import com.goodworkalan.infuse.jpa.EntityFactory;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Provides an Infuse entity factory to populate controllers with domain
 * objects from the database. 
 * 
 * @author Alan Gutierrez
 */
public class EntityFactoryProvider implements Provider<EntityFactory> {
    /** The entity manager. */
    private EntityManager em;

    /**
     * Create an entity factory provider that creates an entity factory that
     * selects objects using the given JPA entity manager.
     * 
     * @param em
     *            The entity manager.
     */
    @Inject
    public EntityFactoryProvider(EntityManager em) {
        this.em = em;
    }

    /**
     * Get the entity factory.
     * 
     * @return The entity factory.
     */
    public EntityFactory get() {
        return new EntityFactory(em);
    }
}
