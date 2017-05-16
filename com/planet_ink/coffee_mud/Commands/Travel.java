package com.planet_ink.coffee_mud.Commands;
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
import com.planet_ink.coffee_mud.Libraries.interfaces.*;
import com.planet_ink.coffee_mud.Locales.interfaces.*;
import com.planet_ink.coffee_mud.MOBS.interfaces.*;
import com.planet_ink.coffee_mud.Races.interfaces.*;

import java.util.List;
import java.util.Vector;

/**
 * Created by ABeaumont on 05/16/2017.
 */
public class Travel extends Go {

    public Travel() {}

    private final String[] access=I(new String[]{"TRAVEL","TR","TRA"});
    @Override public String[] getAccessWords(){return access;}
    @Override
    public boolean execute(MOB mob, List<String> commands, int metaFlags) throws java.io.IOException    {
        Vector<String> origCmds=new XVector<String>(commands);
        if (commands.size() < 2) {
            CMLib.commands().doCommandFail(mob, origCmds,L("Travel in which direction?"));
            return false;
        }

        if(!standIfNecessary(mob,metaFlags, true))
            return false;

        final String dirArg = commands.get(commands.size()-1);
        final int direction=CMLib.directions().getGoodDirectionCode(dirArg);
        if(direction<0)
        {
            mob.tell(L("You have failed to specify a direction.  Try @x1.\n\r",Directions.LETTERS()));
            mob.location().showOthers(mob,null,CMMsg.MSG_OK_ACTION,L("<S-NAME> seems ready to travel, but looks around confused instead."));
            return false;
        }
        /** TODO:   Implement check for DOMAIN_OUTDOORS_ROAD in direction of travel, then loop movement as long as
         *  TODO:        a) At least one other exit other than direction arrived from of type DOMAIN_OUTDOORS_ROAD
         *  TODO:        b) Not more than one other exit other than direction arrived from of type DOMAIN_OUTDOORS_ROAD
         *  TODO:        c) No adjacent exits of type DOMAIN_OUTDOORS_AREA_CONNECTION
         *  TODO:        d) No failure due to lack of movement points or combat.
         *  TODO:   If any of these criteria fail, then return false (halt).
         */

        return false;
    }
    @Override public boolean canBeOrdered(){return true;}

    @Override
    public boolean securityCheck(MOB mob)    {
        return (mob==null) || (mob.isMonster()) || (mob.location()==null)
                || ((!(mob.location() instanceof BoardableShip)) && (!(mob.location().getArea() instanceof BoardableShip)));
    }

}
