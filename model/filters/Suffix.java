package phrase.model.filters;

import util.Contract;


/**
 * Implantation des filtres du type : "getValue() est un suffixe de s".
 * @inv <pre>
 *     forall String s : accept(s) == s.endsWith(getValue()) </pre>
 */
public class Suffix extends AbstractFilter {

    // CONSTRUCTEURS

    public Suffix() {
        super("");
    }

    // REQUETES

    @Override
    public boolean accept(String s) {
        Contract.checkCondition(s != null);

        return s.endsWith(getValue());
    }

    @Override
    public String toString() {
        return "Suffixe";
    }
}
