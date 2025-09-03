package com.vanixmc.events.trigger_tests;


import com.vanixmc.events.trigger.domain.TriggerType;
import com.vanixmc.events.trigger.factory.TriggerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RecurringTriggerTest {

    private TriggerFactory triggerFactory;

    @BeforeEach
    public void initFactory() {
        this.triggerFactory = new TriggerFactory();
    }

    @Test
    public void testSuccessfulRecurringTriggerBuild() {

    }
}
