package com.calypso.tk.util.cache;

/**
 * Created by IntelliJ IDEA.
 * User: attalep
 * Date: 12/11/11
 * Time: 14:19
 */
import com.calypso.tk.core.VersionedObject;

public class CacheUtil {

	private CacheUtil() {
	}

	public static VersionedObject peek(Cache cache, Object obj, int i) {
		try {
			VersionedObject versionedobject = (VersionedObject) cache.peek(obj);
			if (versionedobject != null && versionedobject.getVersion() == i)
				return versionedobject;
			else
				return null;
		} catch (ClassCastException e) {
//            System.out.println(cache.getName() + " " + obj.toString() + " " + e.getMessage());
		}

		return null;
	}
}
