package dk.muj.derius.farming;

import org.bukkit.block.BlockState;

import dk.muj.derius.api.DPlayer;
import dk.muj.derius.util.AbilityUtil;
import dk.muj.derius.util.Listener;

public class FarmingListener implements Listener
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	private static FarmingListener i = new FarmingListener();
	public static FarmingListener get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void onBlockBreak(DPlayer dplayer, BlockState blockState)
	{	
		AbilityUtil.activateAbility(dplayer, DoubleDropAndReplant.get(), blockState, true);
	}
}
