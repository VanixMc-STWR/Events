package com.vanixmc.events.files;

import com.vanixmc.events.EventsPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

    public Map<String, Object> load(String name) {
        File file = new File(directory, name + ".yml");
        try (InputStream inputStream = new FileInputStream(file)) {
            Yaml yml = new Yaml();
            return yml.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
