package com.planet_ink.coffee_mud.Commands;
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
public class Reply extends StdCommand
{
	public Reply(){}

	private String[] access={"REPLY","REP","RE"};
	public String[] getAccessWords(){return access;}
	public boolean execute(MOB mob, Vector commands)
		throws java.io.IOException
	{
		if(mob==null) return false;
		PlayerStats pstats=mob.playerStats();
		if(pstats==null) return false;
		if(pstats.replyTo()==null)
		{
			mob.tell("No one has told you anything yet!");
			return false;
		}
		if((pstats.replyTo().Name().indexOf("@")<0)
		&&(!pstats.replyTo().isMonster())
		&&((CMMap.getPlayer(pstats.replyTo().Name())==null)||(!Sense.isInTheGame(pstats.replyTo()))))
		{
			mob.tell(pstats.replyTo().Name()+" is no longer logged in.");
			return false;
		}
		if(Util.combine(commands,1).length()==0)
		{
			mob.tell("Tell '"+pstats.replyTo().Name()+" what?");
			return false;
		}
		CommonMsgs.say(mob,pstats.replyTo(),Util.combine(commands,1),true,!mob.location().isInhabitant(pstats.replyTo()));
		if((pstats.replyTo().session()!=null)
		&&(pstats.replyTo().session().afkFlag()))
			mob.tell("Note: "+pstats.replyTo().name()+" is AFK at the moment.");
		return false;
	}
	public int ticksToExecute(){return 1;}
	public boolean canBeOrdered(){return false;}

	public int compareTo(Object o){ return CMClass.classID(this).compareToIgnoreCase(CMClass.classID(o));}
}
