package com.thizthizzydizzy.treefeller;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Material;
public class Tool{
    public final Material material;
    public Tool(Material material){
        this.material = material;
    }
    public void print(Logger logger){
        logger.log(Level.INFO, "Loaded tool: {0}", material);
        for(Option option : Option.options){
            Object value = option.getValue(this);
            if(value!=null){
                logger.log(Level.INFO, "- {0}: {1}", new Object[]{option.name, option.makeReadable(value)});
            }
        }
   }
}