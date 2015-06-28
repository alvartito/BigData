package util.driver;

import proyecto.utad.mapony.csv.MaponyCsvJob;
import proyecto.utad.mapony.groupNear.MaponyGroupNearJob;
import proyecto.utad.mapony.inferencia.MaponyInferenciaJob;
import proyecto.utad.mapony.pruebas.MaponyPruebasJob;

public class Driver extends ProgramDriver {
	
	public Driver() throws Throwable {
		super();
		addClass("Mapony CSV", MaponyCsvJob.class, "Exportar a csv");
		addClass("Mapony Group By Near Places", MaponyGroupNearJob.class, "GroupByNear ");
		addClass("Mapony Inferencia (carga en ES)", MaponyInferenciaJob.class, "Inferencia (carga en ES)");
		addClass("Mapony Pruebas", MaponyPruebasJob.class, "Pruebas");
	}

	public static void main(String[] args) throws Throwable {
		Driver driver = new Driver();
		driver.driver(args);
		System.exit(0);
	}
}
