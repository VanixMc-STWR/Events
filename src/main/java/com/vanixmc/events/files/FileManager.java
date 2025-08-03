package com.vanixmc.events.files;

import com.vanixmc.events.EventsPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class FileManager {
    private final Map<String, FileConfiguration> loadedConfigs;
    private final File directory;

    public FileManager(String subDirectory) {
        this.loadedConfigs = new HashMap<>();
        this.directory = new File(EventsPlugin.getInstance().getDataFolder(), subDirectory);
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IllegalStateException("Failed to create directory: " + directory.getPath());
        }
    }

    public FileConfiguration load(String name) {
        if (loadedConfigs.containsKey(name)) {
            return loadedConfigs.get(name);
        }

        File file = new File(directory, name + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        loadedConfigs.put(name, config);
        return config;
    }

    public void save(String name) {
        FileConfiguration config = loadedConfigs.get(name);
        if (config == null) return;

        File file = new File(directory, name + ".yml");
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save config: " + name, e);
        }
    }

    public void saveAll() {
        for (String name : loadedConfigs.keySet()) {
            save(name);
        }
    }

    public void reloadAll() {
        loadedConfigs.clear();
        for (String fileName : listFileNames()) {
            load(fileName);
        }
    }

    public Set<String> listFileNames() {
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files == null) return Collections.emptySet();

        return Arrays.stream(files)
                .map(file -> file.getName().replace(".yml", ""))
                .collect(Collectors.toSet());
    }

    public FileConfiguration getConfig(String name) {
        return loadedConfigs.get(name);
    }

    public boolean exists(String name) {
        return new File(directory, name + ".yml").exists();
    }

    public void delete(String name) {
        File file = new File(directory, name + ".yml");
        if (file.exists() && file.delete()) {
            loadedConfigs.remove(name);
        }
    }
}
