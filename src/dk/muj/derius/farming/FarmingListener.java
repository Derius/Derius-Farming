package dk.muj.derius.farming;

import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import dk.muj.derius.entity.MPlayer;
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
	public void onBlockBreak(MPlayer mplayer, BlockState block)
	{
		if ( ! mplayer.isPlayer()) return;
		Player player = mplayer.getPlayer();
		ItemStack inHand = player.getItemInHand();
		
		mplayer.activateAbility(DoubleDropAndReplant.get(), block);
	}
}
