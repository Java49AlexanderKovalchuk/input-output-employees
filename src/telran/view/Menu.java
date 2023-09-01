package telran.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Menu implements Item {
	private static final int N_SYMBOLS = 20;
	String name;
	ArrayList<Item> items;
	public Menu(String name, ArrayList<Item> items) {
		this.name = name;
		this.items = items;
	}
	public Menu (String name, Item ...items) {
		this(name, new ArrayList<>(Arrays.asList(items)));
	}
	@Override
	public String displayName() {
		
		return name;
	}

	@Override
	public void perform(InputOutput io) {
		boolean running = true;
		do {
			try {
				displayTitle(io);
				displayItemNames(io);
				int itemNumber = io.readInt("Enter item number", "Wrong item number",
						1, items.size());
				Item item = items.get(itemNumber - 1);
				item.perform(io);
				running = !item.isExit();
			} catch (Throwable e) {
				io.writeLine(e.getMessage());
			}
		} while(running);

	}

	private void displayItemNames(InputOutput io) {
		IntStream.rangeClosed(1, items.size())
		.forEach(i -> io.writeLine(i + ". " + items.get(i - 1).displayName()));
		
	}
	private void displayTitle(InputOutput io) {
		io.write("*".repeat(N_SYMBOLS));
		io.write(name);
		io.writeLine("*".repeat(N_SYMBOLS));
		
	}
	@Override
	public boolean isExit() {
		
		return false;
	}

}
