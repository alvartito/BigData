package util.beans;



/** @author Álvaro Sánchez Blasco
 *
 */
public class InferenciaBean {
	/** Photo/Video Identifier */
	private String identifier;
	
	private String dateTaken;
	/** Capture Device */
	private String captureDevice;
	/** Title */
	private String title;
	/** Description */
	private String description;
	/** User Tags */
	private String userTags;
	/** Machine Tags */
	private String machineTags;
	/** Longitude */
	private String longitude;
	/** Latitude */
	private String latitude;
	/** Photo/Video Download Url */
	private String downloadUrl;
	
	private String geoHash;
	private String geoHashCiudad;
	private String pais;
	private String ciudad;
	private String continente;
	

	public InferenciaBean() {
	}

	/**
	 * @param identifier
	 * @param description
	 * @param downloadUrl
	 * @param dateTaken
	 * @param longitude
	 * @param latitude
	 * @param title
	 * @param captureDevice
	 * @param userTags
	 * @param machineTags
	 */
	public InferenciaBean(String identifier, String description, String downloadUrl, String dateTaken, String longitude, String latitude,
			String title, String captureDevice, String userTags, String machineTags) {
		set(identifier, description, downloadUrl, dateTaken, longitude, latitude, title, captureDevice, userTags, machineTags);
	}

	/**
	 * @param inferencia
	 * @param geohash
	 * @param ciudad
	 * @param pais
	 * @param continente
	 * @return
	 */
	public final InferenciaBean completarBean(InferenciaBean inferencia, String geohash, String ciudad, String pais, String continente) {
		inferencia.setGeoHash(geohash);
		inferencia.setCiudad(ciudad);
		inferencia.setPais(pais);
		inferencia.setContinente(continente);
		return inferencia;
	}
	
	/**
	 * @param identifier
	 * @param description
	 * @param downloadUrl
	 * @param dateTaken
	 * @param longitude
	 * @param latitude
	 * @param title
	 * @param captureDevice
	 * @param userTags
	 * @param machineTags
	 */
	private void set(String identifier, String description, String downloadUrl, String dateTaken, String longitude, String latitude,
			String title, String captureDevice, String userTags, String machineTags) {

		this.identifier = identifier;
		this.description = description;
		this.downloadUrl = downloadUrl;
		this.dateTaken = dateTaken;
		this.longitude = longitude;
		this.latitude = latitude;
		this.title = title;
		this.captureDevice = captureDevice;
		this.userTags = userTags;
		this.machineTags = machineTags;

		setIdentifier(identifier);
		setDescription(description);
		setDownloadUrl(downloadUrl);
		setDateTaken(dateTaken);
		setLongitude(longitude);
		setLatitude(latitude);
		setTitle(title);
		setCaptureDevice(captureDevice);
		setUserTags(userTags);
		setMachineTags(machineTags);
	}

//	/**
//	 * @see java.lang.Object#toString()
//	 * @return identifier + "|" + description + "|" + downloadUrl + "|" + longitude + "|" + latitude + "|" +
//	 *         title + "|" + machineTags + "|" + userTags + "|" + geoHash;
//	 */
//	public String toString() {
//		return getIdentifier() + "|" + getDescription() + "|" + getDownloadUrl() + "|" + getLongitude() + "|" + getLatitude() + "|" + getTitle()
//				+ "|" + getMachineTags() + "|" + getUserTags() + "|" + getGeoHash();
//	}

	/**
	 * @return the identifier
	 */
	public final String getIdentifier() {
		return identifier;
	}

	/**
	 * @param identifier
	 *            the identifier to set
	 */
	public final void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return the dateTaken
	 */
	public final String getDateTaken() {
		return dateTaken;
	}

	/**
	 * @param dateTaken
	 *            the dateTaken to set
	 */
	public final void setDateTaken(String dateTaken) {
		this.dateTaken = dateTaken;
	}

	/**
	 * @return the captureDevice
	 */
	public final String getCaptureDevice() {
		return captureDevice;
	}

	/**
	 * @param captureDevice
	 *            the captureDevice to set
	 */
	public final void setCaptureDevice(String captureDevice) {
		this.captureDevice = captureDevice;
	}

	/**
	 * @return the title
	 */
	public final String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public final void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public final String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public final void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the userTags
	 */
	public final String getUserTags() {
		return userTags;
	}

	/**
	 * @param userTags
	 *            the userTags to set
	 */
	public final void setUserTags(String userTags) {
		this.userTags = userTags;
	}

	/**
	 * @return the machineTags
	 */
	public final String getMachineTags() {
		return machineTags;
	}

	/**
	 * @param machineTags
	 *            the machineTags to set
	 */
	public final void setMachineTags(String machineTags) {
		this.machineTags = machineTags;
	}

	/**
	 * @return the longitude
	 */
	public final String getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public final void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	public final String getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public final void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the downloadUrl
	 */
	public final String getDownloadUrl() {
		return downloadUrl;
	}

	/**
	 * @param downloadUrl
	 *            the downloadUrl to set
	 */
	public final void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
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
	public final void setGeoHash(String geoHash) {
		this.geoHash = geoHash;
	}

	/**
	 * @return the pais
	 */
	public final String getPais() {
		return pais;
	}

	/**
	 * @param pais the pais to set
	 */
	public final void setPais(String pais) {
		this.pais = pais;
	}

	/**
	 * @return the ciudad
	 */
	public final String getCiudad() {
		return ciudad;
	}

	/**
	 * @param ciudad the ciudad to set
	 */
	public final void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	/**
	 * @return the continente
	 */
	public final String getContinente() {
		return continente;
	}

	/**
	 * @param continente the continente to set
	 */
	public final void setContinente(String continente) {
		this.continente = continente;
	}

	/**
	 * @return the geoHashCiudad
	 */
	public final String getGeoHashCiudad() {
		return geoHashCiudad;
	}

	/**
	 * @param geoHashCiudad the geoHashCiudad to set
	 */
	public final void setGeoHashCiudad(String geoHashCiudad) {
		this.geoHashCiudad = geoHashCiudad;
	}
}
