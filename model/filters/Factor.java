package phrase.model.filters;

import util.Contract;


/**
 * Implantation des filtres du type : "getValue() est un facteur de s".
 * @inv <pre>
 *     forall String s : accept(s) == s.contains(getValue()) </pre>
 */
public class Factor extends AbstractFilter {

    // CONSTRUCTEURS

    public Factor() {
        super("");
    }

    // REQUETES

    @Override
    public boolean accept(String s) {
        Contract.checkCondition(s != null);

        return s.contains(getValue());
    }

    @Override
    public String toString() {
        return "Facteur";
    }
}
