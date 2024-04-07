package phrase.model;

import java.beans.PropertyChangeListener;
import java.util.List;


/**
 * Définit les filtres pour les listes de chaînes de caractères.
 * @inv <pre>
 *     getValue() != null
 *     filter(list) != null && filter(list) != list
 *     forall String s :
 *         list.contains(s) && accept(s) <==> filter(list).contains(s) </pre>
 */
public interface Filter {
    
    // CONSTANTES STATIQUES
    
    String PROP_VALUE = "value";

    // REQUETES

    /**
     * Indique si la chaîne s est acceptée par ce filtre lorsqu'il est basé
     *  sur la valeur getValue().
     * @pre <pre>
     *     s != null </pre>
     */
    boolean accept(String s);

    /**
     * Retourne, à partir de list, une nouvelle liste constituée des valeurs
     *  filtrées selon ce filtre lorsqu'il est basé sur la valeur getValue().
     * @pre <pre>
     *     list != null </pre>
     */
    List<String> filter(List<String> list);

    /**
     * Un tableau de tous les PCL enregistrés.
     * @pre
     *     p != null
     */
    PropertyChangeListener[] getPropertyChangeListeners(String p);

    /**
     * La valeur associée à ce filtre.
     */
    String getValue();

    // COMMANDES

    /**
     * Enregistre un PropertyChangeListener.
     * @pre <pre>
     *     p != null && lnr != null </pre>
     * @post <pre>
     *     lnr a été ajouté à la séquence des écouteurs </pre>
     */
    void addPropertyChangeListener(String p, PropertyChangeListener lnr);

    /**
     * Retire un PropertyChangeListener.
     * @pre <pre>
     *     p != null && lnr != null </pre>
     * @post <pre>
     *     lnr a été retiré de la séquence des écouteurs </pre>
     */
    void removePropertyChangeListener(String p, PropertyChangeListener lnr);

    /**
     * Change la valeur associée à ce filtre.
     * @pre <pre>
     *     v != null </pre>
     * @post <pre>
     *     getValue() == v </pre>
     */
    void setValue(String v);
}
