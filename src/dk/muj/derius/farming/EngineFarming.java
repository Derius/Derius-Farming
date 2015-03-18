package dk.muj.derius.farming;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.NetherWartsState;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.material.CocoaPlant;
import org.bukkit.material.CocoaPlant.CocoaPlantSize;
import org.bukkit.material.Crops;
import org.bukkit.material.NetherWarts;
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
		
		// Handle placement and growth state
		if ( ! (DeriusAPI.isBlockPlacedByPlayer(block) && isGrowthStateCorrect(block.getState()))) return;
		
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
				if (DeriusAPI.isBlockPlacedByPlayer(upperBlock)) continue;
				
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
	@SuppressWarnings("deprecation")
	private boolean isGrowthStateCorrect(BlockState state)
	{
		 switch (state.getType())
		 {
			// These are the special cases to check for
			case CARROT:
			case POTATO:
				return state.getRawData() == CropState.RIPE.getData();
				 
			case CROPS:
				return ((Crops) state.getData()).getState() == CropState.RIPE;
				 
			case NETHER_WARTS:
				return ((NetherWarts) state.getData()).getState() == NetherWartsState.RIPE;

			case COCOA:
				return ((CocoaPlant) state.getData()).getSize() == CocoaPlantSize.LARGE;
				 
			// Normally, it is true. The rest gets taken care of by the block placement storage
			default:
				return true;
		 }
	}

}
