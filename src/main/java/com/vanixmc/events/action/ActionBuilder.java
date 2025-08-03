package com.vanixmc.events.action;

import com.vanixmc.events.shared.DomainConfig;

@FunctionalInterface
public interface ActionBuilder {
    Action build(DomainConfig config);
}
