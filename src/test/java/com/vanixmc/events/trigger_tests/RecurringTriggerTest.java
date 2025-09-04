package com.vanixmc.events.trigger_tests;

import com.sk89q.worldguard.domains.Domain;
import com.vanixmc.events.shared.DomainConfig;
import com.vanixmc.events.shared.TickTime;
import com.vanixmc.events.trigger.factory.TriggerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RecurringTriggerTest {

    private TriggerFactory triggerFactory;

    @BeforeEach
    public void initFactory() {
        this.triggerFactory = new TriggerFactory();
    }

    @Test
    public void testSuccessfulRegexParsingMilliseconds() {
        DomainConfig domainConfig = new DomainConfig();

        domainConfig.getConfig().put("test_m_seconds", "3 milliseconds");

        domainConfig.getConfig().put("test_m_second", "3 millisecond");

        domainConfig.getConfig().put("test_ms", "3 ms");

        TickTime testMilliseconds = domainConfig.parseTime("test_m_seconds");

        TickTime testMillisecond = domainConfig.parseTime("test_m_second");

        TickTime testMs = domainConfig.parseTime("test_ms");

        System.out.println(testMilliseconds);
        System.out.println(testMillisecond);
        System.out.println(testMs);

        assertEquals(3, testMilliseconds.getDuration());
        assertEquals(TimeUnit.MILLISECONDS, testMilliseconds.getTimeUnit());

        assertEquals(3, testMillisecond.getDuration());
        assertEquals(TimeUnit.MILLISECONDS, testMs.getTimeUnit());

        assertEquals(3, testMs.getDuration());
        assertEquals(TimeUnit.MILLISECONDS, testMs.getTimeUnit());
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
    public void testSuccessfulParsingMinutes() {
        DomainConfig domainConfig = new DomainConfig();

        domainConfig.getConfig().put("test_minutes", "3 minutes");

        domainConfig.getConfig().put("test_minute", "3 minute");

        domainConfig.getConfig().put("test_m", "3 m");

        TickTime testMinutes = domainConfig.parseTime("test_minutes");

        TickTime testMinute = domainConfig.parseTime("test_minute");

        TickTime testM = domainConfig.parseTime("test_m");

        System.out.println(testMinutes);
        System.out.println(testMinute);
        System.out.println(testM);

        assertEquals(3, testMinutes.getDuration());
        assertEquals(TimeUnit.MINUTES, testMinutes.getTimeUnit());

        assertEquals(3, testMinute.getDuration());
        assertEquals(TimeUnit.MINUTES, testMinute.getTimeUnit());

        assertEquals(3, testM.getDuration());
        assertEquals(TimeUnit.MINUTES, testM.getTimeUnit());
    }

    @Test
    public void testSuccessfulParsingHours() {
        DomainConfig domainConfig = new DomainConfig();

        domainConfig.getConfig().put("test_hours", "3 hours");

        domainConfig.getConfig().put("test_hour", "3 hour");

        domainConfig.getConfig().put("test_h", "3 h");

        TickTime testHours = domainConfig.parseTime("test_hours");

        TickTime testHour = domainConfig.parseTime("test_hour");

        TickTime testH = domainConfig.parseTime("test_h");

        System.out.println(testHours);
        System.out.println(testHour);
        System.out.println(testH);

        assertEquals(3, testHours.getDuration());
        assertEquals(TimeUnit.HOURS, testHours.getTimeUnit());

        assertEquals(3, testHour.getDuration());
        assertEquals(TimeUnit.HOURS, testHour.getTimeUnit());

        assertEquals(3, testH.getDuration());
        assertEquals(TimeUnit.HOURS, testH.getTimeUnit());
    }

    @Test
    public void testSuccessfulParsingDays() {
        DomainConfig domainConfig = new DomainConfig();

        domainConfig.getConfig().put("test_days", "3 days");

        domainConfig.getConfig().put("test_day", "3 day");

        domainConfig.getConfig().put("test_d", "3 d");

        TickTime testDays = domainConfig.parseTime("test_days");

        TickTime testDay = domainConfig.parseTime("test_day");

        TickTime testD = domainConfig.parseTime("test_d");

        System.out.println(testDays);
        System.out.println(testDay);
        System.out.println(testD);


        assertEquals(3, testDays.getDuration());
        assertEquals(TimeUnit.DAYS, testDays.getTimeUnit());

        assertEquals(3, testDay.getDuration());
        assertEquals(TimeUnit.DAYS, testDay.getTimeUnit());

        assertEquals(3, testD.getDuration());
        assertEquals(TimeUnit.DAYS, testD.getTimeUnit());
    }

    @Test
    public void testSuccessfulRegexParsingRepetitions() {
        DomainConfig domainConfig = new DomainConfig();

        domainConfig.getConfig().put("repetitions", "3");

        assertEquals(3, domainConfig.parseRepetitions());
    }

    @Test
    public void testSuccessfulExceptionForNegativeRepetitions() {
        DomainConfig domainConfig = new DomainConfig();

        domainConfig.getConfig().put("repetitions", "-3");

        assertThrows(IllegalArgumentException.class, domainConfig::parseRepetitions);
    }

    @Test
    public void testSuccessfulExceptionFor0Repetitions() {
        DomainConfig domainConfig = new DomainConfig();

        domainConfig.getConfig().put("repetitions", "0");

        assertThrows(IllegalArgumentException.class, domainConfig::parseRepetitions);
    }

    @Test
    public void testSuccessfulParsingRepetitionsInf() {
        DomainConfig domainConfig = new DomainConfig();

        domainConfig.getConfig().put("repetitions", "inf");

        assertEquals(-1, domainConfig.parseRepetitions());
    }



}
