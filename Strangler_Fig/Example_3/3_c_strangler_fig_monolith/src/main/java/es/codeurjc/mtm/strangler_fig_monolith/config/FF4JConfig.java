package es.codeurjc.mtm.strangler_fig_monolith.config;

import org.ff4j.FF4j;
import org.ff4j.audit.repository.InMemoryEventRepository;
import org.ff4j.property.store.InMemoryPropertyStore;
import org.ff4j.store.InMemoryFeatureStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FF4JConfig {

  @Bean
  public FF4j getFF4j() {
    FF4j ff4j = new FF4j();

    /*
     * Implementation of each store. Here this is boiler plate as if nothing is
     * specified the inmemory is used. Those are really the one that will change
     * depending on your technology.
     */
    ff4j.setFeatureStore(new InMemoryFeatureStore());
    ff4j.setPropertiesStore(new InMemoryPropertyStore());
    ff4j.setEventRepository(new InMemoryEventRepository());

    // Enabling audit and monitoring, default value is false
    ff4j.audit(true);

    // When evaluting not existing features, ff4j will create then but disabled
    ff4j.autoCreate(true);

    // To define RBAC access, the application must have a logged user
    // ff4j.setAuthManager(...);

    // To define a cacher layer to relax the DB, multiple implementations
    // ff4j.cache([a cache Manager]);

    return ff4j;
  }
}