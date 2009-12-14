package com.goodworkalan.minimal.guice;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.goodworkalan.infuse.CollectionFactory;
import com.goodworkalan.infuse.ObjectFactory;
import com.goodworkalan.infuse.guice.GuiceFactory;
import com.goodworkalan.minimal.persistence.EntityFactoryProvider;
import com.goodworkalan.minimal.persistence.EntityManagerFactoryProvider;
import com.goodworkalan.minimal.persistence.EntityManagerProvider;
import com.goodworkalan.paste.ApplicationScoped;
import com.goodworkalan.paste.RequestScoped;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

/**
 * Guice bindings for participating interfaces.
 *
 * @author Alan Gutierrez
 */
public class MinimalModule extends AbstractModule {
    /**
     * Bind interfaces to implementations and providers.
     */
    @Override
    protected void configure() {
        bind(EntityManagerFactory.class).toProvider(EntityManagerFactoryProvider.class).in(ApplicationScoped.class);
        bind(EntityManager.class).toProvider(EntityManagerProvider.class).in(RequestScoped.class);
        Multibinder<ObjectFactory> factories = Multibinder.newSetBinder(binder(), ObjectFactory.class);
        factories.addBinding().toProvider(EntityFactoryProvider.class);
        factories.addBinding().to(CollectionFactory.class);
        factories.addBinding().to(GuiceFactory.class).in(RequestScoped.class);
    }
}