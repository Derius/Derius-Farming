package dk.muj.derius.farming;

import org.bukkit.Material;
import org.bukkit.block.BlockState;

import com.massivecraft.massivecore.util.MUtil;

import dk.muj.derius.DeriusCore;
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
	
	private FarmingListener()
	{
		i = this;
		
		Listener.registerTools(MUtil.list(
				Material.WOOD_HOE,
				Material.STONE_HOE,
				Material.IRON_HOE,
				Material.GOLD_HOE,
				Material.DIAMOND_HOE));
		Listener.registerBlockBreakKeys(this, FarmingSkill.getExpGain().keySet());
		DeriusCore.getBlockMixin().addBlockTypesToListenFor(FarmingSkill.getExpGain().keySet());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void onBlockBreak(DPlayer dplayer, BlockState blockState)
	{	
		if ( ! dplayer.isPlayer()) return;
		
		// TODO: Add in active ability!
		
		Integer exp = FarmingSkill.getExpGain().get(blockState.getType());
		if ( exp != null)
		{
			dplayer.addExp(FarmingSkill.get(), exp);
		}
		
		AbilityUtil.activateAbility(dplayer, DoubleDropAndReplant.get(), blockState, true);
	}
}
