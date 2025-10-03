package com.thiagosalvess.admin.catalogy.domain.event;

@FunctionalInterface
public interface DomainEventPublisher {

    void publishEvent(DomainEvent event);
}
