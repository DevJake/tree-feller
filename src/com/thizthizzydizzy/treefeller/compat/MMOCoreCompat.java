package com.thizthizzydizzy.treefeller.compat;
import com.thizthizzydizzy.simplegui.ItemBuilder;
import com.thizthizzydizzy.treefeller.Modifier;
import com.thizthizzydizzy.treefeller.Option;
import com.thizthizzydizzy.treefeller.Tool;
import com.thizthizzydizzy.treefeller.Tree;
import com.thizthizzydizzy.treefeller.DebugResult;
import com.thizthizzydizzy.treefeller.TreeFeller;
import com.thizthizzydizzy.treefeller.menu.MenuGlobalConfiguration;
import com.thizthizzydizzy.treefeller.menu.MenuToolConfiguration;
import com.thizthizzydizzy.treefeller.menu.MenuTreeConfiguration;
import com.thizthizzydizzy.treefeller.menu.modify.MenuModifyStringDoubleMap;
import com.thizthizzydizzy.treefeller.menu.modify.MenuModifyStringIntegerMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.block.BlockInfo;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static com.thizthizzydizzy.treefeller.DebugResult.Type.GLOBAL;
import static com.thizthizzydizzy.treefeller.DebugResult.Type.SUCCESS;

public class MMOCoreCompat extends InternalCompatibility{
    private static boolean installed;
    public static Option<HashMap<String, Double>> MMOCORE_TRUNK_XP = new Option<HashMap<String, Double>>("MMOCore Trunk XP", true, false, true, new HashMap<>(), "\n   - global: 1"){
        @Override
        public String getDesc(boolean ingame){
            return "EXP will be provided to these professions when a tree is felled\n"
                    + "EXP is provided per-block (a value of 1 means 1 EXP per block of trunk)\n"
                    + "use \"global\" to add global experience"+(ingame?"":("\n"
                    + "ex:\n"
                    + "- global: 3\n"
                    + "- woodcutting: 8"));
        }
        @Override
        public HashMap<String, Double> load(Object o){
            if(o instanceof MemorySection){
                HashMap<String, Double> professions = new HashMap<>();
                MemorySection m = (MemorySection)o;
                for(String key : m.getKeys(false)){
                    String profession = key;
                    if(profession==null)continue;
                    Double xp = Option.loadDouble(m.get(key));
                    if(xp==null)continue;
                    if(professions.containsKey(profession)){
                        professions.put(profession, professions.get(profession)+xp);
                    }else{
                        professions.put(profession, xp);
                    }
                }
                return professions;
            }
            if(o instanceof Map){
                HashMap<String, Double> professions = new HashMap<>();
                Map m = (Map)o;
                for(Object obj : m.keySet()){
                    String profession = null;
                    if(obj instanceof String){
                        profession = (String)obj;
                    }
                    if(profession==null)continue;
                    Double xp = Option.loadDouble(m.get(obj));
                    if(xp==null)continue;
                    if(professions.containsKey(profession)){
                        professions.put(profession, professions.get(profession)+xp);
                    }else{
                        professions.put(profession, xp);
                    }
                }
                return professions;
            }
            if(o instanceof List){
                List l = (List)o;
                HashMap<String, Double> professions = new HashMap<>();
                for(Object lbj : l){
                    if(lbj instanceof Map){
                        Map m = (Map)lbj;
                        for(Object obj : m.keySet()){
                            String profession = null;
                            if(obj instanceof String){
                                profession = (String)obj;
                            }
                            if(profession==null)continue;
                            Double xp = Option.loadDouble(m.get(obj));
                            if(xp==null)continue;
                            if(professions.containsKey(profession)){
                                professions.put(profession, professions.get(profession)+xp);
                            }else{
                                professions.put(profession, xp);
                            }
                        }
                    }
                }
                return professions;
            }
            return null;
        }
        @Override
        public ItemBuilder getConfigurationDisplayItem(HashMap<String, Double> value){
            return new ItemBuilder(Material.OAK_LOG);
        }
        @Override
        public void openGlobalModifyMenu(MenuGlobalConfiguration parent){
            parent.open(new MenuModifyStringDoubleMap(parent, parent.plugin, parent.player, name, 0, Double.MAX_VALUE, false, false, globalValue, (value) -> {
                globalValue = value;
            }));
        }
        @Override
        public void openToolModifyMenu(MenuToolConfiguration parent, Tool tool){
            parent.open(new MenuModifyStringDoubleMap(parent, parent.plugin, parent.player, name, 0, Double.MAX_VALUE, true, false, toolValues.get(tool), (value) -> {
                if(value==null)toolValues.remove(tool);
                else toolValues.put(tool, value);
            }));
        }
        @Override
        public void openTreeModifyMenu(MenuTreeConfiguration parent, Tree tree){
            parent.open(new MenuModifyStringDoubleMap(parent, parent.plugin, parent.player, name, 0, Double.MAX_VALUE, true, false, treeValues.get(tree), (value) -> {
                if(value==null)treeValues.remove(tree);
                else treeValues.put(tree, value);
            }));
        }
        @Override
        public String writeToConfig(HashMap<String, Double> value){
            if(value==null)return "";
            String s = "{";
            String str = "";
            for(String st : value.keySet()){
                str+=", "+st+": "+value.get(st);
            }
            if(!str.isEmpty())s+=str.substring(2);
            return s+"}";
        }
    };
    public static Option<HashMap<String, Double>> MMOCORE_LEAVES_XP = new Option<HashMap<String, Double>>("MMOCore Leaves XP", true, false, true, new HashMap<>(), "\n   - global: 0"){
        @Override
        public String getDesc(boolean ingame){
            return "EXP will be provided to these professions when a tree is felled\n"
                    + "EXP is provided per-block (a value of 1 means 1 EXP per block of leaves)\n"
                    + "use \"global\" to add global experience"+(ingame?"":("\n"
                    + "ex:\n"
                    + "- global: 3\n"
                    + "- woodcutting: 8"));
        }
        @Override
        public HashMap<String, Double> load(Object o){
            if(o instanceof MemorySection){
                HashMap<String, Double> professions = new HashMap<>();
                MemorySection m = (MemorySection)o;
                for(String key : m.getKeys(false)){
                    String profession = key;
                    if(profession==null)continue;
                    Double xp = Option.loadDouble(m.get(key));
                    if(xp==null)continue;
                    if(professions.containsKey(profession)){
                        professions.put(profession, professions.get(profession)+xp);
                    }else{
                        professions.put(profession, xp);
                    }
                }
                return professions;
            }
            if(o instanceof Map){
                HashMap<String, Double> professions = new HashMap<>();
                Map m = (Map)o;
                for(Object obj : m.keySet()){
                    String profession = null;
                    if(obj instanceof String){
                        profession = (String)obj;
                    }
                    if(profession==null)continue;
                    Double xp = Option.loadDouble(m.get(obj));
                    if(xp==null)continue;
                    if(professions.containsKey(profession)){
                        professions.put(profession, professions.get(profession)+xp);
                    }else{
                        professions.put(profession, xp);
                    }
                }
                return professions;
            }
            if(o instanceof List){
                List l = (List)o;
                HashMap<String, Double> professions = new HashMap<>();
                for(Object lbj : l){
                    if(lbj instanceof Map){
                        Map m = (Map)lbj;
                        for(Object obj : m.keySet()){
                            String profession = null;
                            if(obj instanceof String){
                                profession = (String)obj;
                            }
                            if(profession==null)continue;
                            Double xp = Option.loadDouble(m.get(obj));
                            if(xp==null)continue;
                            if(professions.containsKey(profession)){
                                professions.put(profession, professions.get(profession)+xp);
                            }else{
                                professions.put(profession, xp);
                            }
                        }
                    }
                }
                return professions;
            }
            return null;
        }
        @Override
        public ItemBuilder getConfigurationDisplayItem(HashMap<String, Double> value){
            return new ItemBuilder(Material.OAK_LEAVES);
        }
        @Override
        public void openGlobalModifyMenu(MenuGlobalConfiguration parent){
            parent.open(new MenuModifyStringDoubleMap(parent, parent.plugin, parent.player, name, 0, Double.MAX_VALUE, false, false, globalValue, (value) -> {
                globalValue = value;
            }));
        }
        @Override
        public void openToolModifyMenu(MenuToolConfiguration parent, Tool tool){
            parent.open(new MenuModifyStringDoubleMap(parent, parent.plugin, parent.player, name, 0, Double.MAX_VALUE, true, false, toolValues.get(tool), (value) -> {
                if(value==null)toolValues.remove(tool);
                else toolValues.put(tool, value);
            }));
        }
        @Override
        public void openTreeModifyMenu(MenuTreeConfiguration parent, Tree tree){
            parent.open(new MenuModifyStringDoubleMap(parent, parent.plugin, parent.player, name, 0, Double.MAX_VALUE, true, false, treeValues.get(tree), (value) -> {
                if(value==null)treeValues.remove(tree);
                else treeValues.put(tree, value);
            }));
        }
        @Override
        public String writeToConfig(HashMap<String, Double> value){
            if(value==null)return "";
            String s = "{";
            String str = "";
            for(String st : value.keySet()){
                str+=", "+st+": "+value.get(st);
            }
            if(!str.isEmpty())s+=str.substring(2);
            return s+"}";
        }
    };
    
    public static Option<HashMap<String, Integer>> MMOCORE_REQUIRED_PROFESSION_LEVEL = new Option<HashMap<String, Integer>>("MMOCore Required Profession Level", true, false, true, new HashMap<>(), "\n   - woodcutting: 1"){
        @Override
        public String getDesc(boolean ingame){
            return "These professions levels will be checked before a tree is felled";
        }
        @Override
        public HashMap<String, Integer> load(Object o){
            if(o instanceof MemorySection){
                HashMap<String, Integer> professions = new HashMap<>();
                MemorySection m = (MemorySection)o;
                for(String key : m.getKeys(false)){
                    String profession = key;
                    if(profession==null)continue;
                    Integer lvlreq = Option.loadInt(m.get(key));
                    if(lvlreq==null)continue;
                    if(professions.containsKey(profession)){
                        professions.put(profession, professions.get(profession)+lvlreq);
                    }else{
                        professions.put(profession, lvlreq);
                    }
                }
                return professions;
            }
            if(o instanceof Map){
                HashMap<String, Integer> professions = new HashMap<>();
                Map m = (Map)o;
                for(Object obj : m.keySet()){
                    String profession = null;
                    if(obj instanceof String){
                        profession = (String)obj;
                    }
                    if(profession==null)continue;
                    Integer lvlreq = Option.loadInt(m.get(obj));
                    if(lvlreq==null)continue;
                    if(professions.containsKey(profession)){
                        professions.put(profession, professions.get(profession)+lvlreq);
                    }else{
                        professions.put(profession, lvlreq);
                    }
                }
                return professions;
            }
            if(o instanceof List){
                List l = (List)o;
                HashMap<String, Integer> professions = new HashMap<>();
                for(Object lbj : l){
                    if(lbj instanceof Map){
                        Map m = (Map)lbj;
                        for(Object obj : m.keySet()){
                            String profession = null;
                            if(obj instanceof String){
                                profession = (String)obj;
                            }
                            if(profession==null)continue;
                            Integer lvlreq = Option.loadInt(m.get(obj));
                            if(lvlreq==null)continue;
                            if(professions.containsKey(profession)){
                                professions.put(profession, professions.get(profession)+lvlreq);
                            }else{
                                professions.put(profession, lvlreq);
                            }
                        }
                    }
                }
                return professions;
            }
            return null;
        }
        @Override
        public ItemBuilder getConfigurationDisplayItem(HashMap<String, Integer> value){
            return new ItemBuilder(Material.GOLDEN_PICKAXE);
        }
        @Override
        public void openGlobalModifyMenu(MenuGlobalConfiguration parent){
            parent.open(new MenuModifyStringIntegerMap(parent, parent.plugin, parent.player, name, 0, Integer.MAX_VALUE, false, false, globalValue, ( value) -> {
                globalValue = value;
            }));
        }
        @Override
        public void openToolModifyMenu(MenuToolConfiguration parent, Tool tool){
            parent.open(new MenuModifyStringIntegerMap(parent, parent.plugin, parent.player, name, 0, Integer.MAX_VALUE, true, false, toolValues.get(tool), (value) -> {
                if(value==null)toolValues.remove(tool);
                else toolValues.put(tool, value);
            }));
        }
        @Override
        public void openTreeModifyMenu(MenuTreeConfiguration parent, Tree tree){
            parent.open(new MenuModifyStringIntegerMap(parent, parent.plugin, parent.player, name, 0, Integer.MAX_VALUE, true, false, treeValues.get(tree), (value) -> {
                if(value==null)treeValues.remove(tree);
                else treeValues.put(tree, value);
            }));
        }
        @Override
        public String writeToConfig(HashMap<String, Integer> value){
            if(value==null)return "";
            String s = "{";
            String str = "";
            for(String st : value.keySet()){
                str+=", "+st+": "+value.get(st);
            }
            if(!str.isEmpty())s+=str.substring(2);
            return s+"}";
        }
        @Override
        protected DebugResult doCheck( TreeFeller plugin, Tool tool, Tree tree, Block block, Player player, ItemStack axe ){
            if(!installed)return null;
            //System.out.println( "[SOP] hello from mmocore compat OPTION" );
    
    
            HashMap<String, Integer> lvlReqs = this.get( tool, tree );
    
            if( lvlReqs == null ){
                //System.out.println( "[SOP] lvlreq null, returning" );
                return null;
            }
            net.Indyuce.mmocore.api.player.PlayerData data = net.Indyuce.mmocore.api.player.PlayerData.get( player );
    
            boolean allowFell = true;
            for( String profession : lvlReqs.keySet( ) ){
                int lvlReq = lvlReqs.get( profession );
                int playerLevel;
    
                if( profession.equals( "global" ) ){
                    playerLevel = data.getLevel( );
                } else {
                    playerLevel = data.getCollectionSkills( ).getLevel( profession );
                }
    
                if( lvlReq > playerLevel ){
                    //System.out.println( "[SOP] You need at least level " + lvlReq + " " + profession + " to chop down this tree" );
                    player.sendMessage( "You need at least level " + lvlReq + " " + profession + " to chop down this tree" );
                    
                    allowFell = false;
                }
    
            }
            if( allowFell ){
                return new DebugResult( this, SUCCESS );
            } else {
                return new DebugResult( this, GLOBAL, 0, globalValue );
            }
            
        }
        
    };
    
    @Override
    public String getPluginName(){
        return "MMOCore";
    }

    @Override
    public void reload() {
        installed = isInstalled();
    }
    
    @Override
    public void breakBlock(Tree tree, Tool tool, Player player, ItemStack axe, Block block, List<Modifier> modifiers){
        if(player==null)return;
        HashMap<String, Double> xp = null;
        if(tree.trunk.contains(block.getType())){
            xp = MMOCORE_TRUNK_XP.get(tool, tree);
        }else if(tree.leaves.contains(block.getType())){
            xp = MMOCORE_LEAVES_XP.get(tool, tree);
        }
        if(xp==null||xp.isEmpty())return;
        net.Indyuce.mmocore.api.player.PlayerData data = net.Indyuce.mmocore.api.player.PlayerData.get(player);
        //player.sendMessage(data.getCollectionSkills().getClass().getName());
        //player.sendMessage(net.Indyuce.mmocore.MMOCore.plugin.professionManager.getClass().getName());
        if(xp.containsKey("global")){
            data.giveExperience(convert(xp.get("global")), net.Indyuce.mmocore.experience.EXPSource.SOURCE);
        }
        for(String profession : xp.keySet()){
            int exp = convert(xp.get(profession));
            if(profession.equals("global")){
                data.giveExperience(exp, net.Indyuce.mmocore.experience.EXPSource.SOURCE);
            }else{
                data.getCollectionSkills().giveExperience(net.Indyuce.mmocore.MMOCore.plugin.professionManager.get(profession), exp, net.Indyuce.mmocore.experience.EXPSource.SOURCE);
            }
        }
        
        //MMOCore's Regen (see MMOCore/professions/mining.yml)
        //MMOCore's "temp-block" option must not be set
        BlockInfo info = MMOCore.plugin.mineManager.getInfo( block );
        String savedData = block.getBlockData( ).getAsString( );
        if( info == null ){
            //System.out.println( "[SOP] mmocore blockinfo null" );
        } else if( info.hasRegen()){
            //System.out.println( "[SOP] attempting to schedule mmocore regen: " + savedData );
            Bukkit.getScheduler().runTaskLater( MMOCore.plugin, () ->
                    MMOCore.plugin.mineManager.initialize(
                            info.startRegeneration( Bukkit.createBlockData( savedData ), block.getLocation( ) ),
                            true ), 1
            );
        }
    }
    
    private int convert(double d){
        int i = (int)d;
        double remainder = d-i;
        if(new Random().nextDouble()<remainder)i++;
        return i;
    }
}
