package eu.ha3.matmos.game.data.modules.world;

import eu.ha3.matmos.engine.core.interfaces.Data;
import eu.ha3.matmos.game.data.modules.Module;
import eu.ha3.matmos.game.data.modules.ModuleProcessor;
import eu.ha3.matmos.game.mod.MAtMod;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

public class ModuleBiome extends ModuleProcessor implements Module {
    private final MAtMod mod;

    public ModuleBiome(Data data, MAtMod mod) {
        super(data, "w_biome");
        this.mod = mod;
    }

    @Override
    protected void doProcess() {
        int biomej = this.mod.getConfig().getInteger("useroptions.biome.override");

        if (biomej <= -1) {
            //Solly edit - only calculate biome once
            Biome biome = calculateBiome();
            setValue("id", Biome.getIdForBiome(biome));
            setValue("biome_name", biome.getBiomeName());
        } else {
            setValue("id", biomej);
            setValue("biome_name", "");
        }
    }

    private Biome calculateBiome() {
        Minecraft mc = Minecraft.getMinecraft();
        BlockPos playerPos = getPlayer().getPosition();

        return mc.world.getChunk(playerPos).getBiome(playerPos, mc.world.getBiomeProvider());
    }
}