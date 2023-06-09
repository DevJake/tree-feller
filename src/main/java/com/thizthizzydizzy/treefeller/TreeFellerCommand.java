package com.thizthizzydizzy.treefeller;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public abstract class TreeFellerCommand {
    public final String command;
    private final String permission;
    private final TreeFellerCommand[] subcommands;

    public TreeFellerCommand(String command, TreeFellerCommand... subcommands) {
        this(command, command, subcommands);
    }

    public TreeFellerCommand(String command, String permission, TreeFellerCommand... subcommands) {
        if (permission != null && permission.isEmpty()) permission = null;
        if (permission != null && !permission.startsWith("treefeller.")) permission = "treefeller." + permission;
        this.command = command;
        this.permission = permission;
        this.subcommands = subcommands;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args, String[] fullArgs) {
        if (!hasPermission(sender)) {
            sender.sendMessage(ChatColor.RED + "Unknown Command");
            return true;
        }
        if (args.length < 1) return run(sender, command, label, fullArgs);
        for (TreeFellerCommand cmd : subcommands) {
            if (args[0].equals(cmd.command)) {
                return cmd.onCommand(sender, command, label, trim(args, 1), fullArgs);
            }
        }
        sender.sendMessage("Usage: /treefeller " + getFullUsage());
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> strs = new ArrayList<>();
        if (!hasPermission(sender)) return strs;
        if (args.length == 1) {
            for (TreeFellerCommand cmd : subcommands) {
                if (cmd.command.substring(0, cmd.command.length() - 1).startsWith(args[0]) && cmd.hasPermission(sender))
                    strs.add(cmd.command);
            }
        }
        if (args.length > 1) {
            for (TreeFellerCommand cmd : subcommands) {
                if (args[0].equals(cmd.command)) return cmd.onTabComplete(sender, command, label, trim(args, 1));
            }
        }
        return strs;
    }

    protected abstract boolean run(CommandSender sender, Command command, String label, String[] args);

    public String[] trim(String[] data, int beginning) {
        if (data == null) return null;
        String[] newData = new String[Math.max(0, data.length - beginning)];
        System.arraycopy(data, 0 + beginning, newData, 0, newData.length);
        return newData;
    }

    public boolean hasPermission(CommandSender sender) {
        if (permission == null) return true;
        return sender.hasPermission(permission);
    }

    protected String getUsage() {
        return command;
    }

    protected String getFullUsage() {
        String usage = command;
        if (subcommands.length > 0) {
            String subUsage = "";
            for (TreeFellerCommand cmd : subcommands) {
                subUsage += "|" + cmd.getFullUsage();
            }
            usage += " [" + subUsage.substring(1) + "]";
        }
        return usage;
    }
}