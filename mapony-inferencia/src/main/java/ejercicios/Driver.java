package ejercicios;

import proyecto.utad.mapony.Mapony;
import aux.ProgramDriver;

public class Driver extends ProgramDriver {

	
	
	public Driver() throws Throwable {
		super();
		addClass("sort-hadoop", Mapony.class, "Just write the input as output (text files)");
	}

	public static void main(String[] args) throws Throwable {
		Driver driver = new Driver();
		driver.driver(args);
		System.exit(0);
	}
}
