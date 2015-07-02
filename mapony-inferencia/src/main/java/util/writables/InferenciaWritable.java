package util.writables;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

import util.MaponyUtil;
import util.constantes.MaponyCte;

/** @author Álvaro Sánchez Blasco
 *
 */
public class InferenciaWritable extends RawDataWritable implements Writable {
	
	private Text geoHashCiudad;
	private Text pais;
	private Text ciudad;
	private Text continente;

	public InferenciaWritable() {
		super();
		this.ciudad = new Text();
		this.pais = new Text();
		this.continente = new Text();
	}

	public InferenciaWritable(RawDataWritable rdw) throws Exception {
		super(rdw);
		this.ciudad = new Text();
		this.pais = new Text();
		this.continente = new Text();
		this.geoHashCiudad = MaponyUtil.getGeoHashPorPrecision(rdw.getLongitude(), rdw.getLatitude(), MaponyCte.precisionGeoHashCiudad);
	}

	/**
	 * @return the geoHashCiudad
	 */
	public final Text getGeoHashCiudad() {
		return geoHashCiudad;
	}

	/**
	 * @param geoHashCiudad the geoHashCiudad to set
	 */
	public final void setGeoHashCiudad(Text geoHashCiudad) {
		this.geoHashCiudad = geoHashCiudad;
	}

	/**
	 * @return the pais
	 */
	public final Text getPais() {
		return pais;
	}

	/**
	 * @param pais the pais to set
	 */
	public final void setPais(Text pais) {
		this.pais = pais;
	}

	/**
	 * @return the ciudad
	 */
	public final Text getCiudad() {
		return ciudad;
	}

	/**
	 * @param ciudad the ciudad to set
	 */
	public final void setCiudad(Text ciudad) {
		this.ciudad = ciudad;
	}

	/**
	 * @return the continente
	 */
	public final Text getContinente() {
		return continente;
	}

	/**
	 * @param continente the continente to set
	 */
	public final void setContinente(Text continente) {
		this.continente = continente;
	}

}
