package dk.muj.derius.farming.SkillsAndAbilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;

import dk.muj.derius.api.ability.AbilityAbstract;
import dk.muj.derius.api.player.DPlayer;
import dk.muj.derius.api.skill.Skill;
import dk.muj.derius.api.util.SkillUtil;

public class DoubleDropAndReplant extends AbilityAbstract
{
	private static DoubleDropAndReplant i = new DoubleDropAndReplant();
	public static DoubleDropAndReplant get() { return i; }
	
	// -------------------------------------------- //
	// DESCRIPTION
	// -------------------------------------------- //
	
	public DoubleDropAndReplant()
	{
		this.setName("Doubledrop and replace");
		
		this.setDesc("gives doubledrop and sometimes replants the crop");
		
		this.setType(AbilityType.PASSIVE);
	}
	
	// -------------------------------------------- //
	// SKILL & ID
	// -------------------------------------------- //
	
	@Override
	public String getId()
	{
		return "derius:farming:doubledropandreplant";
	}
	
	@Override
	public Skill getSkill()
	{
		return FarmingSkill.get();
	}

	// -------------------------------------------- //
	// ABILITY ACTIVATION
	// -------------------------------------------- //

	
	@Override
	public Object onActivate(DPlayer dplayer, Object other)
	{
		if( ! (other instanceof BlockState)) return null;
		BlockState blockState = (BlockState) other;
		
		Material material = blockState.getType();
		
		if ( ! FarmingSkill.getExpGain().containsKey(material)) return null;
			
		// Check if it is a pumpkin or melon and make sure they don't abuse it.
		if ( ! isBlockFruitSave(material, blockState)) return null;

		// Should doubledrop occur?
		if( ! (FarmingSkill.getExpGain().containsKey(material) && SkillUtil.shouldDoubleDropOccur(dplayer.getLvl(getSkill()), 10))) return null;
		
		ItemStack inHand = dplayer.getPlayer().getItemInHand();
		Location loc = blockState.getLocation();
		
		for(ItemStack is: blockState.getBlock().getDrops(inHand))
		{
			blockState.getWorld().dropItem(loc, is);
			this.replantSeed(10, loc);
		}
	
		return null;
		
	}
	
	private void replantSeed (int chance, Location loc)
	{
		// TODO: Add in later!
	}
	
	@Override
	public void onDeactivate(DPlayer dplayer, Object other)
	{
		// Nothing to do here
	}

	// -------------------------------------------- //
	// Level description
	// -------------------------------------------- //
	
	@Override
	public Optional<String> getLvlDescriptionMsg(int lvl)
	{
		double percent = Math.min(100.0, (double) lvl/10.0);
		return Optional.of("<i>Chance to double drop and replant: <h>" + String.valueOf(percent) + "%");
	}
	
	// -------------------------------------------- //
	// REDUCE ABUSE
	// -------------------------------------------- //

	// Only here for first time activation, afterwards the BlockMixin takes its place
	private boolean isBlockFruitSave(Material material, BlockState blockState)
	{
		// Is it a Material we want to check for?
		Material stemMaterial = null;
		
		if (material == Material.PUMPKIN)
		{
			stemMaterial = Material.PUMPKIN_STEM;
		}
		else if (material == Material.MELON)
		{
			stemMaterial = Material.MELON_STEM;
		}
		
		// No? return true
		if (stemMaterial == null) return true;
		
		// Check for the neighbors
		List<Block> checkFor = new ArrayList<Block>();
		
		checkFor.add(blockState.getBlock().getRelative(BlockFace.EAST));
		checkFor.add(blockState.getBlock().getRelative(BlockFace.NORTH));
		checkFor.add(blockState.getBlock().getRelative(BlockFace.WEST));
		checkFor.add(blockState.getBlock().getRelative(BlockFace.SOUTH));
		
		for (Block block: checkFor)
		{
			if (block.getType() == stemMaterial)
			{
				return true;
			}
		}
		
		return false;
	}
	

}
