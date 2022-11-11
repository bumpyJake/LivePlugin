package com.bumpyjake.live;

import com.bumpyjake.live.utils.ColorUtils;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class RecCommand implements CommandExecutor {

    private LuckPerms luckperms;
    private final Live plugin;

    public RecCommand(Live plugin){this.plugin = plugin;}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        this.luckperms = getServer().getServicesManager().load(LuckPerms.class);

        String onMessage = ColorUtils.translateColorCodes(plugin.getConfig().getString("rec-on-message"));
        String offMessage = ColorUtils.translateColorCodes(plugin.getConfig().getString("rec-off-message"));
        String onMessageAll = ColorUtils.translateColorCodes(plugin.getConfig().getString("rec-on-message-all"));
        String onTitle = ColorUtils.translateColorCodes(plugin.getConfig().getString("rec-on-title"));
        String onSubtitle = ColorUtils.translateColorCodes(plugin.getConfig().getString("rec-on-subtitle"));
        String groupLive = ("group." + plugin.getConfig().getString("live-group"));
        String groupRec = ("group." + plugin.getConfig().getString("rec-group"));


        if (sender instanceof Player){

            Player p = (Player) sender;
            UUID uuid= p.getUniqueId();
            User user = luckperms.getUserManager().getUser(uuid);
            Node liveNode = Node.builder(groupLive).build();
            Node recNode = Node.builder(groupRec).build();

            Set<String> tags = p.getScoreboardTags();

            if (tags.contains("rec")){
                p.sendMessage(offMessage);
                DataMutateResult result = user.data().remove(recNode);
                luckperms.getUserManager().saveUser(user);
                p.removeScoreboardTag("rec");
            }else{
                if (tags.contains("live")) {
                    DataMutateResult result = user.data().remove(liveNode);
                    p.removeScoreboardTag("live");
                }
                p.sendMessage(onMessage);
                p.sendTitle(onTitle, onSubtitle, 0,30,5);
                DataMutateResult result = user.data().add(recNode);
                luckperms.getUserManager().saveUser(user);
                p.addScoreboardTag("rec");
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
