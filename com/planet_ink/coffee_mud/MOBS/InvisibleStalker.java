package com.planet_ink.coffee_mud.MOBS;

import java.util.*;
import com.planet_ink.coffee_mud.utils.*;
import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.common.*;
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
public class InvisibleStalker extends StdMOB
{
	public String ID(){return "InvisibleStalker";}
	public InvisibleStalker()
	{
		super();
		Random randomizer = new Random(System.currentTimeMillis());

		Username="an Invisible Stalker";
		setDescription("A shimmering blob of energy.");
		setDisplayText("An invisible stalker hunts here.");
		setAlignment(500);
		setMoney(0);
		baseEnvStats.setWeight(10 + Math.abs(randomizer.nextInt() % 10));


		baseCharStats().setStat(CharStats.INTELLIGENCE,12 + Math.abs(randomizer.nextInt() % 3));
		baseCharStats().setStat(CharStats.STRENGTH,20);
		baseCharStats().setStat(CharStats.DEXTERITY,13);

		baseEnvStats().setDamage(16);
		baseEnvStats().setSpeed(1.0);
		baseEnvStats().setAbility(0);
		baseEnvStats().setLevel(4);
		baseEnvStats().setArmor(0);
		baseEnvStats().setDisposition(baseEnvStats().disposition()|EnvStats.IS_INVISIBLE);

		baseState.setHitPoints(Dice.roll(baseEnvStats().level(),20,baseEnvStats().level()));

		addBehavior(CMClass.getBehavior("Aggressive"));
		addBehavior(CMClass.getBehavior("Mobile"));

		recoverMaxState();
		resetToMaxState();
		recoverEnvStats();
		recoverCharStats();
	}

}
