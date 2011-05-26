package presage.util;
import java.util.*;

public class RandomIterator {

	private Random random;

	ArrayList set;

	public RandomIterator(SortedSet s, int randomseed) {
		set = new ArrayList(s);
		this.random	= new Random(randomseed);

	}

	public RandomIterator(ArrayList s, int randomseed) {
		set = (ArrayList)s.clone();
		this.random	= new Random(randomseed);
	}

	public RandomIterator(SortedSet s, long randomseed) {
		set = new ArrayList(s);
		this.random	= new Random(randomseed);

	}

	public RandomIterator(ArrayList s, long randomseed) {
		set = (ArrayList)s.clone();
		this.random	= new Random(randomseed);
	}

	public void removeElement(Object x){
		set.remove(x);
	}

	public boolean hasNext() {
		if (set.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}
	public Object next() {
		Object[] setArray = set.toArray();
		Object o = setArray[random.nextInt(set.size())];
		set.remove(o);
		return o;
	}
}
