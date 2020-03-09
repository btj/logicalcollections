package logicalcollections;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class LogicalList {
	
	public static <T> List<T> plus(List<T> list, T element) {
		List<T> result = new ArrayList<T>(list);
		result.add(element);
		return result;
	}

	public static <T> List<T> minus(List<T> list, T element) {
		List<T> result = new ArrayList<T>(list);
		result.remove(element);
		return result;
	}
	
	public static <T> boolean distinct(List<T> list) {
		var elements = new HashSet<T>();
		for (var element : list) {
			if (elements.contains(element))
				return false;
			elements.add(element);
		}
		return true;
	}

}
