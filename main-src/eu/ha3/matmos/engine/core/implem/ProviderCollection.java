package eu.ha3.matmos.engine.core.implem;

import eu.ha3.matmos.engine.core.implem.abstractions.Provider;
import eu.ha3.matmos.engine.core.interfaces.ReferenceTime;
import eu.ha3.matmos.engine.core.interfaces.SheetCommander;
import eu.ha3.matmos.engine.core.interfaces.SoundRelay;

/*
 * --filenotes-placeholder
 */

public interface ProviderCollection {
    public ReferenceTime getReferenceTime();

    public SoundRelay getSoundRelay();

    public SheetCommander<String> getSheetCommander();

    public Provider<Condition> getCondition();

    public Provider<Junction> getJunction();

    public Provider<Machine> getMachine();

    public Provider<Event> getEvent();

    public Provider<Dynamic> getDynamic();
}
