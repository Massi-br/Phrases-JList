package phrase.model.filters;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import phrase.model.Filter;
import util.Contract;


/**
 * Implantation partielle des filtres.
 */
abstract class AbstractFilter implements Filter {

    // ATTRIBUTS

    private final PropertyChangeSupport propSupport;
    private String value;

    // CONSTRUCTEURS

    AbstractFilter(String initValue) {
        Contract.checkCondition(initValue != null);

        value = initValue;
        propSupport = new PropertyChangeSupport(this);
    }

    // REQUETES

    @Override
    public List<String> filter(List<String> list) {
        Contract.checkCondition(list != null);

        List<String> result = new ArrayList<String>();
        for (String s : list) {
            if (accept(s)) {
                result.add(s);
            }
        }
        return result;
    }

    @Override
    public PropertyChangeListener[] getPropertyChangeListeners(String p) {
        Contract.checkCondition(p != null);

        return propSupport.getPropertyChangeListeners(p);
    }

    @Override
    public String getValue() {
        return value;
    }

    // COMMANDES

    @Override
    public void addPropertyChangeListener(String p,
            PropertyChangeListener lnr) {
        Contract.checkCondition(p != null && lnr != null);

        propSupport.addPropertyChangeListener(p, lnr);
    }

    @Override
    public void removePropertyChangeListener(String p,
            PropertyChangeListener lnr) {
        Contract.checkCondition(p != null && lnr != null);

        propSupport.removePropertyChangeListener(p, lnr);
    }

    @Override
    public void setValue(String v) {
        Contract.checkCondition(v != null);

        String oldValue = getValue();
        value = v;
        propSupport.firePropertyChange(PROP_VALUE, oldValue, v);
    }
}
