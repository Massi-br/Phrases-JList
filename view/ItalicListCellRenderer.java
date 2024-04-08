package phrase.view;

import java.awt.Component;

import java.awt.Font;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;

import phrase.model.StdMarkableFilteringListModel;

public class ItalicListCellRenderer implements ListCellRenderer<String> {
	
	private DefaultListCellRenderer deleguate;
	
	public ItalicListCellRenderer(){
		deleguate =new DefaultListCellRenderer();
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends String> list, String value, int index,
			boolean isSelected, boolean cellHasFocus) {
		
		Component result = deleguate.getListCellRendererComponent(list, value,
				index, isSelected, cellHasFocus);
		
		StdMarkableFilteringListModel listModel = (StdMarkableFilteringListModel) list.getModel(); 
		
		if (listModel.isMarked(value)){
			result.setFont(new Font(null, Font.ITALIC, 12));
		}else {
			Font defaultFont = UIManager.getFont("List.font"); 
			result.setFont(defaultFont); 
		}
		return result;
	}
}
