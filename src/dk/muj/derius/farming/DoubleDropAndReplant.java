package dk.muj.derius.farming;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.massivecore.collections.WorldExceptionSet;

import dk.muj.derius.api.Ability;
import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.Skill;
import dk.muj.derius.entity.ability.DeriusAbility;
import dk.muj.derius.farming.entity.MConf;
import dk.muj.derius.util.SkillUtil;

public class DoubleDropAndReplant extends DeriusAbility implements Ability
{
	private static DoubleDropAndReplant i = new DoubleDropAndReplant();
	public static DoubleDropAndReplant get() { return i; }
	
	// -------------------------------------------- //
	// DESCRIPTION
	// -------------------------------------------- //
	
	public DoubleDropAndReplant()
	{
		this.setName("Doubledrop and replace");
		
		this.setDesc("gives doubledrop and sometimes replants it");
		
		this.setType(AbilityType.PASSIVE);
	}
	
	// -------------------------------------------- //
	// SKILL & ID
	// -------------------------------------------- //
	
	@Override
	public String getId()
	{
		return MConf.get().getDoubleDropAndReplantId;
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
	public Optional<Object> onActivate(DPlayer dplayer, Object blockState)
	{
		if( ! (blockState instanceof BlockState))
			return Optional.empty();
		
		BlockState b = (BlockState) blockState;
		
		@SuppressWarnings("deprecation")
		int cropId = b.getTypeId();
		Material material = b.getType();
		ItemStack inHand = dplayer.getPlayer().getItemInHand();
		Location loc = b.getLocation();
		
		if(inHand.getItemMeta().hasEnchant(Enchantment.SILK_TOUCH))
			return Optional.empty();
			
		// Check if it is a pumpkin or melon and make sure they don't abuse it.
		if (isBlockFruitAbused(cropId, loc))
			return Optional.empty();

		
		if(MConf.get().expGain.containsKey(material) && SkillUtil.shouldDoubleDropOccur(dplayer.getLvl(getSkill()), 10))
		{
			for(ItemStack is: b.getBlock().getDrops(inHand))
			{
				b.getWorld().dropItem(loc, is);
				this.replantSeed(10, loc);
			}
		}
	
		return Optional.empty();
		
	}
	
	
	// TODO: Add in later!
	private void replantSeed (int chance, Location loc)
	{
		
	}
	
	@Override
	public void onDeactivate(DPlayer dplayer, Object other)
	{
		// TODO Auto-generated method stub
		
	}

	// -------------------------------------------- //
	// Level description
	// -------------------------------------------- //
	
	@Override
	public String getLvlDescriptionMsg(int lvl)
	{
		return "chance to double drop and replant seed" + lvl/10.0 + "%";
	}
	
	// -------------------------------------------- //
	// REDUCE ABUSE
	// -------------------------------------------- //

	private boolean isBlockFruitAbused(int Id, Location loc)
	{
		int stemId;
		
		if (Id == 86 || Id == 103)
		{
			if (Id == 86)
			{
				stemId = 105;
			}
			else
			{
				stemId = 104;
			}
			
			if (isNextToStem(stemId, loc))
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		
		return false;
	}
	
	@SuppressWarnings("deprecation")
	private boolean isNextToStem(int stemId, Location loc)
	{
		Block b = loc.getBlock();
		List<Block> checkFor = new ArrayList<Block>();
		
		checkFor.add(b.getRelative(BlockFace.EAST));
		checkFor.add(b.getRelative(BlockFace.NORTH));
		checkFor.add(b.getRelative(BlockFace.WEST));
		checkFor.add(b.getRelative(BlockFace.SOUTH));
		
		for (Block block: checkFor)
			if (block.getTypeId() == stemId)
				return true;
		
		return false;
	}

	@Override
	public void setWorldsEarn(WorldExceptionSet worldsUse)
	{
		// TODO Auto-generated method stub
		
	}

}
