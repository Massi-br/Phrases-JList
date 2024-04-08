package phrase.model;

import java.beans.PropertyChangeEvent;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractListModel;
import util.Contract;



@SuppressWarnings("rawtypes")
public class StdFilteringListModel extends AbstractListModel implements FilteringListModel {
	private static final long serialVersionUID = -8747998338860197510L;

	private List<String> UnfilteredList;
	private List<String> filteredList;
	private Filter f;
	
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
		return filteredList.size();
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
			fireIntervalAdded(element, filteredList.size() -1, filteredList.size() -1);
		}	
	}

	@Override
	public void clearElements() {
		UnfilteredList.clear();
		filteredList.clear();
		
		fireIntervalRemoved(this, 0, Math.max(0, filteredList.size() -1));
	} 

	@Override
	public void setElements(Collection<String> c) {
		Contract.checkCondition(c!=null);
		
		UnfilteredList = new ArrayList<String>(c);
		filteredList.clear();
		if (f == null) {
			filteredList = new ArrayList<String>(c);
		} else {
			for (String s: UnfilteredList) {
				if (f.accept(s)) {
					filteredList.add(s);
				}
			}
		}
		fireContentsChanged(this,0,Math.max(0,filteredList.size() - 1));
	}

	@Override
    public void setFilter(Filter filter) {
	if (f == filter) {
       return;
    }
       f = filter;
       filteredList.clear();
       for (String s : UnfilteredList) {
           if (filter == null || filter.accept(s)) {
               filteredList.add(s);
           }
       }
       if (filter != null) {
           PropertyChangeListener l = new PropertyChangeListener() {
               @Override
               public void propertyChange(PropertyChangeEvent evt) {
                   filteredList.clear();
                   for (String s : UnfilteredList) {
                       if (StdFilteringListModel.this.f.accept(s)) {
                           filteredList.add(s);
                       }
                   }
                   fireContentsChanged(this, 0, Math.max(0, filteredList.size() - 1));
               }
           };
           filter.addPropertyChangeListener(Filter.PROP_VALUE, l);
       } else {
           filteredList = new ArrayList<String>(UnfilteredList);
       }
       fireContentsChanged(this, 0, Math.max(0, filteredList.size() - 1));
   }
	
}
