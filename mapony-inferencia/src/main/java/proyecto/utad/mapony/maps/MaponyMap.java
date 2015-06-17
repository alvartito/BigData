package proyecto.utad.mapony.maps;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import writables.CustomWritable;
import writables.RawDataBean;

public class MaponyMap extends Mapper<LongWritable, Text, Text, CustomWritable> {

	private Text outKey;

	private final Logger logger = LoggerFactory.getLogger(MaponyMap.class);

	protected void map(LongWritable offset, Text line, Context context) throws IOException, InterruptedException {
		String[] dato = line.toString().split("\t");

		// Comenzamos por limpiar las referencias de videos, por lo que el campo [22] del String[] ha de ser
		// '0' para que lo procesemos.
		final RawDataBean rdBean = new RawDataBean(dato[0], dato[1], dato[2], dato[3], dato[4], dato[5], dato[6], dato[7], dato[8], dato[9], dato[10], dato[11],
				dato[12], dato[13], dato[14], dato[15], dato[16], dato[17], dato[18], dato[19], dato[20], dato[21], dato[22]);
		
		// Además, si no tiene informados los campos de longitud y latitud, también descartamos el registro.
		if ("1".toString().compareTo(rdBean.getMarker()) != 0 && ("".compareTo(rdBean.getLatitude()) != 0 && "".compareTo(rdBean.getLongitude()) != 0)) {
			getLogger().info(rdBean.toString());
			outKey = new Text(rdBean.getIdentifier());

			CustomWritable cwDescripcion = new CustomWritable("descripcion");
			cwDescripcion.setTexto(rdBean.getDescription());

			CustomWritable cwFoto = new CustomWritable("foto");
			String url = rdBean.getDownloadUrl();
			cwFoto.setTexto(url);

			CustomWritable cwGeo = new CustomWritable("location");
			StringBuilder posicion = new StringBuilder();
			posicion.append(rdBean.getLatitude());
			posicion.append(",");
			posicion.append(rdBean.getLongitude());

			cwGeo.setTexto(posicion.toString());

			CustomWritable cwTags = new CustomWritable("tags");
			cwTags.setTexto(rdBean.getMachineTags() + " " + rdBean.getUserTags());

			CustomWritable cwTitulo = new CustomWritable("titulo");
			cwTitulo.setTexto(rdBean.getTitle());

			context.write(outKey, cwDescripcion);
			context.write(outKey, cwFoto);
			context.write(outKey, cwGeo);
			context.write(outKey, cwTags);
			context.write(outKey, cwTitulo);
		}
	}

	/**
	 * @return the logger
	 */
	private final Logger getLogger() {
		return logger;
	};

	//
	// private final String CADENA_VACIA = " ";
	//
	// private String replace(String str) {
	// if (str.trim().length() > 0) {
	// logger.error("'"+str+"'");
	// str = str.replaceAll("%5B", CADENA_VACIA).replace("%5D", CADENA_VACIA).replaceAll("+", CADENA_VACIA);
	// }
	// return str;
	// }

//	public Address getAddress(double latitud, double longitud) {

//		WebService.setUserName("demo"); // add your username here
//
//		Address searchResult;
//
//		PostalCodeSearchCriteria postalCodeSearchCriteria = new PostalCodeSearchCriteria();
//		try {
//			postalCodeSearchCriteria.setLatitude(latitud);
//			postalCodeSearchCriteria.setLongitude(longitud);
//
//			List<PostalCode> postalCodes = WebService.findNearbyPostalCodes(postalCodeSearchCriteria);
//
//			postalCodeSearchCriteria.setLatitude(longitud);
//			postalCodeSearchCriteria.setLongitude(latitud);
//			List<PostalCode> postalCodesInv = WebService.findNearbyPostalCodes(postalCodeSearchCriteria);
//
//			if (null != postalCodes) {
//				for (PostalCode postalCode : postalCodes) {
//					archivo.write("\npostalCodes.AdminCode1 " + postalCode.getAdminCode1());
//					archivo.write("\npostalCodes.AdminCode2 " + postalCode.getAdminCode2());
//					archivo.write("\npostalCodes.AdminName1 " + postalCode.getAdminName1());
//					archivo.write("\npostalCodes.AdminName2 " + postalCode.getAdminName2());
//					archivo.write("\npostalCodes.CountryCode " + postalCode.getCountryCode());
//					archivo.write("\npostalCodes.Distance " + postalCode.getDistance());
//					archivo.write("\npostalCodes.Latitude " + postalCode.getLatitude());
//					archivo.write("\npostalCodes.Longitude " + postalCode.getLongitude());
//					archivo.write("\npostalCodes.PlaceName " + postalCode.getPlaceName());
//					archivo.write("\npostalCodes.PostalCode " + postalCode.getPostalCode()+"\n\n");
//				}
//			}
//
//			if (null != postalCodesInv) {
//				for (PostalCode postalCode : postalCodesInv) {
//					logger.info("postalCodesInv " + postalCode.toString());
//				}
//			}
//		} catch (InvalidParameterException e1) {
//			// TODO Auto-generated catch block
//			logger.error("getDocument(long, lat).InvalidParameterException " + e1.getMessage());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			logger.error("getDocument(long, lat).Exception " + e.getMessage());
//		}
//
//		try {
//			searchResult = WebService.findNearestAddress(longitud, latitud);
//			return searchResult;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//
//	}

}
