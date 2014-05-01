package code.wizard.spell;

public enum SpellSlot {

	PRIMARY_MELEE("pm"),
	SECUNDAIRY_MELEE("sm"),
	PRIMARY_STICK("ps"),
	SECUNDAIRY_STICK("ss"),
	PRIMARY_WAND("pw"),
	SECUNDAIRY_WAND("sw"),
	SPECIAL("s");
	
	String name;
	
	
	private SpellSlot(String n){
		name = n;
	}
	
	public String getConfigName(){
		return name;
	}
}
