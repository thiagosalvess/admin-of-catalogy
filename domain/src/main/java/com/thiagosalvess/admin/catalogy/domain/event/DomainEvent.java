package com.thiagosalvess.admin.catalogy.domain.event;

import java.io.Serializable;
import java.time.Instant;

public interface DomainEvent extends Serializable {

    Instant occurredOn();
}
