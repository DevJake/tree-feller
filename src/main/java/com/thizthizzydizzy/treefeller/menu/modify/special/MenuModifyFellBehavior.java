package com.thizthizzydizzy.treefeller.menu.modify.special;

import com.thizthizzydizzy.simplegui.Button;
import com.thizthizzydizzy.simplegui.Label;
import com.thizthizzydizzy.simplegui.Menu;
import com.thizthizzydizzy.treefeller.FellBehavior;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;

public class MenuModifyFellBehavior extends Menu {//TODO make multi-page

    public MenuModifyFellBehavior(Menu parent, Plugin plugin, Player player, String name, boolean allowNull,
                                  FellBehavior defaultValue, FellBehavior[] values, Consumer<FellBehavior> setFunc) {
        super(parent, plugin, player, "Modify Fell Behavior (" + name + ")", getSize(values.length, allowNull));
        Label label = add(new Label(0, makeItem(Material.PAPER).setDisplayName(Objects.toString(defaultValue))));
        for (int i = 0; i < values.length; i++) {
            int idx = i;
            add(new Button(i + 1,
                    makeItem(values[i].getItem()).setDisplayName("Set to " + values[i]).addLore(shorten(values[i].getDescription(), 42)), (click) -> {
                if (click != ClickType.LEFT) return;
                setFunc.accept(values[idx]);
                label.label = makeItem(Material.PAPER).setDisplayName(Objects.toString(values[idx])).build();
                updateInventory();
            }));
        }
        if (allowNull) {
            add(new Button(values.length + 1, makeItem(Material.BLACK_CONCRETE).setDisplayName("Set to NULL"),
                    (click) -> {
                if (click != ClickType.LEFT) return;
                setFunc.accept(null);
                label.label = makeItem(Material.PAPER).setDisplayName(Objects.toString(null)).build();
                updateInventory();
            }));
        }
        add(new Button(size - 1, makeItem(Material.BARRIER).setDisplayName("Back"), (click) -> {
            if (click != ClickType.LEFT) return;
            open(parent);
        }));
    }

    private static int getSize(int count, boolean allowNull) {
        int actualCount = count + 2;
        if (allowNull) actualCount++;
        if (actualCount > 54)
            throw new IllegalArgumentException("MenuModifyFellBehavior only supports up to 52 values! (including " +
                    "null)");
        if (actualCount / 9 * 9 == actualCount) return actualCount;
        return actualCount / 9 * 9 + 9;
    }

    private ArrayList<String> shorten(String s, int chars) {
        ArrayList<String> strs = new ArrayList<>();
        strs.add(s);
        return shorten(strs, chars);
    }

    private ArrayList<String> shorten(ArrayList<String> description, int chars) {
        ArrayList<String> output = new ArrayList<>();
        for (String s : description) {
            String[] split = s.split(" ");
            String str = "";
            for (int i = 0; i < split.length; i++) {
                str += " " + split[i];
                if (str.length() > chars) {
                    output.add(str.trim());
                    str = "";
                }
            }
            if (!str.trim().isEmpty()) output.add(str.trim());
        }
        return output;
    }
}