package dk.muj.farming;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
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
	
	public DoubleDropAndReplant()
	{
		this.setName("Doubledrop and replace");
		this.setDescription("gives doubledrop and sometimes replants it");
		this.setType(AbilityType.PASSIVE);
		
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

	@Override
	public void onActivate(MPlayer mplayer, Object block)
	{
		if(!(block instanceof Block))
			return;
		if(!mplayer.isPlayer())
			return;
		
		Skill skill = getSkill();
		
		Block b = (Block) block;
		
		int logId = b.getTypeId();
		ItemStack inHand = mplayer.getPlayer().getItemInHand();
		Location loc = b.getLocation();
		
		if(skill.CanSkillBeEarnedInArea(b.getLocation()))
		{
			if(!MConf.get().expGain.containsKey(logId))
				return;
			int expGain = MConf.get().expGain.get(logId);
			mplayer.AddExp(FarmingSkill.get(), expGain);
		}
		
		if(i.CanAbilityBeUsedInArea(loc) && MConf.get().expGain.containsKey(logId))
		{
				SkillUtil.PlayerGetDoubleDrop(mplayer, skill, 10);
				this.replantSeed(skill, 10, loc);
		}
	}
	
	// TODO: Add in later!
	private void replantSeed (Skill skill, int chance, Location loc)
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
}
