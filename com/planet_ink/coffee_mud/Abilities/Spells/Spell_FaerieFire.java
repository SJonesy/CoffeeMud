package com.planet_ink.coffee_mud.Abilities.Spells;
import com.planet_ink.coffee_mud.core.interfaces.*;
import com.planet_ink.coffee_mud.core.*;
import com.planet_ink.coffee_mud.core.collections.*;
import com.planet_ink.coffee_mud.Abilities.interfaces.*;
import com.planet_ink.coffee_mud.Areas.interfaces.*;
import com.planet_ink.coffee_mud.Behaviors.interfaces.*;
import com.planet_ink.coffee_mud.CharClasses.interfaces.*;
import com.planet_ink.coffee_mud.Commands.interfaces.*;
import com.planet_ink.coffee_mud.Common.interfaces.*;
import com.planet_ink.coffee_mud.Exits.interfaces.*;
import com.planet_ink.coffee_mud.Items.interfaces.*;
import com.planet_ink.coffee_mud.Locales.interfaces.*;
import com.planet_ink.coffee_mud.MOBS.interfaces.*;
import com.planet_ink.coffee_mud.Races.interfaces.*;


import java.util.*;

/*
   Copyright 2000-2014 Bo Zimmerman

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
@SuppressWarnings("rawtypes")
public class Spell_FaerieFire extends Spell
{
	@Override public String ID() { return "Spell_FaerieFire"; }
	public final static String localizedName = CMLib.lang()._("Faerie Fire");
	@Override public String name() { return localizedName; }
	@Override public String displayText(){return "(Faerie Fire)";}
	@Override public int abstractQuality(){return Ability.QUALITY_MALICIOUS;}
	@Override protected int canAffectCode(){return CAN_MOBS;}
	@Override public int classificationCode(){ return Ability.ACODE_SPELL|Ability.DOMAIN_ILLUSION;}


	@Override
	public void unInvoke()
	{
		if(!(affected instanceof MOB))
			return;
		final MOB mob=(MOB)affected;
		if(canBeUninvoked())
			mob.location().show(mob, null, CMMsg.MSG_OK_VISUAL, _("The faerie fire around <S-NAME> fades."));
		super.unInvoke();
	}

	@Override
	public void affectPhyStats(Physical affected, PhyStats affectableStats)
	{
		super.affectPhyStats(affected,affectableStats);

		if((affectableStats.disposition()&PhyStats.IS_INVISIBLE)==PhyStats.IS_INVISIBLE)
			affectableStats.setDisposition(affectableStats.disposition()-PhyStats.IS_INVISIBLE);
		affectableStats.setDisposition(affectableStats.disposition()|PhyStats.IS_GLOWING);
		affectableStats.setDisposition(affectableStats.disposition()|PhyStats.IS_BONUS);
		affectableStats.setArmor(affectableStats.armor()+10);
	}

	@Override
	public int castingQuality(MOB mob, Physical target)
	{
		if(mob!=null)
		{
			if(target instanceof MOB)
			{
				if(!CMLib.flags().isInvisible(target))
					return Ability.QUALITY_INDIFFERENT;
			}
		}
		return super.castingQuality(mob,target);
	}

	@Override
	public boolean invoke(MOB mob, Vector commands, Physical givenTarget, boolean auto, int asLevel)
	{
		final MOB target = getTarget(mob,commands,givenTarget);
		if(target==null) return false;
		Room R=CMLib.map().roomLocation(target);
		if(R==null) R=mob.location();

		// the invoke method for spells receives as
		// parameters the invoker, and the REMAINING
		// command line parameters, divided into words,
		// and added as String objects to a vector.
		if(!super.invoke(mob,commands,givenTarget,auto,asLevel))
			return false;


		final boolean success=proficiencyCheck(mob,0,auto);

		if(success)
		{
			// it worked, so build a copy of this ability,
			// and add it to the affects list of the
			// affected MOB.  Then tell everyone else
			// what happened.

			final CMMsg msg = CMClass.getMsg(mob, target, this, verbalCastCode(mob,target,auto),_("@x1twinkling fire envelopes <T-NAME>.^?",(auto?"A ":"^S<S-NAME> speak(s) and gesture(s) and a ")));
			if(R.okMessage(mob,msg))
			{
				R.send(mob,msg);
				maliciousAffect(mob,target,asLevel,0,-1);
			}
		}
		else
			return beneficialWordsFizzle(mob,null,_("<S-NAME> mutter(s) about a faerie fire, but the spell fizzles."));

		// return whether it worked
		return success;
	}
}
