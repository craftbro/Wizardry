package code.wizard.armor;

import code.wizard.util.DamageType;

public class DStat {

	
	boolean pos;
	int per;
	DamageType type;
	
	public DStat(boolean pos, int per, DamageType type){
		this.pos = pos;
		this.per = per;
		this.type = type;
	}  
	
	public boolean isPositive(){
		return pos;
	}
	
	public int getPercent(){
		return per;
	}
	
	public DamageType getType(){
		return type;
	}
	
}
