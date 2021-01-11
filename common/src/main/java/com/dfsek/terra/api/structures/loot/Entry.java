package com.dfsek.terra.api.structures.loot;

import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.block.MaterialData;
import com.dfsek.terra.api.platform.inventory.ItemStack;
import com.dfsek.terra.api.structures.loot.functions.AmountFunction;
import com.dfsek.terra.api.structures.loot.functions.DamageFunction;
import com.dfsek.terra.api.structures.loot.functions.EnchantFunction;
import com.dfsek.terra.api.structures.loot.functions.LootFunction;
import com.dfsek.terra.api.util.GlueList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Representation of a single item entry within a Loot Table pool.
 */
public class Entry {
    private final Logger logger;
    private final MaterialData item;
    private final int weight;
    private final List<LootFunction> functions = new GlueList<>();
    private final TerraPlugin main;

    /**
     * Instantiates an Entry from a JSON representation.
     *
     * @param entry The JSON Object to instantiate from.
     */
    public Entry(JSONObject entry, TerraPlugin main) {
        this.main = main;
        this.logger = main.getLogger();
        String id = entry.get("name").toString();
        this.item = main.getWorldHandle().createMaterialData(id);

        int weight1;
        try {
            weight1 = ((Number) entry.get("weight")).intValue();
        } catch(NullPointerException e) {
            weight1 = 1;
        }

        this.weight = weight1;
        if(entry.containsKey("functions")) {
            for(Object function : (JSONArray) entry.get("functions")) {
                switch(((String) ((JSONObject) function).get("function"))) {
                    case "minecraft:apply_bonus":
                    case "apply_bonus":
                        logger.warning("There is no implementation for the \"minecraft:apply_bonus\" function.");
                        break;
                    case "minecraft:copy_name":
                    case "copy_name":
                        logger.warning("There is no implementation for the \"minecraft:copy_name\" function.");
                        break;
                    case "minecraft:copy_nbt":
                    case "copy_nbt":
                        logger.warning("There is no implementation for the \"minecraft:copy_nbt\" function.");
                        break;
                    case "minecraft:copy_state":
                    case "copy_state":
                        logger.warning("There is no implementation for the \"minecraft:copy_state\" function.");
                        break;
                    case "minecraft:enchant_randomly":
                    case "enchant_randomly":
                        break;
                    case "minecraft:enchant_with_levels":
                    case "enchant_with_levels":
                        Number maxEnchant = (Number) ((JSONObject) ((JSONObject) function).get("levels")).get("max");
                        Number minEnchant = (Number) ((JSONObject) ((JSONObject) function).get("levels")).get("min");
                        JSONArray disabled = null;
                        if(((JSONObject) function).containsKey("disabled_enchants"))
                            disabled = (JSONArray) ((JSONObject) function).get("disabled_enchants");
                        functions.add(new EnchantFunction(minEnchant.intValue(), maxEnchant.intValue(), disabled, main));
                        break;
                    case "minecraft:exploration_map":
                    case "exploration_map":
                        break;
                    case "minecraft:explosion_decay":
                    case "explosion_decay":
                        break;
                    case "minecraft:furnace_smelt":
                    case "furnace_smelt":
                        break;
                    case "minecraft:fill_player_head":
                    case "fill_player_head":
                        break;
                    case "minecraft:limit_count":
                    case "limit_count":
                        break;
                    case "minecraft:looting_enchant":
                    case "looting_enchant":
                        break;
                    case "minecraft:set_attributes":
                    case "set_attributes":
                        break;
                    // TODO: 2021-01-09 maybe add this function when 1.17 comes out. Or just warn & ignore like the rest lmao.
/*
                    case "minecraft:set_banner_pattern":
                    case "set_banner_pattern":
                        break;
*/
                    case "minecraft:set_contents":
                    case "set_contents":
                        break;
                    case "minecraft:set_count":
                    case "set_count":
                        Object loot = ((JSONObject) function).get("count");
                        Number max, min;
                        if(loot instanceof Long) {
                            max = (Long) loot;
                            min = (Long) loot;
                        } else {
                            max = (Number) ((JSONObject) loot).get("max");
                            min = (Number) ((JSONObject) loot).get("min");
                        }
                        functions.add(new AmountFunction(min.intValue(), max.intValue()));
                        break;
                    case "minecraft:set_damage":
                    case "set_damage":
                        Number maxDamage = (Number) ((JSONObject) ((JSONObject) function).get("damage")).get("max");
                        Number minDamage = (Number) ((JSONObject) ((JSONObject) function).get("damage")).get("min");
                        functions.add(new DamageFunction(minDamage.intValue(), maxDamage.intValue()));
                        break;
                    // TODO: 2021-01-09 maybe add this function when 1.17 comes out. Or just warn & ignore like the rest
/*
                    case "minecraft:set_enchantments":
                    case "set_enchantments":
                        break;
*/
                    case "minecraft:set_loot_table":
                    case "set_loot_table":
                        // just set the "LootTable" nbt tag based on the "name" value.
                        // set the "LootTableSeed" nbt tag based on the "seed" value.
                        break;
                    case "minecraft:set_lore":
                    case "set_lore":
                        // set the "display:Lore" nbt tag based on the "lore" value. ("lore" values is a JSON list & "Lore" tag is also a list.)
                        // if the "replace" values is true, replace  the lore. If false, append to the item lore.
                        // the "entity" value specifies which entity to affect.
                        break;
                    case "minecraft:set_name":
                    case "set_name":
                        // set the "display:Name" nbt tag based on the "name" value.
                        // the "entity" value specifies which entity to affect.
                        break;
                    case "minecraft:set_nbt":
                    case "set_nbt":
                        // Adds the nbt in the "tag" value to the nbt.
                        break;
                    case "minecraft:set_stew_effect":
                    case "set_stew_effect":
                        logger.warning("There is no implementation for the \"minecraft:set_stew_effect\" function.");
                        break;
                    default:
                        logger.warning(() -> String.format("Could not find implementation for the unknown function \"%s\"", ((JSONObject) function).get("function")));
                }
            }
        }
    }

    /**
     * Fetches a single ItemStack from the Entry, applying all functions to it.
     *
     * @param r The Random instance to apply functions with
     * @return ItemStack - The ItemStack with all functions applied.
     */
    public ItemStack getItem(Random r) {
        ItemStack item = main.getItemHandle().newItemStack(this.item, 1);
        for(LootFunction f : functions) {
            item = f.apply(item, r);
        }
        return item;
    }

    /**
     * Gets the weight attribute of the Entry.
     *
     * @return long - The weight of the Entry.
     */
    public int getWeight() {
        return this.weight;
    }
}
