package com.bumpyjake.live;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class LiveHelp implements CommandExecutor, TabCompleter {

    private final Live plugin;

    public LiveHelp(Live plugin){this.plugin = plugin;}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player){

            Player p = (Player) sender;

            if (args.length == 0){
                p.sendMessage(ColorUtils.translateColorCodes("&8=========================="));
                p.sendMessage(ColorUtils.translateColorCodes("&b/live &7- &fGive yourself the live role!"));
                p.sendMessage(ColorUtils.translateColorCodes("&b/liveplugin help &7- &fShow this message!"));
                p.sendMessage(ColorUtils.translateColorCodes("&b/liveplugin reload &7- &fReload the configuration!"));
                p.sendMessage(ColorUtils.translateColorCodes(""));
                p.sendMessage(ColorUtils.translateColorCodes("&fIf you have any issues, contact &bbumpyJake#2004"));
                p.sendMessage(ColorUtils.translateColorCodes("&8=========================="));
            }else if (args.length == 1) {

                if (args[0].equalsIgnoreCase("help")) {
                    p.sendMessage(ColorUtils.translateColorCodes("&8=========================="));
                    p.sendMessage(ColorUtils.translateColorCodes("&b/live &7- &fGive yourself the live role!"));
                    p.sendMessage(ColorUtils.translateColorCodes("&b/liveplugin help &7- &fShow this message!"));
                    p.sendMessage(ColorUtils.translateColorCodes("&b/liveplugin reload &7- &fReload the configuration!"));
                    p.sendMessage(ColorUtils.translateColorCodes(""));
                    p.sendMessage(ColorUtils.translateColorCodes("&fIf you have any issues, contact &bbumpyJake#2004"));
                    p.sendMessage(ColorUtils.translateColorCodes("&8=========================="));
                } else if (args[0].equalsIgnoreCase("reload")) {
                    if (p.hasPermission("liveplugin.reload")) {
                        plugin.reloadConfig();
                        p.sendMessage(ColorUtils.translateColorCodes("&aReloaded configuration"));
                    }
                }else{
                    p.sendMessage(ColorUtils.translateColorCodes("&cThis is not a valid argument"));
                }
            }



        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String commandLable, String[] args) {

        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("help");
            completions.add("reload");
            return StringUtil.copyPartialMatches(args[0], completions, new ArrayList<>());
        }
        return null;
    }

}
