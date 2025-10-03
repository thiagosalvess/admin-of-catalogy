package com.thiagosalvess.admin.catalogy.infrastructure.configuration;

import com.google.cloud.storage.Storage;
import com.thiagosalvess.admin.catalogy.infrastructure.configuration.properties.google.GoogleStorageProperties;
import com.thiagosalvess.admin.catalogy.infrastructure.configuration.properties.storage.StorageProperties;
import com.thiagosalvess.admin.catalogy.infrastructure.services.StorageService;
import com.thiagosalvess.admin.catalogy.infrastructure.services.impl.GCStorageService;
import com.thiagosalvess.admin.catalogy.infrastructure.services.local.InMemoryStorageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class StorageConfig {

    @Bean
    @ConfigurationProperties(value = "storage.catalogy-videos")
    public StorageProperties storageProperties() {
        return new StorageProperties();
    }

    @Bean()
    @Profile({"development", "test-integration ", "test-e2e"})
    public StorageService inMemoryStorageService() {
        return new InMemoryStorageService();
    }

    @Bean()
    @ConditionalOnMissingBean
    public StorageService gcStorageService(
            final GoogleStorageProperties props,
            final Storage storage
    ) {
        return new GCStorageService(props.getBucket(), storage);
    }
}
