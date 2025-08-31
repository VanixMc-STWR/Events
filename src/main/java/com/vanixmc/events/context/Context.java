package com.vanixmc.events.context;

import com.vanixmc.events.event.domain.Event;
import com.vanixmc.events.trigger.domain.Triggerable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Getter
@Setter
@Builder
@ToString
public class Context {
    private Entity entity;
    private Player player;
    private Event event;
    private Location location;
    private Triggerable triggerable;
    private org.bukkit.event.Event bukkitEvent;
    private final PersistentData persistentData = new PersistentData();

    public static class PersistentData {
        private static final Pattern VAR_PATTERN = Pattern.compile("\\$\\{([\\w-]+)}");
        private final Map<String, Object> contextMap;

        public PersistentData() {
            this.contextMap = new HashMap<>();
        }

        public void addContext(String key, Object value) {
            if (contextMap.containsKey(key)) {
                contextMap.replace(key, value);
                return;
            }
            contextMap.put(key, value);
        }

        public Object removeContext(String key) {
            return contextMap.remove(key);
        }

        public Optional<Object> getContext(String key) {
            return Optional.ofNullable(contextMap.get(key));
        }

        public String replaceVariables(String input) {
            Matcher matcher = VAR_PATTERN.matcher(input);
            StringBuilder result = new StringBuilder();

            while (matcher.find()) {
                String key = matcher.group(1);
                Optional<Object> value = getContext(key);
                matcher.appendReplacement(result, Matcher.quoteReplacement(value.isPresent() ? value.get().toString() : ""));
            }

            matcher.appendTail(result);
            return result.toString();
        }

    }
}
