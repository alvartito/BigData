package pd04.reduceJoinYMapJoin.sol;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

public class SolUnionDatosMJ implements Writable {
	BooleanWritable isEmployee;

	// SELECT ID empleado, nombre, apellido, fecha incorporacion, sueldo, fecha
	// inicio, fecha final FROM load_salaries.dump.clean JOIN
	// load_employees.dump.clean ON ID empleado;

	// 10001,60117,'1986-06-26','1987-06-26'
	IntWritable sueldo;
	Text fechaIni;
	Text fechaFin;

	// 10001,'1953-09-02','Georgi','Facello','M','1986-06-26'
	Text nombre;
	Text apellido;
	Text fechaInc;
	Text dptName;
	
	public SolUnionDatosMJ() {
		isEmployee = new BooleanWritable();
		sueldo = new IntWritable();
		fechaIni = new Text();
		fechaFin = new Text();
		nombre = new Text();
		apellido = new Text();
		fechaInc = new Text();
		dptName = new Text();
	}

	public SolUnionDatosMJ(BooleanWritable isEmployee, Text nombre,
			Text apellido, Text fechaInc, Text dptName) {
		this.isEmployee = isEmployee;
		sueldo = new IntWritable();
		fechaIni = new Text();
		fechaFin = new Text();
		this.nombre = nombre;
		this.apellido = apellido;
		this.fechaInc = fechaInc;
		this.dptName = dptName;
	}

	public SolUnionDatosMJ(BooleanWritable isEmployee, IntWritable sueldo,
			Text fechaIni, Text fechaFin) {
		this.isEmployee = isEmployee;
		this.sueldo = sueldo;
		this.fechaIni = fechaIni;
		this.fechaFin = fechaFin;
		nombre = new Text();
		apellido = new Text();
		fechaInc = new Text();
		dptName = new Text();
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		isEmployee.readFields(in);
		sueldo.readFields(in);
		fechaIni.readFields(in);
		fechaFin.readFields(in);
		nombre.readFields(in);
		apellido.readFields(in);
		fechaInc.readFields(in);
		dptName.readFields(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		isEmployee.write(out);
		sueldo.write(out);
		fechaIni.write(out);
		fechaFin.write(out);
		nombre.write(out);
		apellido.write(out);
		fechaInc.write(out);
		dptName.write(out);
	}

	public Text getDptName() {
		return dptName;
	}

	public void setDptName(Text dptName) {
		this.dptName = dptName;
	}

	public BooleanWritable getIsEmployee() {
		return isEmployee;
	}

	public void setIsEmployee(BooleanWritable isEmployee) {
		this.isEmployee = isEmployee;
	}

	public IntWritable getSueldo() {
		return sueldo;
	}

	public void setSueldo(IntWritable sueldo) {
		this.sueldo = sueldo;
	}

	public Text getFechaIni() {
		return fechaIni;
	}

	public void setFechaIni(Text fechaIni) {
		this.fechaIni = fechaIni;
	}

	public Text getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Text fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Text getNombre() {
		return nombre;
	}

	public void setNombre(Text nombre) {
		this.nombre = nombre;
	}

	public Text getApellido() {
		return apellido;
	}

	public void setApellido(Text apellido) {
		this.apellido = apellido;
	}

	public Text getFechaInc() {
		return fechaInc;
	}

	public void setFechaInc(Text fechaInc) {
		this.fechaInc = fechaInc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((apellido == null) ? 0 : apellido.hashCode());
		result = prime * result + ((dptName == null) ? 0 : dptName.hashCode());
		result = prime * result
				+ ((fechaFin == null) ? 0 : fechaFin.hashCode());
		result = prime * result
				+ ((fechaInc == null) ? 0 : fechaInc.hashCode());
		result = prime * result
				+ ((fechaIni == null) ? 0 : fechaIni.hashCode());
		result = prime * result
				+ ((isEmployee == null) ? 0 : isEmployee.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + ((sueldo == null) ? 0 : sueldo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SolUnionDatosMJ))
			return false;
		SolUnionDatosMJ other = (SolUnionDatosMJ) obj;
		if (apellido == null) {
			if (other.apellido != null)
				return false;
		} else if (!apellido.equals(other.apellido))
			return false;
		if (dptName == null) {
			if (other.dptName != null)
				return false;
		} else if (!dptName.equals(other.dptName))
			return false;
		if (fechaFin == null) {
			if (other.fechaFin != null)
				return false;
		} else if (!fechaFin.equals(other.fechaFin))
			return false;
		if (fechaInc == null) {
			if (other.fechaInc != null)
				return false;
		} else if (!fechaInc.equals(other.fechaInc))
			return false;
		if (fechaIni == null) {
			if (other.fechaIni != null)
				return false;
		} else if (!fechaIni.equals(other.fechaIni))
			return false;
		if (isEmployee == null) {
			if (other.isEmployee != null)
				return false;
		} else if (!isEmployee.equals(other.isEmployee))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (sueldo == null) {
			if (other.sueldo != null)
				return false;
		} else if (!sueldo.equals(other.sueldo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "sueldo=" + sueldo + ", fechaIni=" + fechaIni
				+ ", fechaFin=" + fechaFin + ", nombre=" + nombre
				+ ", apellido=" + apellido + ", fechaInc=" + fechaInc 
				+ ", departamento=" + dptName;
	}

}
