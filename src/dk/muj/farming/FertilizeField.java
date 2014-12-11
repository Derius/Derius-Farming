package dk.muj.farming;

import org.bukkit.Material;

import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.ability.Ability;
import dk.muj.derius.ability.AbilityType;
import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.farming.entity.MConf;
import dk.muj.derius.skill.Skill;

public class FertilizeField extends Ability
{
	private static FertilizeField i = new FertilizeField();
	public static FertilizeField get() { return i; }
	
	// -------------------------------------------- //
	// DESCRIPTION
	// -------------------------------------------- //
	
	public FertilizeField()
	{
		this.setName("Fertilize Field");
		this.setDescription("Fertilizes a field");
		this.setType(AbilityType.ACTIVE);
		
		this.addInteractKeys(
				Material.WOOD_HOE,
				Material.STONE_HOE,
				Material.IRON_HOE,
				Material.GOLD_HOE,
				Material.DIAMOND_HOE
				);
	}
	
	// -------------------------------------------- //
	// SKILL & ID
	// -------------------------------------------- //
	
	@Override
	public int getId()
	{
		return MConf.get().getFertilizeFieldId;
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
	public void onActivate(MPlayer p, Object other)
	{
		// TODO: Add fertilization skill by radius
	}
	
	@Override
	public void onDeactivate(MPlayer p)
	{
		// Nothing at the moment.
	}

	// -------------------------------------------- //
	// ABILITY ACTIVATION
	// -------------------------------------------- //
	
	@Override
	public boolean CanPlayerActivateAbility(MPlayer p)
	{
		if(p.getLvl(FarmingSkill.get()) >= MConf.get().getFertilizeFieldMinLvl)
			return true;
		return false;
	}
	
	// -------------------------------------------- //
	// Level description
	// -------------------------------------------- //
	
	// TODO: Add in actual radius method
	@Override
	public String getLvlDescription(int lvl)
	{
		int radius = 1;
		return Txt.parse("Fertilizes a field with the radius %s", radius);
	}
}
