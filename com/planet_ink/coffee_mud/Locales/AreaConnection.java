package com.planet_ink.coffee_mud.Locales;
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

import java.util.*;

/*
    Twaffo Smells
*/
public class AreaConnection extends StdRoom
{
    @Override
    public String ID()
    {
        return "AreaConnection";
    }

    public AreaConnection()
    {
        super();
        name="An Area Connecting Room";
        basePhyStats.setWeight(1);
        recoverPhyStats();
    }

    @Override
    public int domainType()
    {
        return Room.DOMAIN_OUTDOORS_AREA_CONNECTION;
    }

    public static final Integer[] resourceList={};
    public static final List<Integer> roomResources=new Vector<Integer>(Arrays.asList(resourceList));

}
