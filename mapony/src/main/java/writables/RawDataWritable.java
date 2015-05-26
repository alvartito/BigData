package writables;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
/** @author Álvaro Sánchez Blasco */
/**
 * @author cloudera
 *
 */
public class RawDataWritable implements
Writable {

	private String amigo;
	private boolean relacion;
	
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
	
	
	
	public RawDataWritable(){}
	
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
	public RawDataWritable(String identifier, String userNsid, String userNickname, String dateTaken,
			String dateUploaded, String captureDevice, String title, String description, String userTags,
			String machineTags, String longitude, String latitude, String accuracy, String pageUrl, String downloadUrl,
			String licenseName, String licenseUrl, String serverIdentifier, String farmIdentifier, String secret,
			String secretOriginal, String extension, String marker) {
		set(identifier, userNsid, userNickname, dateTaken, dateUploaded, captureDevice, title, description, userTags,
				machineTags, longitude, latitude, accuracy, pageUrl, downloadUrl, licenseName, licenseUrl,
				serverIdentifier, farmIdentifier, secret, secretOriginal, extension, marker);
	}

	public void set(boolean relacion, String amigo) {
		this.amigo = amigo;
		this.relacion = relacion;
		setRelacion(relacion);
		setAmigo(amigo);
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

	}
	
	public String get() {
		return amigo+ "," + relacion;
	}
	
	public String toString() {
		return amigo + "|" + relacion;
	}
	
	public void write(DataOutput out) throws IOException {
		Text.writeString(out, amigo);
		out.writeBoolean(relacion);

	
	}

	public void readFields(DataInput in) throws IOException {
		amigo = Text.readString(in);
		relacion = in.readBoolean();
	}

	public String getAmigo() {
		return amigo;
	}

	private void setAmigo(String amigo) {
		this.amigo = amigo;
	}

	public boolean isRelacion() {
		return relacion;
	}

	private void setRelacion(boolean relacion) {
		this.relacion = relacion;
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
	 * @param userNsid the userNsid to set
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
	 * @param userNickname the userNickname to set
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
	 * @param dateTaken the dateTaken to set
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
	 * @param dateUploaded the dateUploaded to set
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
	 * @param captureDevice the captureDevice to set
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
	 * @param title the title to set
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
	 * @param description the description to set
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
	 * @param userTags the userTags to set
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
	 * @param machineTags the machineTags to set
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
	 * @param longitude the longitude to set
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
	 * @param latitude the latitude to set
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
	 * @param accuracy the accuracy to set
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
	 * @param pageUrl the pageUrl to set
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
	 * @param downloadUrl the downloadUrl to set
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
	 * @param licenseName the licenseName to set
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
	 * @param licenseUrl the licenseUrl to set
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
	 * @param serverIdentifier the serverIdentifier to set
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
	 * @param farmIdentifier the farmIdentifier to set
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
	 * @param secret the secret to set
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
	 * @param secretOriginal the secretOriginal to set
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
	 * @param extension the extension to set
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
	 * @param marker the marker to set
	 */
	public final void setMarker(String marker) {
		this.marker = marker;
	}
}
