package util.beans;

import util.MaponyUtil;


/** @author Álvaro Sánchez Blasco
 *
 */
public class RawDataBean {
	/** Photo/Video Identifier */
	private String identifier;
	/** User Nsid */
	private String userNsid;
	/** User Nickname */
	private String userNickname;
	/** Date Taken */
	private String dateTaken;
	/** Date Uploaded */
	private String dateUploaded;
	/** Capture Device */
	private String captureDevice;
	/** Title */
	private String title;
	/** Description */
	private String description;
	/** User Tags (Comma-Separated) */
	private String userTags;
	/** Machine Tags (Comma-Separated) */
	private String machineTags;
	/** Longitude */
	private String longitude;
	/** Latitude */
	private String latitude;
	/** Accuracy */
	private String accuracy;
	/** Photo/Video Page Url */
	private String pageUrl;
	/** Photo/Video Download Url */
	private String downloadUrl;
	/** License Name */
	private String licenseName;
	/** License Url */
	private String licenseUrl;
	/** Photo/Video Server Identifier */
	private String serverIdentifier;
	/** Photo/Video Farm Identifier */
	private String farmIdentifier;
	/** Photo/Video Secret */
	private String secret;
	/** Photo/Video Secret Original */
	private String secretOriginal;
	/** Extension Of The Original Photo */
	private String extension;
	/** Photos/Video Marker (0 = Photo, 1 = Video) */
	private String marker;
	
	private String geoHash;
	private String pais;
	private String ciudad;
	private String continente;
	

	public RawDataBean() {
	}

	/**
	 * @param identifier
	 * @param userNsid
	 * @param userNickname
	 * @param dateTaken
	 * @param dateUploaded
	 * @param captureDevice
	 * @param title
	 * @param description
	 * @param userTags
	 * @param machineTags
	 * @param longitude
	 * @param latitude
	 * @param accuracy
	 * @param pageUrl
	 * @param downloadUrl
	 * @param licenseName
	 * @param licenseUrl
	 * @param serverIdentifier
	 * @param farmIdentifier
	 * @param secret
	 * @param secretOriginal
	 * @param extension
	 * @param marker
	 */
	public RawDataBean(String identifier, String userNsid, String userNickname, String dateTaken,
			String dateUploaded, String captureDevice, String title, String description, String userTags,
			String machineTags, String longitude, String latitude, String accuracy, String pageUrl, String downloadUrl,
			String licenseName, String licenseUrl, String serverIdentifier, String farmIdentifier, String secret,
			String secretOriginal, String extension, String marker) {
		set(identifier, userNsid, userNickname, dateTaken, dateUploaded, captureDevice, title, description, userTags,
				machineTags, longitude, latitude, accuracy, pageUrl, downloadUrl, licenseName, licenseUrl,
				serverIdentifier, farmIdentifier, secret, secretOriginal, extension, marker);
	}

	/**
	 * @param identifier
	 * @param userNsid
	 * @param userNickname
	 * @param dateTaken
	 * @param dateUploaded
	 * @param captureDevice
	 * @param title
	 * @param description
	 * @param userTags
	 * @param machineTags
	 * @param longitude
	 * @param latitude
	 * @param accuracy
	 * @param pageUrl
	 * @param downloadUrl
	 * @param licenseName
	 * @param licenseUrl
	 * @param serverIdentifier
	 * @param farmIdentifier
	 * @param secret
	 * @param secretOriginal
	 * @param extension
	 * @param marker
	 */
	public void set(String identifier, String userNsid, String userNickname, String dateTaken, String dateUploaded,
			String captureDevice, String title, String description, String userTags, String machineTags,
			String longitude, String latitude, String accuracy, String pageUrl, String downloadUrl, String licenseName,
			String licenseUrl, String serverIdentifier, String farmIdentifier, String secret, String secretOriginal,
			String extension, String marker) {

		setIdentifier(identifier);
		setUserNsid(userNsid);
		setUserNickname(userNickname);
		setDateTaken(dateTaken);
		setDateUploaded(dateUploaded);
		setCaptureDevice(captureDevice);
		setTitle(title);
		setDescription(description);
		setUserTags(userTags);
		setMachineTags(machineTags);
		setLongitude(longitude);
		setLatitude(latitude);
		setAccuracy(accuracy);
		setPageUrl(pageUrl);
		setDownloadUrl(downloadUrl);
		setLicenseName(licenseName);
		setLicenseUrl(licenseUrl);
		setServerIdentifier(serverIdentifier);
		setFarmIdentifier(farmIdentifier);
		setSecret(secret);
		setSecretOriginal(secretOriginal);
		setExtension(extension);
		setMarker(marker);
	}

	/**
	 * @see java.lang.Object#toString()
	 * @return identifier + "|" + description + "|" + downloadUrl + "|" + longitude + "|" + latitude + "|" +
	 *         title + "|" + machineTags + "|" + userTags + "|" + geoHash;
	 */
	public String toString() {
		return getIdentifier() + "|" + getDescription() + "|" + getDownloadUrl() + "|" + getLongitude() + "|" + getLatitude() + "|" + getTitle()
				+ "|" + getMachineTags() + "|" + getUserTags() + "|" + getGeoHash();
	}
	
	public String toCsvString() {
		StringBuilder csv = new StringBuilder();
		
		csv.append(getIdentifier());
		
		if(MaponyUtil.tieneValor(getDescription())){
			csv.append(",").append(getDescription());
		} else {
			csv.append(",").append("\t");
		}
		if(MaponyUtil.tieneValor(getDownloadUrl())){
			csv.append(",").append(getDownloadUrl());
		} else {
			csv.append(",").append("\t");
		}
		if(MaponyUtil.tieneValor(getDateTaken())){
			csv.append(",").append(getDateTaken());
		} else {
			csv.append(",").append("\t");
		}
		if(MaponyUtil.tieneValor(getLongitude())){
			csv.append(",").append(getLongitude());
		} else {
			csv.append(",").append("\t");
		}
		if(MaponyUtil.tieneValor(getLatitude())){
			csv.append(",").append(getLatitude());
		} else {
			csv.append(",").append("\t");
		}
		if(MaponyUtil.tieneValor(getTitle())){
			csv.append(",").append(getTitle());
		} else {
			csv.append(",").append("\t");
		}
		if(MaponyUtil.tieneValor(getCaptureDevice())){
			csv.append(",").append(getCaptureDevice());
		} else {
			csv.append(",").append("\t");
		}
		if(MaponyUtil.tieneValor(getCiudad())){
			csv.append(",").append(getCiudad());
		} else {
			csv.append(",").append("\t");
		}
		if(MaponyUtil.tieneValor(getPais())){
			csv.append(",").append(getPais());
		} else {
			csv.append(",").append("\t");
		}
		if(MaponyUtil.tieneValor(getContinente())){
			csv.append(",").append(getContinente());
		} else {
			csv.append(",").append("\t");
		}

		return csv.toString();
	}

	public String toSequenceFileString() {
		StringBuilder sequenceFileInfo = new StringBuilder();
		
		sequenceFileInfo.append(getIdentifier());
		
		if(MaponyUtil.tieneValor(getDescription())){
			sequenceFileInfo.append("\t").append(MaponyUtil.cleanString(getDescription()));
		} else {
			sequenceFileInfo.append("\t");
		}
		if(MaponyUtil.tieneValor(getDownloadUrl())){
			sequenceFileInfo.append("\t").append(getDownloadUrl());
		} else {
			sequenceFileInfo.append("\t");
		}
		if(MaponyUtil.tieneValor(getDateTaken())){
			sequenceFileInfo.append("\t").append(getDateTaken());
		} else {
			sequenceFileInfo.append("\t");
		}
		if(MaponyUtil.tieneValor(getLongitude())){
			sequenceFileInfo.append("\t").append(getLongitude());
		} else {
			sequenceFileInfo.append("\t");
		}
		if(MaponyUtil.tieneValor(getLatitude())){
			sequenceFileInfo.append("\t").append(getLatitude());
		} else {
			sequenceFileInfo.append("\t");
		}
		if(MaponyUtil.tieneValor(getTitle())){
			sequenceFileInfo.append("\t").append(MaponyUtil.cleanString(getTitle()));
		} else {
			sequenceFileInfo.append("\t");
		}
		if(MaponyUtil.tieneValor(getCaptureDevice())){
			sequenceFileInfo.append("\t").append(MaponyUtil.cleanString(getCaptureDevice()));
		} else {
			sequenceFileInfo.append("\t");
		}
		if(MaponyUtil.tieneValor(getUserTags())){
			sequenceFileInfo.append("\t").append(MaponyUtil.cleanString(getUserTags()));
		} else {
			sequenceFileInfo.append("\t");
		}
		if(MaponyUtil.tieneValor(getMachineTags())){
			sequenceFileInfo.append("\t").append(MaponyUtil.cleanString(getMachineTags()));
		} else {
			sequenceFileInfo.append("\t");
		}		
		return sequenceFileInfo.toString();
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
	 * @return the identifier
	 */
	public final String getIdentifier() {
		return identifier;
	}

	/**
	 * @param identifier the identifier to set
	 */
	private final void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return the userNsid
	 */
	private final String getUserNsid() {
		return userNsid;
	}

	/**
	 * @param userNsid the userNsid to set
	 */
	private final void setUserNsid(String userNsid) {
		this.userNsid = userNsid;
	}

	/**
	 * @return the userNickname
	 */
	private final String getUserNickname() {
		return userNickname;
	}

	/**
	 * @param userNickname the userNickname to set
	 */
	private final void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}

	/**
	 * @return the dateTaken
	 */
	private final String getDateTaken() {
		return dateTaken;
	}

	/**
	 * @param dateTaken the dateTaken to set
	 */
	private final void setDateTaken(String dateTaken) {
		this.dateTaken = dateTaken;
	}

	/**
	 * @return the dateUploaded
	 */
	private final String getDateUploaded() {
		return dateUploaded;
	}

	/**
	 * @param dateUploaded the dateUploaded to set
	 */
	private final void setDateUploaded(String dateUploaded) {
		this.dateUploaded = dateUploaded;
	}

	/**
	 * @return the captureDevice
	 */
	private final String getCaptureDevice() {
		return captureDevice;
	}

	/**
	 * @param captureDevice the captureDevice to set
	 */
	private final void setCaptureDevice(String captureDevice) {
		this.captureDevice = captureDevice;
	}

	/**
	 * @return the title
	 */
	public final String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	private final void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public final String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	private final void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the userTags
	 */
	public final String getUserTags() {
		return userTags;
	}

	/**
	 * @param userTags the userTags to set
	 */
	private final void setUserTags(String userTags) {
		this.userTags = userTags;
	}

	/**
	 * @return the machineTags
	 */
	public final String getMachineTags() {
		return machineTags;
	}

	/**
	 * @param machineTags the machineTags to set
	 */
	private final void setMachineTags(String machineTags) {
		this.machineTags = machineTags;
	}

	/**
	 * @return the longitude
	 */
	public final String getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	private final void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	public final String getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	private final void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the accuracy
	 */
	private final String getAccuracy() {
		return accuracy;
	}

	/**
	 * @param accuracy the accuracy to set
	 */
	private final void setAccuracy(String accuracy) {
		this.accuracy = accuracy;
	}

	/**
	 * @return the pageUrl
	 */
	private final String getPageUrl() {
		return pageUrl;
	}

	/**
	 * @param pageUrl the pageUrl to set
	 */
	private final void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	/**
	 * @return the downloadUrl
	 */
	public final String getDownloadUrl() {
		return downloadUrl;
	}

	/**
	 * @param downloadUrl the downloadUrl to set
	 */
	private final void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	/**
	 * @return the licenseName
	 */
	private final String getLicenseName() {
		return licenseName;
	}

	/**
	 * @param licenseName the licenseName to set
	 */
	private final void setLicenseName(String licenseName) {
		this.licenseName = licenseName;
	}

	/**
	 * @return the licenseUrl
	 */
	private final String getLicenseUrl() {
		return licenseUrl;
	}

	/**
	 * @param licenseUrl the licenseUrl to set
	 */
	private final void setLicenseUrl(String licenseUrl) {
		this.licenseUrl = licenseUrl;
	}

	/**
	 * @return the serverIdentifier
	 */
	private final String getServerIdentifier() {
		return serverIdentifier;
	}

	/**
	 * @param serverIdentifier the serverIdentifier to set
	 */
	private final void setServerIdentifier(String serverIdentifier) {
		this.serverIdentifier = serverIdentifier;
	}

	/**
	 * @return the farmIdentifier
	 */
	private final String getFarmIdentifier() {
		return farmIdentifier;
	}

	/**
	 * @param farmIdentifier the farmIdentifier to set
	 */
	private final void setFarmIdentifier(String farmIdentifier) {
		this.farmIdentifier = farmIdentifier;
	}

	/**
	 * @return the secret
	 */
	private final String getSecret() {
		return secret;
	}

	/**
	 * @param secret the secret to set
	 */
	private final void setSecret(String secret) {
		this.secret = secret;
	}

	/**
	 * @return the secretOriginal
	 */
	private final String getSecretOriginal() {
		return secretOriginal;
	}

	/**
	 * @param secretOriginal the secretOriginal to set
	 */
	private final void setSecretOriginal(String secretOriginal) {
		this.secretOriginal = secretOriginal;
	}

	/**
	 * @return the extension
	 */
	private final String getExtension() {
		return extension;
	}

	/**
	 * @param extension the extension to set
	 */
	private final void setExtension(String extension) {
		this.extension = extension;
	}

	/**
	 * @return the marker
	 */
	public final String getMarker() {
		return marker;
	}

	/**
	 * @param marker the marker to set
	 */
	private final void setMarker(String marker) {
		this.marker = marker;
	}
}
