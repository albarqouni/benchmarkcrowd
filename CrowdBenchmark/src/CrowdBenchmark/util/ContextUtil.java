package CrowdBenchmark.util;

import org.eclipse.e4.core.contexts.IEclipseContext;

public class ContextUtil {

	public static void updateContext(IEclipseContext context, String key,
			Object value) {
		if (context.get(key) == null) {
			context.set(key, value);
			context.declareModifiable(key);
		} else
			context.modify(key, value);
	}
}
