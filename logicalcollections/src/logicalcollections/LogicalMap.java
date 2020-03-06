package logicalcollections;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.*;

public class LogicalMap<K, V> {
	
	private Map<K, V> entries = new HashMap<K, V>();
	private List<Predicate<Map.Entry<K, V>>> constraints = new ArrayList<>();
	
	public boolean containsEntry(K key, V value) {
		if (!entries.containsKey(key)) {
			entries.put(key, value);
			for (var constraint : constraints) {
				if (!constraint.test(new AbstractMap.SimpleEntry<K, V>(key, value)))
					throw new AssertionError("Logical map constraint violated");
			}
			return true;
		} else if (!entries.get(key).equals(value))
			throw new AssertionError("Logical map: conflicting value for existing entry");
		return true;
	}
	
	public boolean allMatch(Predicate<Map.Entry<K, V>> constraint) {
		if (!entries.entrySet().stream().allMatch(constraint))
			throw new AssertionError("Logical map constraint violated");
		constraints.add(constraint);
		return true;
	}
	
	public static <K, V> Map<K, V> matching(Predicate<LogicalMap<K, V>> predicate) {
		var map = new LogicalMap<K, V>();
		if (!predicate.test(map))
			throw new AssertionError("Logical map constraint violated");
		return map.entries;
	}

}
