package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author KingFaris10
 */
public class BiMap<K, V> extends HashMap<K, V> {

	public V get(Object key, V defValue) {
		Object value = this.get(key);
		return value != null ? (V) value : defValue;
	}

	public List<K> getKey(V value) {
		List<K> keyList = new ArrayList<K>();
		Set<Map.Entry<K, V>> entrySet = this.entrySet();
		for (Map.Entry<K, V> mapEntry : entrySet) {
			if ((mapEntry.getValue() == null && value == null) || (value != null && value.equals(mapEntry.getValue()))) {
				keyList.add(mapEntry.getKey());
			}
		}
		if (keyList.isEmpty()) keyList.add(null);
		return keyList;
	}

	public void removeAll(Collection<K> keys) {
		if (keys != null) {
			for (K key : keys) this.remove(key);
		}
	}

	public void removeAllValues(Collection<V> values) {
		if (values != null) {
			Set<Map.Entry<K, V>> entrySet = this.entrySet();
			List<K> keysToRemove = new ArrayList<K>();
			for (Map.Entry<K, V> entry : entrySet) {
				if (values.contains(entry.getValue())) {
					keysToRemove.add(entry.getKey());
				}
			}
			this.removeAll(keysToRemove);
		}
	}

	public void removeValue(V value) {
		this.removeAll(this.getKey(value));
	}

}
