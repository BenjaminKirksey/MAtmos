package eu.ha3.matmos.core;

import java.util.List;

/* x-placeholder */

public class StringListContainer extends Component implements ListContainer {
    private final List<String> list;

    public StringListContainer(String name, List<String> list) {
        super(name);

        this.list = list;
    }

    @Override
    public boolean contains(String element) {
        return this.list.contains(element);
    }
}
