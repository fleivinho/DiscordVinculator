package com.github.wypixel.DiscordVinculator.spigot.config.json;

import java.io.File;

public interface JSONConfiguration {
    /**
     * Loads a JSON Configuration from a file
     * 
     * @param f
     * @return 
     */
    @Deprecated
    public JSONConfig loadJSONConfig(File f) throws FileNotJSONException;
    
}
