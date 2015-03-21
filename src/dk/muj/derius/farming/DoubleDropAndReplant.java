package dk.muj.derius.farming;

import java.util.Optional;

import org.bukkit.CropState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
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
		this.setName("Doubledrop And Replant");
		
		this.setDesc("Gives doubledrop and sometimes replants the crop");
		
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

		// Should doubledrop occur?
		if( ! SkillUtil.shouldDoubleDropOccur(dplayer.getLvl(getSkill()), 10)) return null;
		
		ItemStack inHand = dplayer.getPlayer().getItemInHand();
		Location loc = blockState.getLocation();
		
		// Apply doubledrop and replant if possible
		for(ItemStack is: blockState.getBlock().getDrops(inHand))
		{
			blockState.getWorld().dropItem(loc, is);
			replantSeed(material, dplayer, blockState);
		}
	
		return null;
		
	}
	
	@Override
	public void onDeactivate(DPlayer dplayer, Object other) { }

	// -------------------------------------------- //
	// Level description
	// -------------------------------------------- //
	
	@Override
	public Optional<String> getLvlDescriptionMsg(int lvl)
	{
		double percentDrop = Math.min(100.0, (double) lvl / FarmingSkill.getDoubleDropLevelPerPercent());
		double percentReplant = Math.min(100.0, (double) lvl / FarmingSkill.getReplantLevelPerPercent());
		return Optional.of("<i>Chance to double drop is<h> " + String.valueOf(percentDrop) + "%" + "<i> and to replant <h>" + String.valueOf(percentReplant) + "%");
	}
	
	// -------------------------------------------- //
	// REPLANT SEED
	// -------------------------------------------- //
	
	@SuppressWarnings("deprecation")
	private void replantSeed (Material material, DPlayer dplayer, BlockState blockState)
	{
		if ( ! FarmingSkill.getReplantMaterials().contains(material)) return;
		if ( ! SkillUtil.shouldDoubleDropOccur(dplayer.getLvl(getSkill()), FarmingSkill.getReplantLevelPerPercent())) return;
		
		Block block = blockState.getBlock();
		switch (material)
		{
			case CARROT:
			case CROPS:
			case POTATO:
				block.setData(CropState.SEEDED.getData());
				break;
				
			default:
				block.setData((byte) 0);
				break;
		}
	}
	

}
