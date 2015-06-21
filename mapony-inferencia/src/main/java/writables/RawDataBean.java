package writables;


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

		this.identifier = identifier;
		setIdentifier(identifier);

		this.userNsid = userNsid;
		setUserNsid(userNsid);

		this.userNickname = userNickname;
		this.dateTaken = dateTaken;
		this.dateUploaded = dateUploaded;
		this.captureDevice = captureDevice;
		this.title = title;
		this.description = description;
		this.userTags = userTags;
		this.machineTags = machineTags;
		this.longitude = longitude;
		this.latitude = latitude;
		this.accuracy = accuracy;
		this.pageUrl = pageUrl;
		this.downloadUrl = downloadUrl;
		this.licenseName = licenseName;
		this.licenseUrl = licenseUrl;
		this.serverIdentifier = serverIdentifier;
		this.farmIdentifier = farmIdentifier;
		this.secret = secret;
		this.secretOriginal = secretOriginal;
		this.extension = extension;
		this.marker = marker;

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
	 * @return the userNsid
	 */
	public final String getUserNsid() {
		return userNsid;
	}

	/**
	 * @param userNsid
	 *            the userNsid to set
	 */
	public final void setUserNsid(String userNsid) {
		this.userNsid = userNsid;
	}

	/**
	 * @return the userNickname
	 */
	public final String getUserNickname() {
		return userNickname;
	}

	/**
	 * @param userNickname
	 *            the userNickname to set
	 */
	public final void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
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
	 * @return the dateUploaded
	 */
	public final String getDateUploaded() {
		return dateUploaded;
	}

	/**
	 * @param dateUploaded
	 *            the dateUploaded to set
	 */
	public final void setDateUploaded(String dateUploaded) {
		this.dateUploaded = dateUploaded;
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
	 * @return the accuracy
	 */
	public final String getAccuracy() {
		return accuracy;
	}

	/**
	 * @param accuracy
	 *            the accuracy to set
	 */
	public final void setAccuracy(String accuracy) {
		this.accuracy = accuracy;
	}

	/**
	 * @return the pageUrl
	 */
	public final String getPageUrl() {
		return pageUrl;
	}

	/**
	 * @param pageUrl
	 *            the pageUrl to set
	 */
	public final void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
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
	 * @return the licenseName
	 */
	public final String getLicenseName() {
		return licenseName;
	}

	/**
	 * @param licenseName
	 *            the licenseName to set
	 */
	public final void setLicenseName(String licenseName) {
		this.licenseName = licenseName;
	}

	/**
	 * @return the licenseUrl
	 */
	public final String getLicenseUrl() {
		return licenseUrl;
	}

	/**
	 * @param licenseUrl
	 *            the licenseUrl to set
	 */
	public final void setLicenseUrl(String licenseUrl) {
		this.licenseUrl = licenseUrl;
	}

	/**
	 * @return the serverIdentifier
	 */
	public final String getServerIdentifier() {
		return serverIdentifier;
	}

	/**
	 * @param serverIdentifier
	 *            the serverIdentifier to set
	 */
	public final void setServerIdentifier(String serverIdentifier) {
		this.serverIdentifier = serverIdentifier;
	}

	/**
	 * @return the farmIdentifier
	 */
	public final String getFarmIdentifier() {
		return farmIdentifier;
	}

	/**
	 * @param farmIdentifier
	 *            the farmIdentifier to set
	 */
	public final void setFarmIdentifier(String farmIdentifier) {
		this.farmIdentifier = farmIdentifier;
	}

	/**
	 * @return the secret
	 */
	public final String getSecret() {
		return secret;
	}

	/**
	 * @param secret
	 *            the secret to set
	 */
	public final void setSecret(String secret) {
		this.secret = secret;
	}

	/**
	 * @return the secretOriginal
	 */
	public final String getSecretOriginal() {
		return secretOriginal;
	}

	/**
	 * @param secretOriginal
	 *            the secretOriginal to set
	 */
	public final void setSecretOriginal(String secretOriginal) {
		this.secretOriginal = secretOriginal;
	}

	/**
	 * @return the extension
	 */
	public final String getExtension() {
		return extension;
	}

	/**
	 * @param extension
	 *            the extension to set
	 */
	public final void setExtension(String extension) {
		this.extension = extension;
	}

	/**
	 * @return the marker
	 */
	public final String getMarker() {
		return marker;
	}

	/**
	 * @param marker
	 *            the marker to set
	 */
	public final void setMarker(String marker) {
		this.marker = marker;
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
}
