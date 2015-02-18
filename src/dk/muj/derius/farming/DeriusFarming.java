package dk.muj.derius.farming;

import com.massivecraft.massivecore.MassivePlugin;

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
		if ( ! super.preEnable()) return;
			
		FarmingSkill.get().register();
		FertilizeField.get().register();
		DoubleDropAndReplant.get().register();
		
		FarmingListener.get();
		
		super.postEnable();
	}
}
