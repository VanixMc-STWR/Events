package com.vanixmc.events.trigger_tests;

import com.vanixmc.events.shared.DomainConfig;
import com.vanixmc.events.shared.TickTime;
import com.vanixmc.events.trigger.factory.TriggerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RecurringTriggerTest {

    private TriggerFactory triggerFactory;

    @BeforeEach
    public void initFactory() {
        this.triggerFactory = new TriggerFactory();
    }


    @Test
    public void testSuccessfulRegexParsingSeconds() {
        DomainConfig domainConfig = new DomainConfig();

        domainConfig.getConfig().put("test_seconds", "3 seconds");

        domainConfig.getConfig().put("test_second", "3 second");

        domainConfig.getConfig().put("test_s", "3 s");

        TickTime testSeconds = domainConfig.parseTime("test_seconds");

        TickTime testSecond = domainConfig.parseTime("test_second");

        TickTime testS = domainConfig.parseTime("test_s");

        System.out.println(testSeconds);
        System.out.println(testSecond);
        System.out.println(testS);

        assertEquals(3, testSeconds.getDuration());
        assertEquals(TimeUnit.SECONDS, testSeconds.getTimeUnit());

        assertEquals(3, testSecond.getDuration());
        assertEquals(TimeUnit.SECONDS, testSecond.getTimeUnit());

        assertEquals(3, testS.getDuration());
        assertEquals(TimeUnit.SECONDS, testS.getTimeUnit());
    }

    @Test
    public void testSuccessfulRegexParsingRepetitions() {
        DomainConfig domainConfig = new DomainConfig();

        domainConfig.getConfig().put("repetitions", "3");

        assertEquals(3, domainConfig.parseRepetitions());
    }

    @Test
    public void testSuccessfulParsingRepetitionsInf() {
        DomainConfig domainConfig = new DomainConfig();

        domainConfig.getConfig().put("repetitions", "inf");

        assertEquals(-1, domainConfig.parseRepetitions());
    }

}
