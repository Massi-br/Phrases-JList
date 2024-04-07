package phrase.view;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import phrase.model.Filter;
import phrase.model.FilteringListModel;
import phrase.model.StdFilteringListModel;
import util.Contract;

public class FilteringPane extends JPanel {

    // ATTRIBUTS

    private final MarkableFilteringListModel listModel;
    private final JComboBox allFilters;
    private final JTextField filterValue;
    private final JList filteringList;

    // CONSTRUCTEURS

    public FilteringPane(Filter[] filters) {
        super(new BorderLayout());

        // MODELE
        listModel = new StandardFilteringListModel();
        // VUE
        allFilters = new JComboBox(new DefaultComboBoxModel(filters));
        filterValue = new JTextField();
        filteringList = new JList(listModel);
        placeComponents();
        // CONTROLEUR
        connectControllers();
        
        setCurrentFilterFromSelectedItem();
    }

    // REQUETES

    public JComboBox getCombo() {
        return allFilters;
    }

    public JTextField getTextField() {
        return filterValue;
    }

    public JList getList() {
        return filteringList;
    }

    // COMMANDES

    /**
     * Ajoute un élément à la fin du modèle de getList().
     * @pre <pre>
     *     element != null </pre>
     * @post <pre>
     *     Let m ::= (FilteringListModel) getList().getModel()
     *     m.getUnfilteredSize() == old m.getUnfilteredSize() + 1
     *     m.getUnfilteredElementAt(m.getUnfilteredSize() - 1) == element </pre>
     */
    public void addElement(String element) {
        Contract.checkCondition(element != null);

        listModel.addElement(element);
    }
    
    /**
     * Supprime tous les éléments du modèle de getList().
     */
    public void clear() {
        listModel.clearElements();
    }

    /**
     * Remplace les éléments du modèle de getList() par ceux de c.
     * @pre <pre>
     *     c != null </pre>
     * @post <pre>
     *     Let m ::= (FilteringListModel) getList().getModel()
     *     m.getUnfilteredSize() == c.getSize()
     *     forall s in c :
     *         exists int i, 0 <= i < m.getUnfilteredSize() :
     *             m.getUnfilteredElementAt(i) == s </pre>
     */
    public void setElements(Collection<String> c) {
        Contract.checkCondition(c != null);

        listModel.setElements(c);
    }

    // OUTILS

    private void placeComponents() {
        JPanel p = new JPanel(new BorderLayout());
        { //--
            p.setBorder(BorderFactory.createTitledBorder("Recherche"));
            p.add(allFilters, BorderLayout.WEST);
            p.add(filterValue, BorderLayout.CENTER);
        } //--
        add(p, BorderLayout.NORTH);

        JScrollPane jsp = new JScrollPane(filteringList);
        { //--
            jsp.setBorder(BorderFactory.createTitledBorder("Résultat"));
        } //--
        add(jsp, BorderLayout.CENTER);
    }

    private void connectControllers() {
        /*****************/
        /** A COMPLETER **/
        /*****************/
    }

    private void setCurrentFilterFromSelectedItem() {
        /*****************/
        /** A COMPLETER **/
        /*****************/
    }
}
