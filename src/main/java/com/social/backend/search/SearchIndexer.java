package com.social.backend.search;

import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SearchIndexer {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @EventListener(ApplicationReadyEvent.class)
    public void index() {
        // Create a dedicated EntityManager for the indexing process
        // This avoids the "No transactional EntityManager" proxy error
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            SearchSession searchSession = Search.session(em);

            searchSession.massIndexer()
                    .threadsToLoadObjects(4)
                    .batchSizeToLoadObjects(25)
                    .startAndWait();

            System.out.println("Indexing completed successfully!");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}