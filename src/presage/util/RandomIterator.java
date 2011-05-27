package presage.util;
import java.util.*;

public class RandomIterator {

	private Random random;

	ArrayList<String> set;

	public RandomIterator(SortedSet<String> s, int randomseed) {
		set = new ArrayList<String>(s);
		this.random	= new Random(randomseed);

	}

	@SuppressWarnings("unchecked")
	public RandomIterator(ArrayList<String> s, int randomseed) {
		set = (ArrayList<String>)s.clone();
		this.random	= new Random(randomseed);
	}

	public RandomIterator(SortedSet<String> s, long randomseed) {
		set = new ArrayList<String>(s);
		this.random	= new Random(randomseed);

	}

	@SuppressWarnings("unchecked")
	public RandomIterator(ArrayList<String> s, long randomseed) {
		set = (ArrayList<String>)s.clone();
		this.random	= new Random(randomseed);
	}

	public void removeElement(String x){
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
