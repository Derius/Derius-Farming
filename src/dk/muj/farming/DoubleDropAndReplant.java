package dk.muj.farming;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import dk.muj.derius.ability.Ability;
import dk.muj.derius.ability.AbilityType;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.farming.entity.MConf;
import dk.muj.derius.skill.Skill;
import dk.muj.derius.skill.SkillUtil;

public class DoubleDropAndReplant extends Ability
{
	private static DoubleDropAndReplant i = new DoubleDropAndReplant();
	public static DoubleDropAndReplant get() { return i; }
	
	// -------------------------------------------- //
	// DESCRIPTION
	// -------------------------------------------- //
	
	@SuppressWarnings("deprecation")
	public DoubleDropAndReplant()
	{
		this.setName("Doubledrop and replace");
		this.setDescription("gives doubledrop and sometimes replants it");
		this.setType(AbilityType.PASSIVE);
		this.setAbilityCheck(true);
		
		List<Material> blockBreakKeys = new ArrayList<Material>();
		for(int i : MConf.get().expGain.keySet())
			blockBreakKeys.add(Material.getMaterial(i));
		this.addBlockBreakKeys(blockBreakKeys);
	}
	
	// -------------------------------------------- //
	// SKILL & ID
	// -------------------------------------------- //
	
	@Override
	public int getId()
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

	@SuppressWarnings("deprecation")
	@Override
	public void onActivate(MPlayer mplayer, Object block)
	{
		if(!(block instanceof Block))
			return;
		if(!mplayer.isPlayer())
			return;
		
		Skill skill = getSkill();
		
		Block b = (Block) block;
		
		int cropId = b.getTypeId();
		ItemStack inHand = mplayer.getPlayer().getItemInHand();
		Location loc = b.getLocation();
		
		if(inHand.getItemMeta().hasEnchant(Enchantment.getById(33)))
			return;
		
		if(skill.CanSkillBeEarnedInArea(b.getLocation()))
		{
			if(!MConf.get().expGain.containsKey(cropId))
				return;
			
			// Check if it is a pumpkin or melon and make sure they don't abuse it.
			if (isBlockFruitAbused(cropId, loc))
				return;
			
			int expGain = MConf.get().expGain.get(cropId);
			mplayer.AddExp(FarmingSkill.get(), expGain);
		}
		
		if(this.CanAbilityBeUsedInArea(loc) && MConf.get().expGain.containsKey(cropId))
		{
				SkillUtil.PlayerGetDoubleDrop(mplayer, skill, 10);
				this.replantSeed(10, loc);
		}
	}
	
	// TODO: Add in later!
	private void replantSeed (int chance, Location loc)
	{
		
	}
	
	@Override
	public void onDeactivate(MPlayer p)
	{
		// Nothing to deactivate in a passive skill
	}

	// -------------------------------------------- //
	// ABILITY ACTIVATION
	// -------------------------------------------- //
	
	@Override
	public boolean CanPlayerActivateAbility(MPlayer p)
	{
		return true;
	}
	
	// -------------------------------------------- //
	// Level description
	// -------------------------------------------- //
	
	@Override
	public String getLvlDescription(int lvl)
	{
		return "chance to double drop and replant seed" + lvl/10.0 + "%";
	}
	
	// -------------------------------------------- //
	// REDUCE ABUSE
	// -------------------------------------------- //

	private boolean isBlockFruitAbused(int Id, Location loc)
	{
		int stemId;
		
		if (isBlockFruit(Id))
		{
			if (Id == 86)
				stemId = 105;
			else
				stemId = 104;
			
			if (isNextToStem(stemId, loc))
				return false;
		}
		
		return false;
	}
	
	private boolean isBlockFruit (int Id)
	{
		if ( Id == 86 || Id == 103)
			return true;
		return false;
	}
	
	@SuppressWarnings("deprecation")
	private boolean isNextToStem(int stemId, Location loc)
	{
		double blockX = loc.getX();
		double blockY = loc.getY();
		double blockZ = loc.getZ();
		World world = loc.getWorld();
		List<Block> checkFor = new ArrayList<Block>();
		
		Block BlockA = new Location(world, blockX, blockY, blockZ -1.0 ).getBlock();
		Block BlockB = new Location(world, blockX -1.0 , blockY, blockZ).getBlock();
		Block BlockC = new Location(world, blockX, blockY, blockZ +1.0 ).getBlock();
		Block BlockD = new Location(world, blockX + 1.0, blockY, blockZ).getBlock();
		
		checkFor.add(BlockA);
		checkFor.add(BlockB);
		checkFor.add(BlockC);
		checkFor.add(BlockD);
		
		for (Block block: checkFor)
			if (block.getTypeId() == stemId)
				return true;
		
		return false;
	}
}
