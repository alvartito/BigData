package util.driver;

import proyecto.utad.mapony.csv.MaponyCsvJob;
import proyecto.utad.mapony.groupNear.MaponyGroupNearJob;
import proyecto.utad.mapony.inferencia.MaponyInferenciaJob;
import proyecto.utad.mapony.pruebas.MaponyPruebasJob;

public class Driver extends ProgramDriver {
	
	public Driver() throws Throwable {
		super();
		addClass("csv", MaponyCsvJob.class, "Mapony CSV. Exportar a csv.");
		addClass("agrupa", MaponyGroupNearJob.class, "Mapony Group By Near Places (MaponyGroupNearJob)");
		addClass("inferencia", MaponyInferenciaJob.class, "Mapony Inferencia (carga en ES)");
		addClass("pruebas", MaponyPruebasJob.class, "Mapony Pruebas");
	}

	public static void main(String[] args) throws Throwable {
		Driver driver = new Driver();
		driver.driver(args);
		System.exit(0);
	}
}
