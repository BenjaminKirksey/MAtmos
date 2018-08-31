package eu.ha3.matmos.game.data.modules;

import eu.ha3.matmos.engine.core.interfaces.Data;
import eu.ha3.matmos.game.data.abstractions.module.Module;
import eu.ha3.matmos.game.data.abstractions.module.ModuleProcessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

/*
--filenotes-placeholder
*/

public class M__ply_action extends ModuleProcessor implements Module
{
	public M__ply_action(Data data)
	{
		super(data, "ply_action");
	}
	
	@Override
	protected void doProcess()
	{
        EntityPlayerSP player = Minecraft.getMinecraft().player;
		
		setValue("swing_progress16", (int) Math.floor(player.swingProgress * 16));
		setValue("swinging", player.swingProgress != 0);
		setValue("fall_distance1k", (int) (player.fallDistance * 1000));
		setValue("item_use_duration", player.getItemInUseCount());
	}
}
