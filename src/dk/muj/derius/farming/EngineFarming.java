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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
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
		
		// Handle placement and growth state and minimise abuse
		if (DeriusAPI.isBlockPlacedByPlayer(block) &! BlockUtil.isGrowthStateFull(block.getState()) &! isBlockFruitSave(material, block.getState())) return;

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
			}
		}
		
		// Execute the ability for all blocks and give exp
		for (Block activitionBlock : blocks)
		{
			AbilityUtil.activateAbility(dplayer, DoubleDropAndReplant.get(), activitionBlock.getState(), VerboseLevel.NEVER);
			
			if (SkillUtil.canPlayerLearnSkill(dplayer, skill, VerboseLevel.HIGHEST))
			{
				dplayer.addExp(skill, FarmingSkill.getExpGain().get(material).doubleValue());
			}
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
		if (BlockUtil.isGrowthStateFull(block.getState())) return;
		
		// Tool preparation and ability activation
		Optional<Material> optPrepared = dplayer.getPreparedTool();
		
		if (optPrepared.isPresent() && HOE_MATERIALS.contains(optPrepared.get()) && AbilityUtil.canPlayerActivateAbility(dplayer, FertilizeField.get(), VerboseLevel.ALWAYS))
		{
			AbilityUtil.activateAbility(dplayer, FertilizeField.get(), block, VerboseLevel.NORMAL);
		}
		
		return;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onFoodEat(PlayerItemConsumeEvent event)
	{
		// Fields
		Player player = event.getPlayer();
		DPlayer dplayer = DeriusAPI.getDPlayer(player);
		ItemStack item = event.getItem();
		Material material = item.getType();
		
		if ( ! FarmingSkill.getEatingHeatlhyFoods().contains(material)) return;
		int restored = getFoodRestoredFrom(material);
		
		AbilityUtil.activateAbility(dplayer, EatingHealthy.get(), restored, VerboseLevel.NORMAL);
		
		return;
	}
	
	// -------------------------------------------- //
	// REDUCE ABUSE
	// -------------------------------------------- //

	// Only here for first time activation, afterwards the BlockMixin takes its place
	private boolean isBlockFruitSave(Material material, BlockState blockState)
	{
		// Is it a Material we want to check for?
		if ( ! (material == Material.PUMPKIN || material == Material.MELON_BLOCK)) return true;
		
		Material stemMaterial = null;
		
		if (material == Material.PUMPKIN)
		{
			stemMaterial = Material.PUMPKIN_STEM;
		}
		else if (material == Material.MELON_BLOCK)
		{
			stemMaterial = Material.MELON_STEM;
		}
		
		// Check for the neighbors
		List<Block> checkFor = new ArrayList<Block>();
		Block stemBlock = blockState.getBlock();
		
		checkFor.add(stemBlock.getRelative(BlockFace.EAST));
		checkFor.add(stemBlock.getRelative(BlockFace.NORTH));
		checkFor.add(stemBlock.getRelative(BlockFace.WEST));
		checkFor.add(stemBlock.getRelative(BlockFace.SOUTH));
		
		for (Block block: checkFor)
		{
			if (block.getType() == stemMaterial)
			{
				return true;
			}
		}
		
		return false;
	}
	
	private int getFoodRestoredFrom(Material material)
	{
		switch (material)
		{
			case APPLE:
				return 4;
			case BAKED_POTATO:
				return 5;
			case BREAD:
				return 5;
			case CAKE:
				return 2;
			case CARROT:
				return 3;
			case COOKIE:
				return 2;
			case GOLDEN_CARROT:
				return 6;
			case MELON:
				return 2;
			case MUSHROOM_SOUP:
				return 6;
			case PUMPKIN_PIE:
				return 8;
				
			default:
				return 0;
		}
	}

}
