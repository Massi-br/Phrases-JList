package phrase.gui;

import java.awt.BorderLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import phrase.model.Filter;
import phrase.model.filters.Factor;
import phrase.model.filters.Prefix;
import phrase.model.filters.RegExp;
import phrase.model.filters.Suffix;
import phrase.view.FilteringPane;

public class PhraseAppli {

    // ATTRIBUTS STATIQUES

    private static final String DEFAULT_DIR = System.getProperty("user.home");

    // ATTRIBUTS

    private final JFrame frame;
    private final FilteringPane filteringPane;
    private final JButton loader;
    private final JFileChooser chooser;

    // CONSTRUCTEURS
    
    public PhraseAppli() {
        // VUE
        frame = new JFrame("Filtrage de phrases");
        filteringPane = new FilteringPane(createFilters());
        loader = new JButton("Charger une liste de phrases");
        chooser = new JFileChooser(DEFAULT_DIR);
        placeComponents();
        // CONTROLEUR
        connectControllers();
    }
    
    // COMMANDES
    
    public void display() {
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    /**
     * Utilisation d'un SwingWorker pour lire une à une les lignes de in et
     *  les insérer dans le bean filteringPane.
     * La lecture de in doit se faire sur le thread du SwingWorker.
     * La manipulation de filteringPane doit se faire sur EDT.
     */
    public void populateFilteringPane(final File in) {
        SwingWorker<Void, String> sw = new SwingWorker<Void,String>() {
			@Override
			protected Void doInBackground() throws Exception {
				BufferedReader r = new BufferedReader(new FileReader(in));
				try {
					String s = r.readLine();
					while (s !=null) {
						publish(s);
						delayAction();
						s= r.readLine();
					}
				} finally {
					r.close();
				}
				return null;
			}
			@Override
			protected void process(List<String> l) {
				for (String s: l) {
					filteringPane.addElement(s);
				}
			}
        	
        };
        sw.execute();
    }


    // OUTILS
    

    
    private void placeComponents() {
        JPanel p = new JPanel();
        { //--
            p.add(loader);
        } //--
        frame.add(p, BorderLayout.NORTH);
        
        frame.add(filteringPane, BorderLayout.CENTER);
    }

    private void connectControllers() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        loader.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = chooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File f = chooser.getSelectedFile();
                    populateFilteringPane(f);
                }
            }
        });
    }
    
    private static Filter[] createFilters() {
        return new Filter[] {
                new Prefix(), new Factor(), new Suffix(), new RegExp()
        };
    }
    
    private void delayAction() {
        final int delay = 20;
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            // rien, on quitte
        }
    }
}
