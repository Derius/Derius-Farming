package dk.muj.derius.farming;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.EngineAbstract;

import dk.muj.derius.api.DeriusAPI;
import dk.muj.derius.api.VerboseLevel;
import dk.muj.derius.api.player.DPlayer;
import dk.muj.derius.api.skill.Skill;
import dk.muj.derius.api.util.AbilityUtil;
import dk.muj.derius.api.util.SkillUtil;

public class EngineFarming extends EngineAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static EngineFarming i = new EngineFarming();
	public static EngineFarming get() { return i; }
	public EngineFarming() { }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Plugin getPlugin()
	{
		return DeriusFarming.get();
	}

	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public static final List<Material> HOE_MATERIALS = new ArrayList<Material>();
	
		static
		{
			HOE_MATERIALS.add(Material.WOOD_HOE);
			HOE_MATERIALS.add(Material.STONE_HOE);
			HOE_MATERIALS.add(Material.IRON_HOE);
			HOE_MATERIALS.add(Material.GOLD_HOE);
			HOE_MATERIALS.add(Material.DIAMOND_HOE);
		}
		
	// -------------------------------------------- //
	// LISTENER
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event)
	{	
		Player player = event.getPlayer();
		DPlayer dplayer = DeriusAPI.getDPlayer(player);
		Block block = event.getBlock();
		Material material = block.getType();
		List<Block> blocks = new ArrayList<Block>();
		Skill skill = FarmingSkill.get();
		
		blocks.add(block);
		
		// Tool preparation and ability activition
		Optional<Material> optPrepared = dplayer.getPreparedTool();
		
		if (optPrepared.isPresent() && HOE_MATERIALS.contains(optPrepared.get()))
		{
			AbilityUtil.activateAbility(dplayer, FertilizeField.get(), block, VerboseLevel.NORMAL);
		}
		
		// Special passive ability activation
		if ( ! FarmingSkill.getExpGain().containsKey(material)) return;
		
		// Handle growth state
		if ( ! isGrowthStateCorrect(block.getState())) return;
		
		// Special exp and doubledrop handling, cacti and sugarcanes
		if (material == Material.SUGAR_CANE_BLOCK || material == Material.CACTUS)
		{
			boolean hasLeft = true;
			Block upperBlock = block;
			
			// Check for more blocks to add
			while (hasLeft)
			{
				upperBlock = upperBlock.getRelative(BlockFace.UP);
				
				if (upperBlock.getType() != material) break;
				
				blocks.add(upperBlock);
				
				// Give exp immediately
				if ( ! SkillUtil.canPlayerLearnSkill(dplayer, skill, VerboseLevel.HIGHEST)) continue;
				dplayer.addExp(skill, FarmingSkill.getExpGain().get(material).longValue());
			}
		}
		
		for (Block activitionBlock : blocks)
		{
			AbilityUtil.activateAbility(dplayer, DoubleDropAndReplant.get(), activitionBlock.getState(), VerboseLevel.NEVER);
		}
		
		return;
	}
	private boolean isGrowthStateCorrect(BlockState state)
	{
		// TODO Add in actual checking, it's complicated
		return true;
	}

}
