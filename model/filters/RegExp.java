package phrase.model.filters;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import util.Contract;


/**
 * Implantation des filtres du type :
 *  "getValue() est une regexp reconnaissant s".
 * @inv <pre>
 *     forall String s :
 *         accept(s) == l'automate associé à getValue() reconnaît s </pre>
 */
public class RegExp extends AbstractFilter {

    // ATTRIBUTS STATIQUES

    private static final String DEFAULT_EXP = "";
    private static final Pattern DEFAULT_PATTERN = Pattern.compile(DEFAULT_EXP);
    
    // ATTRIBUTS
    
    private Pattern pattern;

    // CONSTRUCTEURS

    public RegExp() {
        super(DEFAULT_EXP);
        pattern = DEFAULT_PATTERN;
    }

    // REQUETES

    @Override
    public boolean accept(String s) {
        Contract.checkCondition(s != null);

        return pattern.matcher(s).find();
    }

    @Override
    public String toString() {
        return "RegExp";
    }

    // COMMANDES

    @Override
    public void setValue(String v) {
        Contract.checkCondition(v != null);

        if (v.equals(pattern.pattern())) {
            return;
        }
        
        try {
            pattern = Pattern.compile(v);
        } catch (PatternSyntaxException e) {
            v = DEFAULT_EXP;
            pattern = DEFAULT_PATTERN;
        }
        super.setValue(v);
    }
}
