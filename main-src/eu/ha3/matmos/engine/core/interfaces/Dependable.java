package eu.ha3.matmos.engine.core.interfaces;

import java.util.Collection;

/*
 * --filenotes-placeholder
 */

public interface Dependable {
    public Collection<String> getDependencies();
}
