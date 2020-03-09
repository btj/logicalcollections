package logicalcollections;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class LogicalSet<T> {
	
	private Set<T> elements = new HashSet<T>();
	private List<T> elementsList = new ArrayList<T>();
	private List<Predicate<T>> predicates = new ArrayList<Predicate<T>>();
	
	public boolean contains(T element) {
		if (!elements.contains(element)) {
			elements.add(element);
			elementsList.add(element);
			int predicatesCount = predicates.size();
			for (int i = 0; i < predicatesCount; i++)
				if (!predicates.get(i).test(element))
					throw new AssertionError("Logical set constraint violated");
		}
		return true;
	}
	
	public boolean containsAll(Iterable<T> objects) {
		for (var object : objects)
			contains(object);
		return true;
	}
	
	public boolean allMatch(Predicate<T> predicate) {
		predicates.add(predicate);
		int elementsCount = elementsList.size();
		for (int i = 0; i < elementsCount; i++)
			if (!predicate.test(elementsList.get(i)))
				throw new AssertionError("Logical set constraint violated");
		return true;
	}
	
	public static <T> Set<T> matching(Predicate<LogicalSet<T> > predicate) {
		LogicalSet<T> set = new LogicalSet<T>();
		if (!predicate.test(set))
			throw new AssertionError("Logical set constraint violated");
		var elements = set.elements;
		set.elements = null;
		return elements;
	}
	
	public static <T> Set<T> plus(Set<T> set, T element) {
		HashSet<T> result = new HashSet(set);
		result.add(element);
		return result;
	}
}
