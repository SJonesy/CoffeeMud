package com.planet_ink.coffee_mud.Behaviors;

import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.common.*;
import com.planet_ink.coffee_mud.utils.*;
import java.util.*;

/* 
   Copyright 2000-2004 Bo Zimmerman

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
public class NiceAbilities extends ActiveTicker
{
	public String ID(){return "NiceAbilities";}
	protected int canImproveCode(){return Behavior.CAN_MOBS;}
	public NiceAbilities()
	{
		minTicks=10; maxTicks=20; chance=100;
		tickReset();
	}



	public boolean tick(Tickable ticking, int tickID)
	{
		super.tick(ticking,tickID);
		if((canAct(ticking,tickID))&&(ticking instanceof MOB))
		{
			MOB mob=(MOB)ticking;
			Room thisRoom=mob.location();
			if(thisRoom==null) return true;

			double aChance=Util.div(mob.curState().getMana(),mob.maxState().getMana());
			if((Math.random()>aChance)||(mob.curState().getMana()<50))
				return true;

			MOB target=thisRoom.fetchInhabitant(Dice.roll(1,thisRoom.numInhabitants(),-1));
			int x=0;
			while(((target==null)||(target.getVictim()==mob)||(target==mob)||(target.isMonster()))&&((++x)<10))
				target=thisRoom.fetchInhabitant(Dice.roll(1,thisRoom.numInhabitants(),-1));

			int tries=0;
			Ability tryThisOne=null;
			while((tryThisOne==null)&&(tries<100)&&((mob.numAbilities())>0))
			{
				tryThisOne=mob.fetchAbility(Dice.roll(1,mob.numAbilities(),-1));
				if((tryThisOne!=null)
				   &&(mob.fetchEffect(tryThisOne.ID())==null)
				   &&((tryThisOne.quality()==Ability.BENEFICIAL_OTHERS)||(tryThisOne.quality()==Ability.OK_OTHERS)))
				{
					if((tryThisOne.classificationCode()&Ability.ALL_CODES)==Ability.PRAYER)
					{
						if(!tryThisOne.appropriateToMyAlignment(mob.getAlignment()))
							tryThisOne=null;
					}
				}
				else
					tryThisOne=null;
				tries++;
			}
			if(tryThisOne!=null)
				if((target!=null)&&(target!=mob)&&(!target.isMonster()))
				{
					tryThisOne.setProfficiency(100);
					Vector V=new Vector();
					V.addElement(target.name());
					tryThisOne.invoke(mob,V,target,false);
				}
		}
		return true;
	}
}
