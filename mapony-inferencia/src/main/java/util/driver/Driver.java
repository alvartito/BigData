package util.driver;

import proyecto.utad.mapony.csv.MaponyCsvJob;
import proyecto.utad.mapony.groupNear.MaponyGroupNearJob;
import proyecto.utad.mapony.inferencia.MaponyInferenciaJob;
import proyecto.utad.mapony.pruebas.MaponyPruebasJob;

public class Driver extends ProgramDriver {
	
	public Driver() throws Throwable {
		super();
		addClass("Mapony CSV. Exportar a csv.", MaponyCsvJob.class, "MaponyCsvJob");
		addClass("Mapony Group By Near Places (MaponyGroupNearJob)", MaponyGroupNearJob.class, "MaponyGroupNearJob");
		addClass("Mapony Inferencia (carga en ES)", MaponyInferenciaJob.class, "MaponyInferenciaJob");
		addClass("Mapony Pruebas", MaponyPruebasJob.class, "MaponyPruebasJob");
	}

	public static void main(String[] args) throws Throwable {
		Driver driver = new Driver();
		driver.driver(args);
		System.exit(0);
	}
}
