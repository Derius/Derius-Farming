package dk.muj.derius.farming;

import com.massivecraft.massivecore.MassivePlugin;

import dk.muj.derius.farming.entity.MConfColl;

public class DeriusFarming extends MassivePlugin
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static DeriusFarming i;
	public static DeriusFarming get() { return i; }
	public DeriusFarming() { i = this; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void onEnable()
	{
		super.preEnable();
	
		MConfColl.get().init();
			
		FarmingSkill.get().register();
		FertilizeField.get().register();
		DoubleDropAndReplant.get().register();
		
		super.postEnable();
	}
}
