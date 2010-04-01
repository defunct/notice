package com.goodworkalan.notice.viewer.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.goodworkalan.notice.viewer.guice.NoticeViewerModule;
import com.goodworkalan.paste.Request;
import com.goodworkalan.paste.janitor.Janitor;
import com.goodworkalan.paste.janitor.JanitorQueue;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Provides and entity manager using the singleton entity manager factory that
 * is closed at the end of the request. This entity manager provider is bound to
 * the request scope in {@link NoticeViewerModule}.
 * 
 * @author Alan Gutierrez
 */
public class EntityManagerProvider implements Provider<EntityManager> {
    /** The entity manager factory. */
    private final EntityManagerFactory emf;

    /** The request scoped janitor queue. */
    private final JanitorQueue janitors;

    /**
     * Create an entity manager provider that creates an entity manager using
     * the given entity manager factory and closes the the entity manager in a
     * janitor that is added to the given request janitor queue.
     * 
     * @param emf
     *            The entity manager factory.
     * @param janitors
     *            The request janitor queue.
     */
    @Inject
    public EntityManagerProvider(EntityManagerFactory emf, @Request JanitorQueue janitors) {
        this.emf = emf;
        this.janitors = janitors;
    }

    /**
     * Get a newly created entity manager.
     * 
     * @return An entity manager.
     */
    public EntityManager get() {
        final EntityManager em = emf.createEntityManager();

        janitors.add(new Janitor() {
            public void cleanUp() {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }

                if (em.isOpen()) {
                    em.close();
                }
            }
        });

        return em;
    }
}