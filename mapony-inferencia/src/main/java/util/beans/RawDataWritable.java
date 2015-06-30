package util.beans;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

import util.MaponyUtil;
import util.constantes.MaponyCte;

/**
 * @author Álvaro Sánchez Blasco
 *
 */
/**
 * @author cloudera
 *
 */
public class RawDataWritable implements Writable {

	/** Photo/Video Identifier */
	private Text identifier;
	/** Date Taken */
	private Text dateTaken;
	/** Capture Device */
	private Text captureDevice;
	/** Title */
	private Text title;
	/** Description */
	private Text description;
	/** User Tags (Comma-Separated) */
	private Text userTags;
	/** Machine Tags (Comma-Separated) */
	private Text machineTags;
	/** Longitude */
	private Text longitude;
	/** Latitude */
	private Text latitude;
	/** Photo/Video Download Url */
	private Text downloadUrl;

	private Text geoHash;

	public RawDataWritable() {
		identifier = new Text();
		dateTaken = new Text();
		captureDevice = new Text();
		title = new Text();
		description = new Text();
		userTags = new Text();
		machineTags = new Text();
		longitude = new Text();
		latitude = new Text();
		downloadUrl = new Text();
		geoHash = new Text();
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		result = prime * result + ((longitude == null) ? 0 : longitude.hashCode());
		result = prime * result + ((latitude == null) ? 0 : latitude.hashCode());
		return result;
	}

	/**
	 * @param identifier
	 * @param dateTaken
	 * @param captureDevice
	 * @param title
	 * @param description
	 * @param userTags
	 * @param machineTags
	 * @param longitude
	 * @param latitude
	 * @param downloadUrl
	 */
	public RawDataWritable(Text identifier, Text dateTaken, Text captureDevice, Text title, Text description, Text userTags, Text machineTags,
			Text longitude, Text latitude, Text downloadUrl) {

		this.identifier = identifier;
		this.dateTaken = dateTaken;
		this.captureDevice = captureDevice;
		this.title = title;
		this.description = description;
		this.userTags = userTags;
		this.machineTags = machineTags;
		this.longitude = longitude;
		this.latitude = latitude;
		this.downloadUrl = downloadUrl;
		this.geoHash = MaponyUtil.getGeoHashPorPrecision(longitude, latitude, MaponyCte.precisionGeoHashAgrupar);
	}

	/**
	 * @see java.lang.Object#toString()
	 * @return 
	 *         identifier+"|"+dateTaken+"|"+captureDevice+"|"+title+"|"+description+"|"+userTags+"|"+machineTags
	 *         +"|"+longitude+"|"+latitude+"|"+downloadUrl
	 */
	public String toString() {
		return identifier + MaponyCte.PIPE + dateTaken + MaponyCte.PIPE + captureDevice + MaponyCte.PIPE + title + MaponyCte.PIPE + description
				+ MaponyCte.PIPE + userTags + MaponyCte.PIPE + machineTags + "|" + longitude + MaponyCte.PIPE + latitude + MaponyCte.PIPE
				+ downloadUrl;

	}

	public void write(DataOutput out) throws IOException {
		identifier.write(out);
		dateTaken.write(out);
		captureDevice.write(out);
		title.write(out);
		description.write(out);
		userTags.write(out);
		machineTags.write(out);
		longitude.write(out);
		latitude.write(out);
		downloadUrl.write(out);
		geoHash.write(out);
	}

	public void readFields(DataInput in) throws IOException {
		identifier.readFields(in);
		dateTaken.readFields(in);
		captureDevice.readFields(in);
		title.readFields(in);
		description.readFields(in);
		userTags.readFields(in);
		machineTags.readFields(in);
		longitude.readFields(in);
		latitude.readFields(in);
		downloadUrl.readFields(in);
		geoHash.readFields(in);
	}

	/**
	 * @return the identifier
	 */
	public final Text getIdentifier() {
		return identifier;
	}

	/**
	 * @param identifier
	 *            the identifier to set
	 */
	public final void setIdentifier(Text identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return the dateTaken
	 */
	public final Text getDateTaken() {
		return dateTaken;
	}

	/**
	 * @param dateTaken
	 *            the dateTaken to set
	 */
	public final void setDateTaken(Text dateTaken) {
		this.dateTaken = dateTaken;
	}

	/**
	 * @return the captureDevice
	 */
	public final Text getCaptureDevice() {
		return captureDevice;
	}

	/**
	 * @param captureDevice
	 *            the captureDevice to set
	 */
	public final void setCaptureDevice(Text captureDevice) {
		this.captureDevice = captureDevice;
	}

	/**
	 * @return the title
	 */
	public final Text getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public final void setTitle(Text title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public final Text getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public final void setDescription(Text description) {
		this.description = description;
	}

	/**
	 * @return the userTags
	 */
	public final Text getUserTags() {
		return userTags;
	}

	/**
	 * @param userTags
	 *            the userTags to set
	 */
	public final void setUserTags(Text userTags) {
		this.userTags = userTags;
	}

	/**
	 * @return the machineTags
	 */
	public final Text getMachineTags() {
		return machineTags;
	}

	/**
	 * @param machineTags
	 *            the machineTags to set
	 */
	public final void setMachineTags(Text machineTags) {
		this.machineTags = machineTags;
	}

	/**
	 * @return the longitude
	 */
	public final Text getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public final void setLongitude(Text longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	public final Text getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public final void setLatitude(Text latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the downloadUrl
	 */
	public final Text getDownloadUrl() {
		return downloadUrl;
	}

	/**
	 * @param downloadUrl
	 *            the downloadUrl to set
	 */
	public final void setDownloadUrl(Text downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	/**
	 * @return the geoHash
	 */
	public final Text getGeoHash() {
		return geoHash;
	}

	/**
	 * @param geoHash
	 *            the geoHash to set
	 */
	public final void setGeoHash(Text geoHash) {
		this.geoHash = geoHash;
	}

}
