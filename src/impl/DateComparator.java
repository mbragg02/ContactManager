package impl;

import java.io.Serializable;
import java.util.Comparator;
import interfaces.Meeting;


public class DateComparator<T> implements Comparator<Meeting>, Serializable{

	private static final long serialVersionUID = -3420681978462640785L;

	@Override
	public int compare(Meeting o1, Meeting o2) {
		// Returns a negative integer if the first argument is less than the second. 
		// zero if they are equal 
		// A positive integer if the first argument is greater than the second.
		
		int n = o1.getDate().compareTo(o2.getDate());
       
		if (n == 0) {
			// if meetings are at the exact same time. Only return a zero if they have the same ID. i.e the same meeting
            return o1.getId() - o2.getId();
        } 
		else {
            return n;
        }
	}

}
