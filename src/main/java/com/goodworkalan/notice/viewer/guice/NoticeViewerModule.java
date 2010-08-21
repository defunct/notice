package com.goodworkalan.notice.viewer.guice;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.goodworkalan.notice.viewer.persistence.EntityManagerFactoryProvider;
import com.goodworkalan.notice.viewer.persistence.EntityManagerProvider;
import com.goodworkalan.paste.controller.Actors;
import com.goodworkalan.paste.controller.scopes.ApplicationScoped;
import com.goodworkalan.paste.controller.scopes.RequestScoped;
import com.goodworkalan.paste.infuse.Infusable;
import com.goodworkalan.paste.infuse.StashAssignment;
import com.goodworkalan.stringbeans.Converter;
import com.goodworkalan.stringbeans.StringerBuilder;
import com.goodworkalan.stringbeans.jpa.MetaJpaBean;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;

/**
 * Guice bindings for participating interfaces.
 *
 * @author Alan Gutierrez
 */
public class NoticeViewerModule extends AbstractModule {
    /**
     * Bind interfaces to implementations and providers.
     */
    @Override
    protected void configure() {
        bind(EntityManagerFactory.class).toProvider(EntityManagerFactoryProvider.class).in(ApplicationScoped.class);
        bind(EntityManager.class).toProvider(EntityManagerProvider.class).in(RequestScoped.class);
        Converter stringer = new StringerBuilder().isBeanIfAnnotatedWith(Actors.class)
                                                 .isBeanIfAnnotatedWith(Entity.class, MetaJpaBean.class)
                                                 .getInstance();
        bind(Converter.class).toInstance(stringer);
        Multibinder<StashAssignment<?>> keys =  Multibinder.newSetBinder(binder(), new TypeLiteral<StashAssignment<?>>() { }, Infusable.class);
        keys.addBinding().toInstance(new StashAssignment<EntityManager>(MetaJpaBean.ENTITY_MANAGER, EntityManager.class));
    }
}