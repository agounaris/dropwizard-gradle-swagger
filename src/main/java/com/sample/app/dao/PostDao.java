package com.sample.app.dao;

import com.google.inject.Inject;
import com.sample.app.core.Post;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

public class PostDao extends AbstractDAO<Post>{
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    @Inject
    public PostDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Optional<Post> findById(Long id) {
        return Optional.ofNullable(get(id));
    }

    public Post create(Post post) {
        return persist(post);
    }

    public List<Post> findAll() {
        return super.criteria().list();
    }
}
