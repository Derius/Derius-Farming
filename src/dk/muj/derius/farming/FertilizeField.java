package dk.muj.derius.farming;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.mutable.MutableBoolean;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.material.CocoaPlant;

import dk.muj.derius.api.ability.AbilityAbstract;
import dk.muj.derius.api.player.DPlayer;
import dk.muj.derius.api.req.ReqIsAtleastLevel;
import dk.muj.derius.api.skill.Skill;
import dk.muj.derius.api.util.AbilityUtil;
import dk.muj.derius.lib.BlockUtil;

public class FertilizeField extends AbilityAbstract
{
	private static FertilizeField i = new FertilizeField();
	public static FertilizeField get() { return i; }
	
	// -------------------------------------------- //
	// DESCRIPTION
	// -------------------------------------------- //
	
	public FertilizeField()
	{
		this.setName("Fertilize Field");
		
		this.setDesc("Fertilizes a field");
		
		this.setType(AbilityType.ACTIVE);
		
		this.addActivateRequirements(ReqIsAtleastLevel.get( () -> FarmingSkill.getFertilizeFieldMinLvl()));
	}
	
	// -------------------------------------------- //
	// SKILL & ID
	// -------------------------------------------- //
	
	@Override
	public String getId()
	{
		return "derius:farming:fertilizefield";
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
		if ( ! (other instanceof Block)) return null;
		Block sourceBlock = (Block) other;
		
		int level = dplayer.getLvl(FarmingSkill.get());
		
		// Getting the Field
		Set<Block> field = getField(sourceBlock, level);
		if (field == null || field.isEmpty()) return AbilityUtil.CANCEL;
		
		// Apply growth for each
		field.forEach(b -> applyGrowth(b, level));
		
		return null;
	}
	
	@Override
	public void onDeactivate(DPlayer dplayer, Object other) { }
	
	@SuppressWarnings("deprecation")
	private void applyGrowth(Block block, int level)
	{
		byte data = block.getData();
		
		// If it is already fully grown, return.
		if ( ! BlockUtil.isGrowthStateFull(block.getState())) return;
		
		// Do math to find out how much the field grows and limit it
		double steps = level / FarmingSkill.getFertilizeFieldGrowthStepsPerLevels();
		byte additionalData = (byte) (steps * Math.random() * FarmingSkill.getFertilizeFieldGrowthAmountPerStep());
		additionalData = (byte) Math.min(additionalData, FarmingSkill.getFertilizeFieldGrowthMax());
		
		// Limit number to fully grown and set 
		data = (byte) Math.min(data + additionalData, getDataOfFullyGrown(block));
		block.setData(data);
		
		return;
	}
	
	private byte getDataOfFullyGrown(Block block)
	{
		// Every "crop" has it's own grown 
		// Visit http://minecraft.gamepedia.com/Data_values for more information
		Material mat = block.getType();
		
		if (mat == Material.CROPS || mat == Material.CARROT || mat == Material.POTATO)
		{
			return 7;
		}
		else if (mat == Material.NETHER_WARTS)
		{
			return 3;
		}
		else if (mat == Material.COCOA)
		{
			// Cocoa plants are the most weirdest plant to handle
			byte data = 0;
			CocoaPlant plant = (CocoaPlant) block.getState().getData();
			BlockFace face = plant.getFacing();
			
			// The blockface it faces changes its byte value
			if (face == BlockFace.EAST)
			{
				data += 1;
			}
			else if (face == BlockFace.SOUTH)
			{
				data += 2;
			}
			else if (face == BlockFace.WEST)
			{
				data += 3;
			}
			
			// And the fully grown state should be 144. This adds up to the result.
			return (byte) (data + 144);
		}
		else
		{
			// Defaulty, we just set it to seven. The probability of this being correct is really high.
			return 7;
		}
	}
	
	// -------------------------------------------- //
	// Level description
	// -------------------------------------------- //
	
	@Override
	public Optional<String> getLvlDescriptionMsg(int lvl)
	{
		return Optional.of("Fertilizes a field with the radius " + String.valueOf(getFertilizationRadius(lvl)));
	}

	private static double getFertilizationRadius(int lvl)
	{
		double baseRadius = FarmingSkill.getFertilizeFieldBaseRadius();
		double steps = lvl / FarmingSkill.getFertilizeFieldRadiusStepPerLevels();
		
		return baseRadius + (steps * FarmingSkill.getFertilizeFieldRadiusPerStep());
	}
	
	// -------------------------------------------- //
	// FIELD
	// -------------------------------------------- //
	
	// Thanks for this beautiful stream in Woodcutting Madus! :D
	public static Set<Block> getField(final Block source, int level)
	{
		// We create a return...
		Set<Block> ret = new HashSet<>();
		// ... which initially contains the source block.
		ret.add(source);
		
		// We calculate the radius with the methode.
		final int radius = (int) Math.round(getFertilizationRadius(level));
		
		// This boolean determines if we should look any further.
		// it is set to false if an operation doesn't add anything to the return value.
		MutableBoolean someLeft = new MutableBoolean(true);
		
		// The latest added blocks, we use this to prevent looking through
		// the same blocks multiple times.
		// Initially it is just the source block.
		Set<Block> latest = ret;
		  
		while (someLeft.booleanValue())
		{
			// The blocks we are going to add next time we modify return.
			Set<Block> add = new HashSet<Block>();
			
			// For all the latest added blocks, we look through
			// the nearest ones to add to the three.
			latest.forEach( (Block block) ->
				add.addAll(BlockUtil.getSurroundingBlocks(block)
							.stream()
							// Of course it must be a field material
							.filter(b -> FarmingSkill.getFertilizeFieldMaterials().contains(b.getType()))
							// It must be the same Material as the source
							.filter(b -> source.getType() == b.getType())
							// And it may not be too far away.
							.filter(b -> isLocationOk(source.getState(), b.getState(), radius))
							.collect(Collectors.toSet()))
			);
			// So if true is returned, we modified the return...
			// and there might still be wood to find.
			someLeft.setValue(ret.addAll(add));
			// The latest added blocks are the ones we just added.
			latest = add;	
		}
		  
		return ret;
	}

	private static boolean isLocationOk(BlockState source, BlockState compared, int radius)
	{
		// Check for all three dimensions
		// If distance on the axes is too much, location isn't ok.
		if (Math.abs(source.getX()-compared.getX()) > radius) return false;
		if (Math.abs(source.getY()-compared.getY()) > radius) return false;
		if (Math.abs(source.getZ()-compared.getZ()) > radius) return false;
		
		// It passed all the checks.
		return true;
	}

}
