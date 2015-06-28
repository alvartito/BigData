package util.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.constantes.MaponyCte;
import ch.hsr.geohash.GeoHash;


/** @author Álvaro Sánchez Blasco
 *
 */
public class GeoHashBean {

	private final Logger logger = LoggerFactory.getLogger(GeoHashBean.class);
	
	private String geonameid;
	private String name;
	private String asciiname;
	private String alternatenames;
	private double latitude;
	private double longitude;
	private String featureclass;
	private String featurecode;
	private String countrycode;
	private String cc2;
	private String admin1code;
	private String admin2code;
	private String admin3code;
	private String admin4code;
	private String population;
	private String elevation;
	private String dem;
	private String timezone;
	private String modificationdate;
	
	private String pais;
	private String continente;
	private String geoHash;
	
	public GeoHashBean() {
	}


	/**
	 * @param geonameid
	 * @param name
	 * @param asciiname
	 * @param alternatenames
	 * @param latitude
	 * @param longitude
	 * @param featureclass
	 * @param featurecode
	 * @param countrycode
	 * @param cc2
	 * @param admin1code
	 * @param admin2code
	 * @param admin3code
	 * @param admin4code
	 * @param population
	 * @param elevation
	 * @param dem
	 * @param timezone
	 * @param modificationdate
	 */
	public GeoHashBean(final String geonameid, final String name, final String asciiname, final String alternatenames, final String latitude,
			final String longitude, String featureclass, final String featurecode, final String countrycode, final String cc2,
			final String admin1code, final String admin2code, final String admin3code, String admin4code, final String population,
			final String elevation, final String dem, final String timezone, final String modificationdate) {
		setGeonameid(geonameid);
		setName(name);
		setAsciiname(asciiname);
		setAlternatenames(alternatenames);
		setLatitude(new Double(latitude));
		setLongitude(new Double(longitude));
		setFeatureclass(featureclass);
		setFeaturecode(featurecode);
		setCountrycode(countrycode);
		setCc2(cc2);
		setAdmin1code(admin1code);
		setAdmin2code(admin2code);
		setAdmin3code(admin3code);
		setAdmin4code(admin4code);
		setPopulation(population);
		setElevation(elevation);
		setDem(dem);
		setTimezone(timezone);
		setModificationdate(modificationdate);

		String[] continentePais = timezone.split("/");
		setContinente(continentePais[0]);
		setPais(continentePais[1]);
		setGeoHash(GeoHash.geoHashStringWithCharacterPrecision(getLatitude(), getLongitude(), MaponyCte.precisionGeoHashCiudad));
	}

	public GeoHashBean(final String[] datos) {
		setGeonameid(datos[0]);
		setName(datos[1]);
		setAsciiname(datos[2]);
		setAlternatenames(datos[3]);
		setLatitude(new Double(datos[4]));
		setLongitude(new Double(datos[5]));
		setFeatureclass(datos[6]);
		setFeaturecode(datos[7]);
		setCountrycode(datos[8]);
		setCc2(datos[9]);
		setAdmin1code(datos[10]);
		setAdmin2code(datos[11]);
		setAdmin3code(datos[12]);
		setAdmin4code(datos[13]);
		setPopulation(datos[14]);
		setElevation(datos[15]);
		setDem(datos[16]);
		setTimezone(datos[17]);
		setModificationdate(datos[18]);

		String[] continentePais = getTimezone().split("/");
		setContinente(continentePais[0]);
		setPais(continentePais[1]);
		setGeoHash(GeoHash.geoHashStringWithCharacterPrecision(getLatitude(), getLongitude(), MaponyCte.precisionGeoHashCiudad));
		
//		getLogger().info(toString());
	}
	
	
	
	/**
	 * @see java.lang.Object#toString()
	 * @return getGeoHash() + "|" + getName() + "|" + getPais() + "|" + getContinente();
	 */
	public String toString() {
		return getGeoHash() + "|" + getName() + "|" + getPais() + "|" + getContinente();
	}


	/**
	 * @return the geonameid
	 */
	private final String getGeonameid() {
		return geonameid;
	}


	/**
	 * @param geonameid the geonameid to set
	 */
	private final void setGeonameid(final String geonameid) {
		this.geonameid = geonameid;
	}


	/**
	 * @return the name
	 */
	private final String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	private final void setName(final String name) {
		this.name = name;
	}


	/**
	 * @return the asciiname
	 */
	private final String getAsciiname() {
		return asciiname;
	}


	/**
	 * @param asciiname the asciiname to set
	 */
	private final void setAsciiname(final String asciiname) {
		this.asciiname = asciiname;
	}


	/**
	 * @return the alternatenames
	 */
	private final String getAlternatenames() {
		return alternatenames;
	}


	/**
	 * @param alternatenames the alternatenames to set
	 */
	private final void setAlternatenames(final String alternatenames) {
		this.alternatenames = alternatenames;
	}

	/**
	 * @return the featureclass
	 */
	private final String getFeatureclass() {
		return featureclass;
	}


	/**
	 * @param featureclass the featureclass to set
	 */
	private final void setFeatureclass(final String featureclass) {
		this.featureclass = featureclass;
	}


	/**
	 * @return the featurecode
	 */
	private final String getFeaturecode() {
		return featurecode;
	}


	/**
	 * @param featurecode the featurecode to set
	 */
	private final void setFeaturecode(final String featurecode) {
		this.featurecode = featurecode;
	}


	/**
	 * @return the countrycode
	 */
	private final String getCountrycode() {
		return countrycode;
	}


	/**
	 * @param countrycode the countrycode to set
	 */
	private final void setCountrycode(final String countrycode) {
		this.countrycode = countrycode;
	}


	/**
	 * @return the cc2
	 */
	private final String getCc2() {
		return cc2;
	}


	/**
	 * @param cc2 the cc2 to set
	 */
	private final void setCc2(final String cc2) {
		this.cc2 = cc2;
	}


	/**
	 * @return the admin1code
	 */
	private final String getAdmin1code() {
		return admin1code;
	}


	/**
	 * @param admin1code the admin1code to set
	 */
	private final void setAdmin1code(final String admin1code) {
		this.admin1code = admin1code;
	}


	/**
	 * @return the admin2code
	 */
	private final String getAdmin2code() {
		return admin2code;
	}


	/**
	 * @param admin2code the admin2code to set
	 */
	private final void setAdmin2code(final String admin2code) {
		this.admin2code = admin2code;
	}


	/**
	 * @return the admin3code
	 */
	private final String getAdmin3code() {
		return admin3code;
	}


	/**
	 * @param admin3code the admin3code to set
	 */
	private final void setAdmin3code(final String admin3code) {
		this.admin3code = admin3code;
	}


	/**
	 * @return the admin4code
	 */
	private final String getAdmin4code() {
		return admin4code;
	}


	/**
	 * @param admin4code the admin4code to set
	 */
	private final void setAdmin4code(final String admin4code) {
		this.admin4code = admin4code;
	}


	/**
	 * @return the population
	 */
	private final String getPopulation() {
		return population;
	}


	/**
	 * @param population the population to set
	 */
	private final void setPopulation(final String population) {
		this.population = population;
	}


	/**
	 * @return the elevation
	 */
	private final String getElevation() {
		return elevation;
	}


	/**
	 * @param elevation the elevation to set
	 */
	private final void setElevation(final String elevation) {
		this.elevation = elevation;
	}


	/**
	 * @return the dem
	 */
	private final String getDem() {
		return dem;
	}


	/**
	 * @param dem the dem to set
	 */
	private final void setDem(final String dem) {
		this.dem = dem;
	}


	/**
	 * @return the timezone
	 */
	private final String getTimezone() {
		return timezone;
	}


	/**
	 * @param timezone the timezone to set
	 */
	private final void setTimezone(final String timezone) {
		this.timezone = timezone;
	}


	/**
	 * @return the modificationdate
	 */
	private final String getModificationdate() {
		return modificationdate;
	}


	/**
	 * @param modificationdate the modificationdate to set
	 */
	private final void setModificationdate(final String modificationdate) {
		this.modificationdate = modificationdate;
	}


	/**
	 * @return the pais
	 */
	private final String getPais() {
		return pais;
	}


	/**
	 * @param pais the pais to set
	 */
	private final void setPais(final String pais) {
		this.pais = pais;
	}


	/**
	 * @return the continente
	 */
	private final String getContinente() {
		return continente;
	}


	/**
	 * @param continente the continente to set
	 */
	private final void setContinente(final String continente) {
		this.continente = continente;
	}


	/**
	 * @return the latitude
	 */
	private final double getLatitude() {
		return latitude;
	}


	/**
	 * @param latitude the latitude to set
	 */
	private final void setLatitude(final double latitude) {
		this.latitude = latitude;
	}


	/**
	 * @return the longitude
	 */
	private final double getLongitude() {
		return longitude;
	}


	/**
	 * @param longitude the longitude to set
	 */
	private final void setLongitude(final double longitude) {
		this.longitude = longitude;
	}


	/**
	 * @return the geoHash
	 */
	public final String getGeoHash() {
		return geoHash;
	}


	/**
	 * @param geoHash the geoHash to set
	 */
	private final void setGeoHash(final String geoHash) {
		this.geoHash = geoHash;
	}


	/**
	 * @return the logger
	 */
	private final Logger getLogger() {
		return logger;
	}

//	public void write(DataOutput out) throws IOException {
//		Text.writeString(out, identifier);
//		Text.writeString(out, userNsid);
//		Text.writeString(out, userNickname);
//		Text.writeString(out, dateTaken);
//		Text.writeString(out, dateUploaded);
//		Text.writeString(out, captureDevice);
//		Text.writeString(out, title);
//		Text.writeString(out, description);
//		Text.writeString(out, userTags);
//		Text.writeString(out, machineTags);
//		Text.writeString(out, longitude);
//		Text.writeString(out, latitude);
//		Text.writeString(out, accuracy);
//		Text.writeString(out, pageUrl);
//		Text.writeString(out, downloadUrl);
//		Text.writeString(out, licenseName);
//		Text.writeString(out, licenseUrl);
//		Text.writeString(out, serverIdentifier);
//		Text.writeString(out, farmIdentifier);
//		Text.writeString(out, secret);
//		Text.writeString(out, secretOriginal);
//		Text.writeString(out, extension);
//		Text.writeString(out, marker);
//	}
//
//	public void readFields(DataInput in) throws IOException {
//		identifier = Text.readString(in);
//		userNsid = Text.readString(in);
//		userNickname = Text.readString(in);
//		dateTaken = Text.readString(in);
//		dateUploaded = Text.readString(in);
//		captureDevice = Text.readString(in);
//		title = Text.readString(in);
//		description = Text.readString(in);
//		userTags = Text.readString(in);
//		machineTags = Text.readString(in);
//		longitude = Text.readString(in);
//		latitude = Text.readString(in);
//		accuracy = Text.readString(in);
//		pageUrl = Text.readString(in);
//		downloadUrl = Text.readString(in);
//		licenseName = Text.readString(in);
//		licenseUrl = Text.readString(in);
//		serverIdentifier = Text.readString(in);
//		farmIdentifier = Text.readString(in);
//		secret = Text.readString(in);
//		secretOriginal = Text.readString(in);
//		extension = Text.readString(in);
//		marker = Text.readString(in);
//	}

}
