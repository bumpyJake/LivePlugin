package com.bumpyjake.live;

import org.bukkit.plugin.java.JavaPlugin;

public final class Live extends JavaPlugin {

    @Override
    public void onEnable() {

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        getCommand("live").setExecutor(new LiveCommand(this));
        getCommand("liveplugin").setExecutor(new LiveHelp(this));
        getCommand("recording").setExecutor(new RecCommand(this));

    }




}
