package logicalcollections;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class LogicalSet<T> {
	
	private Set<T> elements = new HashSet<T>();
	private List<Predicate<T>> constraints = new ArrayList<Predicate<T>>();
	
	public boolean contains(T element) {
		if (!elements.contains(element)) {
			elements.add(element);
			for (var predicate : constraints)
				if (!predicate.test(element))
					throw new AssertionError("Logical set constraint violated");
		}
		return true;
	}
	
	public boolean allMatch(Predicate<T> predicate) {
		if (!elements.parallelStream().allMatch(predicate))
			throw new AssertionError("Logical set constraint violated");
		constraints.add(predicate);
		return true;
	}
	
	public static <T> Set<T> matching(Predicate<LogicalSet<T> > predicate) {
		LogicalSet<T> set = new LogicalSet<T>();
		if (!predicate.test(set))
			throw new AssertionError("Logical set constraint violated");
		return set.elements;
	}

}
