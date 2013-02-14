package CrowdBenchmark.validators;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

public class NumericValidator implements IValidator {

	@Override
	public IStatus validate(Object value) {
		if (value == null) {
			return ValidationStatus.ok();
		}
		if (value instanceof String) {
			String text = (String) value;
			if (text.trim().isEmpty() || text.matches("\\d*")) {
				return ValidationStatus.ok();
			}
		}
		if (value instanceof Integer) {
			String s = String.valueOf(value);
			if (s.matches("\\d*")) {
				return ValidationStatus.ok();
			}
		}
		return ValidationStatus.error("Not a number");
	}
}
