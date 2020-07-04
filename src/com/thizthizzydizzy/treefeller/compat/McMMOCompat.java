package com.thizthizzydizzy.treefeller.compat;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
public class McMMOCompat extends InternalCompatibility{
    @Override
    public String getPluginName(){
        return "mcMMO";
    }
    @Override
    public void breakBlock(Player player, Block block){
        if(player==null)return;
        com.gmail.nossr50.api.ExperienceAPI.addXpFromBlock(block.getState(), com.gmail.nossr50.util.player.UserManager.getPlayer(player));
    }
}