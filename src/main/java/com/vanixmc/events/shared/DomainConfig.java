package com.vanixmc.events.shared;

import com.ibm.icu.util.TimeUnitAmount;
import com.vanixmc.events.exceptions.NoBuilderDefinedException;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Location;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@ToString
@SuppressWarnings("unchecked | unused")
public class DomainConfig {
    private final Map<String, Object> config;

    public DomainConfig() {
        config = new HashMap<>();
    }

    public DomainConfig(Map<String, Object> data) {
        config = new HashMap<>(data);
    }

    /**
     * Retrieves the value associated with the given key as a String.
     * Returns an empty string if the value is not a String or is absent.
     *
     * @param key the key to look up
     * @return the String value, or an empty string if not found or not a String
     */
    public String getString(String key) {
        Object value = config.get(key);
        return value instanceof String ? (String) value : "";
    }

    /**
     * Retrieves the value associated with the given key as a String.
     * Returns an empty string if the value is not a String or is absent.
     *
     * @param key the key to look up
     * @return the String value, or an empty string if not found or not a String
     */
    public String getUppercaseString(String key) {
        Object value = config.get(key);
        return value instanceof String ? ((String) value).toUpperCase() : "";
    }

    public Integer getInt(String key) {
        Object value = config.get(key);
        return value instanceof Integer ? (Integer) value : null;
    }

    public Long getLong(String key) {
        return (Long) config.get(key);
    }

    public Double getDouble(String key) {
        return (Double) config.get(key);
    }

    public Boolean getBoolean(String key) {
        Object value = config.get(key);
        return value instanceof Boolean ? (Boolean) value : null;
    }

    public Object getObject(String key) {
        return config.get(key);
    }

    public String getId() {
        return (String) config.get("id");
    }

    public List<Object> getObjectList(String key) {
        Object value = config.get(key);
        if (value instanceof List) {
            return (List<Object>) value;
        }
        return Collections.emptyList();
    }

    public List<String> getStringList(String key) {
        Object value = config.get(key);
        if (value instanceof List<?> list) {
            boolean allStrings = list.stream().allMatch(e -> e instanceof String);
            if (allStrings) {
                @SuppressWarnings("unchecked")
                List<String> strings = (List<String>) list;
                return strings;
            }
        }
        return Collections.emptyList();
    }

    // Bukkit object methods

    public Location getLocation(String key) {
        Object value = config.get(key);
        return Location.deserialize((Map<String, Object>) value);
    }

    /**
     * Parses time data from configuration and returns a TickTime object representing
     * said time in ticks.
     *
     * @return TickTime object representing String time data in ticks.
     */
    public TickTime parseTime(String key) {
        Object value = config.get(key);

        if (value == null) return null;

        String tickTimeString = (String)value;

        //  regex separates digits from letters,
        //  and reads regardless of whitespace.
        Pattern TIME_PATTERN =
                Pattern.compile("^\\s*(\\d+)\\s*(\\w+)\\s*$");

        Matcher matcher = TIME_PATTERN.matcher(tickTimeString);

        if (!matcher.matches()) {
            String errorMessage = String.format("Invalid %s entry: %s, use format [duration integer] [time unit string]",
                    key, tickTimeString);
            throw new IllegalArgumentException(errorMessage);
        }

        int duration = Integer.parseInt(matcher.group(1));

        String timeUnitString = matcher.group(2);

        TimeUnit timeUnit = parseTimeUnit(timeUnitString);

        return new TickTime(duration, timeUnit);
    }

    /**
     * Retrieves the Java TimeUnit enum value corresponding to string representations of time measurements.
     * Supports milliseconds, seconds, minutes, hours, and days.
     *
     * @return The TimeUnit object containing the desired time measurement as its unit.
     */
    public TimeUnit parseTimeUnit(String unit) {
        return switch (unit) {
            case "ms", "millisecond", "milliseconds" -> TimeUnit.MILLISECONDS;
            case "s", "second", "seconds"             -> TimeUnit.SECONDS;
            case "m", "minute", "minutes"             -> TimeUnit.MINUTES;
            case "h", "hour", "hours"                 -> TimeUnit.HOURS;
            case "d", "day", "days"                   -> TimeUnit.DAYS;
            default -> throw new IllegalArgumentException("Unsupported time unit: " + unit);
        };
    }

    /**
     * Retrieves the amount of repetitions from config section "repetitions".
     *
     * @return the amount of repetitions, '-1' for infinite repetitions, and '-2' to represent an error flag.
     */
    public int parseRepetitions() {

        String repetitions = getString("repetitions");

        if (!repetitions.matches("^-?\\d+$"))
            throw new IllegalArgumentException("Invalid repetition entry: %s, entry requires an integer");

        return Integer.parseInt(repetitions);
    }
}
