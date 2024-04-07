package phrase;

import javax.swing.SwingUtilities;

import phrase.gui.PhraseAppli;

public final class Main {

    private Main() {
        // rien ici
    }

    // POINTS D'ENTREE

    public static void main(String[] args) {
        SwingUtilities.invokeLater(
            new Runnable() {
                @Override
                public void run() {
                    new PhraseAppli().display();
                }
            }
        );
    }
}
