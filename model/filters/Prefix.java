package phrase.model.filters;

import util.Contract;


/**
 * Implantation des filtres du type : "getValue() est un préfixe de s".
 * @inv <pre>
 *     forall String s : accept(s) == s.startsWith(getValue()) </pre>
 */
public class Prefix extends AbstractFilter {

    // CONSTRUCTEURS

    public Prefix() {
        super("");
    }

    // REQUETES

    @Override
    public boolean accept(String s) {
        Contract.checkCondition(s != null);

        return s.startsWith(getValue());
    }

    @Override
    public String toString() {
        return "Préfixe";
    }
}
