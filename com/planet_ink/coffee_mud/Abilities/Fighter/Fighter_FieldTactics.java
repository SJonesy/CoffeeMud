package com.planet_ink.coffee_mud.Abilities.Fighter;
import com.planet_ink.coffee_mud.Abilities.StdAbility;
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

public class Fighter_FieldTactics extends StdAbility
{
	public String ID() { return "Fighter_FieldTactics"; }
	public String name(){ return "Field Tactics";}
	public String displayText(){ return hidden?"(Hidden)":"";}
	protected int canAffectCode(){return CAN_MOBS;}
	protected int canTargetCode(){return 0;}
	public int quality(){return Ability.BENEFICIAL_SELF;}
	public int classificationCode(){return Ability.SKILL;}
	public boolean isAutoInvoked(){return true;}
	public boolean canBeUninvoked(){return false;}
	private static final Integer[] landClasses = {new Integer(-1)};
	public Integer[] landClasses(){return landClasses;}
	protected boolean activated=false;
	protected boolean hidden=false;
	protected long sitTime=0;

	public boolean tick(Tickable ticking, int tickID)
	{
		if(!super.tick(ticking,tickID)) return false;
		if((affected!=null)&&(affected instanceof MOB)&&(activated))
		{
			if(hiding(affected))
			{
				if(!hidden)
				{
					hidden=true;
					sitTime=System.currentTimeMillis();
					affected.recoverEnvStats();
				}
			}
			else
			if(hidden)
			{
				hidden=false;
				sitTime=System.currentTimeMillis();
				affected.recoverEnvStats();
			}
		}
		return true;
	}

	public boolean hiding(Environmental mob)
	{
		if(!(mob instanceof MOB)) return false;
		return Sense.isSitting(mob)&&(((MOB)mob).riding()==null);
	}
	public boolean hiding(MOB mob)
	{
		return Sense.isSitting(mob)&&(mob.riding()==null);
	}

	public boolean okMessage(Environmental myHost, CMMsg msg)
	{
		if((affected instanceof MOB)
		&&(activated)
		&&(msg.amISource((MOB)affected))
		&&(!msg.amITarget(affected))
		&&(msg.targetMinor()==CMMsg.TYP_DAMAGE)
		&&(msg.tool()!=null)
		&&(msg.tool() instanceof Weapon)
		&&(msg.value()>0))
		{
			if(Dice.rollPercentage()<5) helpProfficiency((MOB)affected);
			msg.setValue(msg.value()+(int)Math.round(Util.mul(msg.value(),Util.div(profficiency(),400.0))));
		}
		else
		if((hidden)&&(!hiding(affected)))
		{
			hidden=false;
			sitTime=System.currentTimeMillis();
			affected.recoverEnvStats();
		}
		else
		if((msg.source()==affected)
		&&(hidden)
		&&((Util.bset(msg.sourceMajor(),CMMsg.MASK_SOUND)
			 ||(msg.sourceMinor()==CMMsg.TYP_SPEAK)
			 ||(msg.sourceMinor()==CMMsg.TYP_ENTER)
			 ||(msg.sourceMinor()==CMMsg.TYP_LEAVE)
			 ||(msg.sourceMinor()==CMMsg.TYP_RECALL)))
		 &&(!Util.bset(msg.sourceMajor(),CMMsg.MASK_GENERAL))
		 &&(msg.sourceMinor()!=CMMsg.TYP_EXAMINESOMETHING)
		 &&(msg.sourceMajor()>0))
		{
			hidden=false;
			sitTime=System.currentTimeMillis();
			affected.recoverEnvStats();
		}
		return super.okMessage(myHost,msg);
	}

	public boolean oneOf(int dom)
	{
		for(int i=0;i<landClasses().length;i++)
			if((dom==landClasses()[i].intValue())
			||(landClasses()[i].intValue()<0))
				return true;
		return false;
	}

	public void affectEnvStats(Environmental affected, EnvStats affectableStats)
	{
		super.affectEnvStats(affected,affectableStats);
		if((affected instanceof MOB)
		&&(((MOB)affected).location()!=null)
		&&(oneOf(((MOB)affected).location().domainType())))
		{
			if((hidden)&&((System.currentTimeMillis()-sitTime)>(60*2*1000)))
				affectableStats.setDisposition(affectableStats.disposition()|EnvStats.IS_HIDDEN);
			activated=true;
			affectableStats.setAttackAdjustment(affectableStats.attackAdjustment()+(int)Math.round(15.0*(Util.div(profficiency(),100.0))));
			affectableStats.setArmor(affectableStats.armor()-(int)Math.round(15.0*(Util.div(profficiency(),100.0))));
		}
		else
		{
			activated=false;
			hidden=false;
		}
	}
}