package ejercicios;

import aux.ProgramDriver;
import ejercicios.amigosdeamigos.AmigosJob;

public class Driver extends ProgramDriver {

	public Driver() throws Throwable {
		super();
		addClass("sort-pangool", AmigosJob.class, "Just write the input as output (text files)");
		addClass("sort-hadoop", AmigosJob.class, "Just write the input as output (text files)");
	}

	public static void main(String[] args) throws Throwable {
		Driver driver = new Driver();
		driver.driver(args);
		System.exit(0);
	}
}
