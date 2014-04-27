package code.wizard.armor;

import java.util.ArrayList;
import java.util.List;

import code.configtesting.config.Config;
import code.wizard.main.Main;
import code.wizard.util.BasicUtil;
import code.wizard.util.DamageType;

public class DStats {

	List<DStat> stats = new ArrayList<DStat>();
	
	public DStats(List<DStat> stats){
		this.stats.addAll(stats);
	}
	
	public void addStats(List<DStat> add){
		for(DStat stat : add){
			stats.add(stat);
		}
	}

	public double calculate(DamageType type, double d){
		
		double am = 0;
		double ap = 0;
		
		for(DStat stat : stats){
			if(stat.getType() != type)continue;
			
			int percent = stat.getPercent();
			
			double part = BasicUtil.getPercent(percent, d);
			
			
			
			if(stat.isPositive()){
				ap+=part;
			}else{
				am+=part;
			}
			
			
		}
		
		d+=ap;
		d-=am;
		
		return d;
		
	}
	
}

