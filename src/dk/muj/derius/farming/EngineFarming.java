package dk.muj.derius.farming;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.EngineAbstract;

import dk.muj.derius.api.DeriusAPI;
import dk.muj.derius.api.VerboseLevel;
import dk.muj.derius.api.player.DPlayer;
import dk.muj.derius.api.skill.Skill;
import dk.muj.derius.api.util.AbilityUtil;
import dk.muj.derius.api.util.SkillUtil;
import dk.muj.derius.lib.BlockUtil;

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
	public void ExpAndPassiveAbilityActivation(BlockBreakEvent event)
	{	
		Player player = event.getPlayer();
		DPlayer dplayer = DeriusAPI.getDPlayer(player);
		Block block = event.getBlock();
		Material material = block.getType();
		List<Block> blocks = new ArrayList<Block>();
		Skill skill = FarmingSkill.get();
		
		// Is the block supposed to get exp and doubledrop?
		if ( ! FarmingSkill.getExpGain().containsKey(material)) return;
		
		// Handle placement and growth state
		if ( ! (DeriusAPI.isBlockPlacedByPlayer(block) && BlockUtil.isGrowthStateFull(block.getState()))) return;
		
		// Add initial block
		blocks.add(block);
		
		// Special exp and doubledrop handling for cacti and sugar canes
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
		
		// Execute the ability for all blocks
		for (Block activitionBlock : blocks)
		{
			AbilityUtil.activateAbility(dplayer, DoubleDropAndReplant.get(), activitionBlock.getState(), VerboseLevel.NEVER);
		}
		
		return;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void ActiveAbilityActivition(PlayerInteractEvent event)
	{
		// Fields
		Player player = event.getPlayer();
		DPlayer dplayer = DeriusAPI.getDPlayer(player);
		Block block = event.getClickedBlock();
		
		// Is field block?
		if ( ! FarmingSkill.getFertilizeFieldMaterials().contains(block.getType())) return;
		
		// Tool preparation and ability activation
		Optional<Material> optPrepared = dplayer.getPreparedTool();
		
		if (optPrepared.isPresent() && HOE_MATERIALS.contains(optPrepared.get()) && AbilityUtil.canPlayerActivateAbility(dplayer, FertilizeField.get(), VerboseLevel.ALWAYS))
		{
			AbilityUtil.activateAbility(dplayer, FertilizeField.get(), block, VerboseLevel.NORMAL);
		}
	}

}
