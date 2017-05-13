package com.planet_ink.coffee_mud.Abilities.Properties;

import com.planet_ink.coffee_mud.Abilities.interfaces.*;

public class Prop_AreaMap extends Property {
    @Override public String ID() { return "Prop_AreaMap"; }
    @Override public String name(){ return "Area displays map"; }
    @Override protected int canAffectCode(){return Ability.CAN_AREAS; }
    @Override public String accountForYourself() { return "AreaMap";  }
}
