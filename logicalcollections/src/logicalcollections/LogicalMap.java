package logicalcollections;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.*;

public class LogicalMap<K, V> {
	
	public class KeySet {
		
		public boolean allMatch(Predicate<K> predicate) {
			predicates.add(predicate);
			int keysCount = keysList.size();
			for (int i = 0; i < keysCount; i++)
				if (!predicate.test(keysList.get(i)))
					throw new AssertionError("Logical map constraint violated");
			return true;
		}
		
	}
	
	private Map<K, V> entries = new HashMap<K, V>();
	private final List<K> keysList = new ArrayList<K>();
	private final List<Predicate<K>> predicates = new ArrayList<>();
	private final KeySet keySet = new KeySet();
	
	public KeySet keySet() { return keySet; }
	
	private void addEntry(K key, V value) {
		entries.put(key, value);
		keysList.add(key);
		int predicatesCount = predicates.size();
		for (int i = 0; i < predicatesCount; i++)
			if (!predicates.get(i).test(key))
				throw new AssertionError("Logical map constraint violated");
	}
	
	public boolean containsKey(K key) {
		if (!entries.containsKey(key))
			addEntry(key, null);
		return true;
	}
	
	public boolean containsEntry(K key, V value) {
		if (value == null)
			throw new IllegalArgumentException("null values are not supported");
		if (!entries.containsKey(key))
			addEntry(key, value);
		else {
			V oldValue = entries.get(key);
			if (oldValue == null)
				entries.put(key, value);
			else if (!oldValue.equals(value))
				throw new AssertionError("Conflicting values for same key");
		}
		return true;
	}
	
	public static <K, V> Map<K, V> matching(Predicate<LogicalMap<K, V>> predicate) {
		var map = new LogicalMap<K, V>();
		if (!predicate.test(map))
			throw new AssertionError("Logical map constraint violated");
		var result = map.entries;
		map.entries = null;
		return result;
	}
	
	public static <K, V> boolean extendsExcept(Map<K, V> map1, Map<K, V> map2, K... keys) {
		HashSet<K> keysSet = new HashSet<>(Arrays.asList(keys));
		for (K key : map2.keySet()) {
			if (!keysSet.contains(key)) {
				if (!map1.containsKey(key))
					return false;
				if (!map1.get(key).equals(map2.get(key)))
					return false;
			}
		}
		return true;
	}
	
	public static <K, V> boolean equalsExcept(Map<K, V> map1, Map<K, V> map2, K... keys) {
		return extendsExcept(map1, map2, keys) && extendsExcept(map2, map1, keys);
	}
	
	public static <K, V> boolean allEntriesMatch(Map<K, V> map, BiPredicate<K, V> predicate) {
		return map.keySet().stream().allMatch(k -> predicate.test(k, map.get(k)));
	}
	
}
