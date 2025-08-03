package com.vanixmc.events.event.domain;

import com.vanixmc.events.shared.DomainConfig;

@FunctionalInterface
public interface EventBuilder {
    Event build(DomainConfig config);
}
