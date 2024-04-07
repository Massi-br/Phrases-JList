package phrase.model;

import java.util.Collection;

import javax.swing.ListModel;


/**
 * Modèles de listes filtrants.
 * Un tel modèle est constitué de deux listes :
 * <ol>
 *   <li>Une liste complète comprenant tous les éléments.</li>
 *   <li>Une liste filtrée comprenant parmi les éléments initiaux ceux qui
 *       sont acceptés par le filtre.</li>
 * </ol>
 * Le modèle, tel qu'il est présenté par ListModel, donne accès à la liste
 *  filtrée ; on rajoute ici les méthodes nécessaires pour accéder à la liste
 *  complète.
 * @inv <pre>
 *     0 <= getSize() <= getUnfilteredSize()
 *     getFilter() == null ==> getSize() == getUnfilteredSize()
 *     forall int i, 0 <= i < getUnfilteredSize() :
 *         getFilter().accept(getUnfilteredElementAt(i))
 *             <==> exists int j, 0 <= j < getSize() :
 *                      getElementAt(j) == getUnfilteredElementAt(i) </pre>
 */
@SuppressWarnings("rawtypes")
public interface FilteringListModel extends ListModel {
    
    // REQUETES

    /**
     * Le ième élément de la liste filtrée.
     * @pre <pre>
     *     0 <= i < getSize() </pre>
     */
    @Override
    String getElementAt(int i);

    /**
     * Le filtre actuel de ce modèle filtrant.
     */
    Filter getFilter();

    /**
     * Le nombre total d'éléments de la liste filtrée.
     */
    @Override
    int getSize();

    /**
     * Le ième élément de la liste complète.
     * @pre <pre>
     *     0 <= i < getUnfilteredSize() </pre>
     */
    String getUnfilteredElementAt(int i);

    /**
     * Le nombre total d'éléments de la liste complète.
     */
    int getUnfilteredSize();

    // COMMANDES

    /**
     * Ajoute un élément dans le modèle, puis notifie ses observateurs.
     * @pre <pre>
     *     element != null </pre>
     * @post <pre>
     *     getUnfilteredSize() == old getUnfilteredSize() + 1
     *     getUnfilteredElementAt(getUnfilteredSize() - 1) == element </pre>
     */
    void addElement(String element);
    
    /**
     * Vide le modèle, puis notifie ses observateurs.
     * @post <pre>
     *     getUnfilteredSize() == 0 </pre>
     */
    void clearElements();

    /**
     * Réinitialise le modèle avec tous les éléments de c, puis notifie ses
     *  observateurs.
     * @pre <pre>
     *     c != null </pre>
     * @post <pre>
     *     getUnfilteredSize() == c.getSize()
     *     forall String s :
     *         c.contains(s)
     *             <==> exists int i, 0 <= i < getUnfilteredSize() :
     *                     getUnfilteredElementAt(i) == s </pre>
     */
    void setElements(Collection<String> c);

    /**
     * Fixe le filtre de ce modèle, puis notifie ses observateurs.
     * @post <pre>
     *     getFilter() == filter </pre>
     */
    void setFilter(Filter filter);
}
