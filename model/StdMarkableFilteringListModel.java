package phrase.model;

import java.util.HashSet;
import java.util.Set;

import util.Contract;

public class StdMarkableFilteringListModel extends StdFilteringListModel {
	private static final long serialVersionUID = -9126677134001307278L;
	
	
	private Set<String> markedElements;
	
	public StdMarkableFilteringListModel() {
		super();
		markedElements = new HashSet<String>();
	}
	
	public boolean isMarked(String s) {
		Contract.checkCondition(s != null);
		return markedElements.contains(s);
	}
	
	public void toggleMark(String s) {
		Contract.checkCondition(s!=null);
		if (isMarked(s)) {
			markedElements.remove(s);
		}else {
			markedElements.add(s);
		}
	}
	
	
	
	
	
	
	

}
