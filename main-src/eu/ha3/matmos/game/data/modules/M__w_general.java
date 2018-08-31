package eu.ha3.matmos.game.data.modules;

import eu.ha3.matmos.engine.core.interfaces.Data;
import eu.ha3.matmos.game.data.abstractions.module.Module;
import eu.ha3.matmos.game.data.abstractions.module.ModuleProcessor;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;

public class M__w_general extends ModuleProcessor implements Module {
    public M__w_general(Data data) {
        super(data, "w_general");
    }

    @Override
    protected void doProcess() {
        World w = Minecraft.getMinecraft().world;
        EntityPlayer player = getPlayer();
        WorldInfo info = w.getWorldInfo();

        setValue("time_modulo24k", (int)(info.getWorldTime() % 24000L));
        setValue("rain", w.isRaining());
        setValue("thunder", info.isThundering());
        setValue("thunder", w.getThunderStrength(0f) > 0.9f);
        setValue("dimension", player.dimension);
        setValue("light_subtracted", w.getSkylightSubtracted());
        setValue("remote", w.isRemote);
        setValue("moon_phase", w.getMoonPhase());
        setValue("can_rain_on", w.canSeeSky(player.getPosition()));
        setValue("biome_can_rain", w.getBiome(player.getPosition()).canRain());
        setValue("rain_force1k", Math.round(w.getRainStrength(0f) * 1000));
        setValue("thunder_force1k", Math.round(w.getThunderStrength(0f) * 1000));

    }
}
