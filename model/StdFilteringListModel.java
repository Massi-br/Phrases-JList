package phrase.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractListModel;
import util.Contract;



@SuppressWarnings("rawtypes")
public class StdFilteringListModel extends AbstractListModel implements FilteringListModel {
	private static final long serialVersionUID = -8747998338860197510L;

	private List<String> UnfilteredList;
	private List<String> filteredList;
	private Filter f;
	private PropertyChangeSupport pcs;
	
	public StdFilteringListModel() {
		UnfilteredList =new ArrayList<String>(); 
		filteredList=new ArrayList<String>();
		f=null;
	}
	
	// -------------------------------------------  REQUETES  -----------------------------------------------//
	@Override
	public String getElementAt(int i) {
		Contract.checkCondition(i >= 0 && i <getSize());
		
		return filteredList.get(i);
	}

	@Override
	public Filter getFilter() {
		return f;
	}

	@Override
	public int getSize() {
		return UnfilteredList.size();
	}

	@Override
	public String getUnfilteredElementAt(int i) {
		Contract.checkCondition(i >=0 && i< getUnfilteredSize());
		
		return UnfilteredList.get(i);
	}

	@Override
	public int getUnfilteredSize() {
		
		return UnfilteredList.size();
	}

	
	// -------------------------------------------  COMMANDES  -----------------------------------------------//
	
	@Override
	public void addElement(String element) {
		Contract.checkCondition(element != null);
		
		UnfilteredList.add(element);
		if (f == null || f.accept(element)){
			filteredList.add(element);
			fireIntervalAdded(filteredList, getSize()-1, getSize()-1);
		}	
	}

	@Override
	public void clearElements() {
		UnfilteredList.clear();
		filteredList.clear();
		
		fireIntervalRemoved(filteredList, 0, Math.max(0, getSize()-1));
	} 

	@Override
	public void setElements(Collection<String> c) {
		Contract.checkCondition(c != null);
		
		UnfilteredList.clear(); 
		UnfilteredList.addAll(c);
		updateFilter();
	}

	@Override
	public void setFilter(Filter filter) {
		if (f == filter) {
			return;
		}
		f = filter;
		f.addPropertyChangeListener(Filter.PROP_VALUE, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateFilter();				
			}
		} );
	}
	
	//-----------------------------------------------------OUTILS---------------------------------//
	private void updateFilter() {
		filteredList.clear();
		if (f == null) {
			filteredList.addAll(UnfilteredList);
		}else {
			filteredList.addAll(f.filter(UnfilteredList));
		}	
		fireContentsChanged(filteredList, 0,Math.max(0, getSize()-1));
	}
	
}
