package telran.view;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public interface InputOutput {
	public String readString(String prompt);

	public void write(Object obj);

	default void writeLine(Object obj) {
		write(obj + "\n");
	}

	default <T> T readObject(String prompt, String errorPrompt, Function<String, T> mapper) {
		boolean running = false;
		T res = null;
		do {
			running = false;
			String resInput = readString(prompt);
			try {
				res = mapper.apply(resInput);

			} catch (Exception e) {
				writeLine(errorPrompt + ": " + e.getMessage());
				running = true;
			}

		} while (running);
		return res;
	}

	default int readInt(String prompt, String errorPrompt) {
		return readObject(prompt, errorPrompt, Integer::parseInt);
	}

	default int readInt(String prompt, String errorPrompt, int min, int max) {
		return readObject(String.format("%s[%d - %d] ", prompt, min, max), errorPrompt,
				string -> {

			int res = Integer.parseInt(string);
			if (res < min) {
				throw new IllegalArgumentException("must be not less than " + min);
			}
			if (res > max) {
				throw new IllegalArgumentException("must be not greater than " + max);
			}
			return res;

		});
	}

	default long readLong(String prompt, String errorPrompt) {
		
		return readObject(prompt, errorPrompt, Long::parseLong);
	}

	default long readLong(String prompt, String errorPrompt, long min, long max) {
		return readObject(String.format("%s[%d - %d] ", prompt, min, max), errorPrompt,
				string -> {

			long res = Long.parseLong(string);
			if (res < min) {
				throw new IllegalArgumentException("must be not less than " + min);
			}
			if (res > max) {
				throw new IllegalArgumentException("must be not greater than " + max);
			}
			return res;

		});
	}

	default String readString(String prompt, String errorPrompt, Predicate<String> predicate) {
		
		return readObject(prompt, errorPrompt, string -> {
			if(!predicate.test(string)) {
				throw new IllegalArgumentException("");
			}
			return string;
		});
	}

	default String readString(String prompt, String errorPrompt,
			Set<String> options) {
		
		return readString(prompt, errorPrompt, options::contains);
	}

	default LocalDate readDate(String prompt, String errorPrompt) {
		
		return readObject(prompt, errorPrompt, LocalDate::parse);
	}

	default LocalDate readDate(String prompt, String errorPrompt,
			LocalDate from, LocalDate to) {
		
		return readObject(prompt, errorPrompt, string -> {
			LocalDate res = LocalDate.parse(string);
			if(res.isBefore(from) || res.isAfter(to)) {
				throw new IllegalArgumentException
				(String.format("Date should be in the range from %s to %s", from, to));
			}
			return res;
		});
	}

	default double readDouble(String prompt, String errorPrompt) {
		
		return readObject(prompt, errorPrompt, Double::parseDouble);
	}
}
