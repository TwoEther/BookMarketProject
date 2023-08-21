package org.project.shop;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class AppConfig {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("shop");

}
