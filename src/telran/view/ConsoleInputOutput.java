package telran.view;

import java.io.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class ConsoleInputOutput implements InputOutput{
	private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	private PrintStream output = System.out;

	public String readString(String prompt) {
		writeLine(prompt);
		try {
			String res = input.readLine();
			return res;
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	public void write(Object obj) {
		output.print(obj);
	}
}
