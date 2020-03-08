package logicalcollections;

import java.util.ArrayList;
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

}
