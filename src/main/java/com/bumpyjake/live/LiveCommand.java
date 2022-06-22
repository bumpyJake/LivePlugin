package com.bumpyjake.live;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class LiveCommand implements CommandExecutor {

    private LuckPerms luckperms;
    private final Live plugin;

    public LiveCommand(Live plugin){this.plugin = plugin;}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        this.luckperms = getServer().getServicesManager().load(LuckPerms.class);

        String onMessage = ColorUtils.translateColorCodes(plugin.getConfig().getString("on-message"));
        String offMessage = ColorUtils.translateColorCodes(plugin.getConfig().getString("off-message"));
        String onMessageAll = ColorUtils.translateColorCodes(plugin.getConfig().getString("on-message-all"));
        String onTitle = ColorUtils.translateColorCodes(plugin.getConfig().getString("on-title"));
        String onSubtitle = ColorUtils.translateColorCodes(plugin.getConfig().getString("on-subtitle"));
        String grouplive = ("group." + plugin.getConfig().getString("live-group"));


        if (sender instanceof Player){

            Player p = (Player) sender;
            UUID uuid= p.getUniqueId();
            User user = luckperms.getUserManager().getUser(uuid);
            Node node = Node.builder(grouplive).build();

            Set<String> isLive = p.getScoreboardTags();

            if (isLive.contains("live")){
                p.sendMessage(offMessage);
                DataMutateResult result = user.data().remove(node);
                p.removeScoreboardTag("live");
            }else{
                p.sendMessage(onMessage);
                p.sendTitle(onTitle, onSubtitle, 0,30,5);
                DataMutateResult result = user.data().add(node);
                p.addScoreboardTag("live");
                Location ploc = p.getLocation();
                p.playSound(ploc, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                for (Player online : Bukkit.getOnlinePlayers()) {
                    if ((online.getUniqueId()) != uuid) {
                        onMessageAll = onMessageAll.replace("%player%", p.getDisplayName());
                        online.sendMessage(onMessageAll);
                    }
                }
            }

        }



        return true;
    }
}
