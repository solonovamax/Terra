package com.dfsek.terra.command;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.command.framework.annotations.Command;
import com.dfsek.terra.command.framework.annotations.SubCommandContainer;
import com.dfsek.terra.command.framework.annotations.param.Argument;
import com.dfsek.terra.command.framework.annotations.param.Switch;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SubCommandContainer(command = "structure", commandAliases = {"s"})
public class StructureSubCommands {

    @Command(name = "spawn", aliases = {"sp"}, description = "Checks the spawn conditions at your location.")
    public boolean spawn(TerraPlugin plugin) {
        Location location = null;
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        // TODO: 2020-12-22 implement this
/*
        boolean isAir = StructureSpawnRequirement.AIR.getInstance(world, (TerraBukkitPlugin) getMain()).matches(x, y, z);
        boolean isGround = StructureSpawnRequirement.LAND.getInstance(world, (TerraBukkitPlugin) getMain()).matches(x, y, z);
        boolean isSea = StructureSpawnRequirement.OCEAN.getInstance(world, (TerraBukkitPlugin) getMain()).matches(x, y, z);

        sender.sendMessage("AIR: " + isAir + "\nLAND: " + isGround + "\nOCEAN: " + isSea);
*/
        return true;
    }

    @Command(name = "locate", aliases = {"l"}, description = "Locates a structure.")
    public boolean locate(TerraPlugin plugin,
                          @Argument(name = "structure", description = "The name of the structure you want to locate.")
                                  String structureName,
                          @Argument(name = "radius", description = "The radius in which to search.", suggestions = {"1000", "2500", "5000", "10000"}, defaultValue = "-1")
                                  int radius) {
        // TODO: 2020-12-22 implement this
        // TODO: 2020-12-22 handle radius of -1.
/*
        TerraStructure s;
        try {
            s = Objects.requireNonNull(((TerraBukkitPlugin) plugin).getWorld(world).getConfig().getStructure(structureName));
        } catch(IllegalArgumentException | NullPointerException e) {
            LangUtil.send("command.structure.invalid", sender, structureName);
            return true;
        }
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new AsyncStructureFinder(((TerraBukkitPlugin) plugin).getWorld(world).getGrid(), s, sender.getLocation(), 0, radius, (location) -> {
            if(sender.isOnline()) {
                if(location != null) {
                    ComponentBuilder cm = new ComponentBuilder(String.format("The nearest %s is at ", structureName.toLowerCase()))
                            .append(String.format("[%d, ~, %d]", location.getBlockX(), location.getBlockZ()), ComponentBuilder.FormatRetention.NONE)
                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/minecraft:tp %s %d.0 %.2f %d.0", sender.getName(), location.getBlockX(), sender.getLocation().getY(), location.getBlockZ())))
                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[] {new TextComponent("Click to teleport")}))
                            .color(ChatColor.GREEN)
                            .append(String.format(" (%.1f blocks away)", location.add(new Vector(0, sender.getLocation().getY(), 0)).distance(sender.getLocation().toVector())), ComponentBuilder.FormatRetention.NONE);
                    sender.spigot().sendMessage(cm.create());
                } else
                    sender.sendMessage("Unable to locate structure. ");
            }
        }, (TerraBukkitPlugin) plugin));
*/

        return true;
    }

    @Command(name = "export", aliases = {"e"}, description = "Exports a structure.")
    public boolean export(TerraPlugin plugin,
                          @Argument(name = "structure", description = "The name of the structure you want to export.")
                                  String structureName) {
        // TODO: 2020-12-22 implementation
/*
        Location[] l = WorldEditUtil.getSelectionLocations(sender);
        if(l == null)
            return true;

        Location l1 = l[0];
        Location l2 = l[1];
        Structure structure;
        try {
            structure = new Structure(l1, l2, structureName);
        } catch(InitializationException e) {
            sender.sendMessage(e.getMessage());
            return true;
        }
        try {
            File file = new File(getMain().getDataFolder() + File.separator + "export" + File.separator + "structures", structureName + ".tstructure");
            //noinspection ResultOfMethodCallIgnored
            file.getParentFile().mkdirs();
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();
            structure.save(file);
            LangUtil.send("command.structure.export", sender, file.getAbsolutePath());
        } catch(IOException e) {
            e.printStackTrace();
        }
*/
        return true;
    }

    @Command(name = "load", aliases = {"l"}, description = "Loads a structure.")
    public boolean load(TerraPlugin plugin,
                        @Switch(possibleValues = {"full", "raw"})
                        @Argument(name = "structure", description = "The name of the structure you want to load")
                                String loadType) {
        switch(loadType) {
            case "full":
                return loadFullStructure();
            case "raw":
                return loadRawStructure();
            default:
                throw new IllegalStateException("loadType is annotated with @Switch, so it should always be one of those two values, as that is the annotation contract.");
        }
    }

    private boolean loadFullStructure() {
/*
        try {
            Rotation r;
            try {
                r = Rotation.fromDegrees(Integer.parseInt(args[1]));
            } catch(NumberFormatException e) {
                LangUtil.send("command.structure.invalid-rotation", sender, args[1]);
                return true;
            }
            Structure struc = Structure.load(new File(getMain().getDataFolder() + File.separator + "export" + File.separator + "structures", args[0] + ".tstructure"));
            if(chunk) struc.paste(sender.getLocation(), sender.getLocation().getChunk(), r, (TerraBukkitPlugin) getMain());
            else struc.paste(sender.getLocation(), r, (TerraBukkitPlugin) getMain());
            //sender.sendMessage(String.valueOf(struc.checkSpawns(sender.getLocation(), r)));
        } catch(IOException e) {
            e.printStackTrace();
            LangUtil.send("command.structure.invalid", sender, args[0]);
        }
*/

        return true;
    }

    private boolean loadRawStructure() {
/*
        try {
            Parser parser = new Parser(IOUtils.toString(new FileInputStream(new File(getMain().getDataFolder(), "test.tesf"))));
            Block main = parser.parse();
            main.apply(new Location(new BukkitWorld(sender.getWorld()), sender.getLocation().getX(), sender.getLocation().getY(), sender.getLocation().getZ()));

        } catch(IOException | ParseException e) {
            e.printStackTrace();
        }
        try {
            WorldHandle handle = ((TerraBukkitPlugin) getMain()).getWorldHandle();
            Structure struc = Structure.load(new File(getMain().getDataFolder() + File.separator + "export" + File.separator + "structures", args[0] + ".tstructure"));
            StructureInfo info = struc.getStructureInfo();
            int centerX = info.getCenterX();
            int centerZ = info.getCenterZ();
            for(StructureContainedBlock[][] level0 : struc.getRawStructure()) {
                for(StructureContainedBlock[] level1 : level0) {
                    for(StructureContainedBlock block : level1) {
                        Location bLocation = sender.getLocation().add(block.getX() - centerX, block.getY(), block.getZ() - centerZ);
                        if(!block.getPull().equals(StructureContainedBlock.Pull.NONE)) {
                            handle.setBlockData(bLocation.getBlock(), Material.OAK_SIGN.createBlockData(), false);
                            Sign sign = (Sign) bLocation.getBlock().getState();
                            sign.setLine(1, "[PULL=" + block.getPull() + "_" + block.getPullOffset() + "]");
                            String data = block.getBlockData().getAsString(true);
                            setTerraSign(sign, data);
                            sign.update();
                        } else if(!block.getRequirement().equals(StructureSpawnRequirement.BLANK)) {
                            handle.setBlockData(bLocation.getBlock(), Material.OAK_SIGN.createBlockData(), false);
                            Sign sign = (Sign) bLocation.getBlock().getState();
                            sign.setLine(1, "[SPAWN=" + block.getRequirement() + "]");
                            String data = block.getBlockData().getAsString(true);
                            setTerraSign(sign, data);
                            sign.update();
                        } else {
                            handle.setBlockData(bLocation.getBlock(), block.getBlockData(), false);
                            if(block.getState() != null) {
                                block.getState().getState(bLocation.getBlock().getState()).update(true, false);
                            }
                        }
                    }
                }
            }

            for(int y = 0; y < struc.getStructureInfo().getSizeY(); y++) {
                StructureContainedBlock block = struc.getRawStructure()[centerX][centerZ][y];
                if(block.getRequirement().equals(StructureSpawnRequirement.BLANK) && block.getPull().equals(StructureContainedBlock.Pull.NONE)) {
                    Location bLocation = sender.getLocation().add(block.getX() - centerX, block.getY(), block.getZ() - centerZ);
                    handle.setBlockData(bLocation.getBlock(), Material.OAK_SIGN.createBlockData(), false);
                    Sign sign = (Sign) bLocation.getBlock().getState();
                    sign.setLine(1, "[CENTER]");
                    String data = block.getBlockData().getAsString(true);
                    setTerraSign(sign, data);
                    sign.update();
                    break;
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
            LangUtil.send("command.structure.invalid", sender, args[0]);
        }
*/

        return true;
    }

    private List<String> getStructureNames(TerraPlugin plugin) {
        List<String> names = new ArrayList<>();
        File structureDir = new File(plugin.getDataFolder() + File.separator + "export" + File.separator + "structures");
        if(!structureDir.exists()) return Collections.emptyList();
        Path structurePath = structureDir.toPath();

        FilenameFilter filter = (dir, name) -> name.endsWith(".tstructure");
        for(File f : structureDir.listFiles(filter)) {
            String path = structurePath.relativize(f.toPath()).toString();
            names.add(path.substring(0, path.length() - 11));
        }
        return names;
    }
}
