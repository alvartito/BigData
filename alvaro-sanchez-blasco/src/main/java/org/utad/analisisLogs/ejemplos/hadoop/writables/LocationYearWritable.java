package org.utad.analisisLogs.ejemplos.hadoop.writables;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.io.WritableUtils;

/**
 * Writable with a Location and a Year
 */
public class LocationYearWritable implements WritableComparable<LocationYearWritable> {
  private String location;
  private int year;

  public LocationYearWritable() {}

  public LocationYearWritable(String location, int year) {
  	this.location = location;
  	this.year = year;
  }

  public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	@Override
  public String toString() {
    return location + "|" + year;
  }
  
  @Override
  public int hashCode() {
  	// XOR between both hash codes
    return year ^ location.hashCode();
  }
  
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof LocationYearWritable))
      return false;
    LocationYearWritable other = (LocationYearWritable)o;
    return (this.year == other.year && this.location.equals(other.location)) ;
  }
  
  // *********************
  // * Writable interface
  // *********************
  public void readFields(DataInput in) throws IOException {
    location = Text.readString(in);
    year = in.readInt();
  }

  public void write(DataOutput out) throws IOException {
    Text.writeString(out, location);
    out.writeInt(year);
  }

  // *********************
  // * Comparable interface
  // *********************  
  
  @Override
  public int compareTo(LocationYearWritable o) {
  	int cmp = location.compareTo(o.location);
  	if (cmp == 0) {
  		return year<o.year ? -1 : (year==o.year ? 0 : 1);
  	}
  	return cmp;
  }

  // ***********************************
  // * More efficient way of comparison 
  // ***********************************  
  
  /** A Comparator optimized for IntWritable. */ 
  public static class Comparator extends WritableComparator {
    public Comparator() {
      super(LocationYearWritable.class);
    }
    
    @Override
    public int compare(byte[] b1, int s1, int l1,
                       byte[] b2, int s2, int l2) {
    	try {
	      int strl1 = readVInt(b1, s1);
	      int strl2 = readVInt(b2, s2);
	      int n1 = WritableUtils.decodeVIntSize(b1[s1]);
	      int n2 = WritableUtils.decodeVIntSize(b2[s2]);
	      int cmp = compareBytes(b1, s1+n1, strl1, b2, s2+n2, strl2);
	
	      if (cmp == 0) {
	        int thisValue = readInt(b1, s1+n1+strl1);
	        int thatValue = readInt(b2, s2+n2+strl2);
	        cmp = (thisValue<thatValue ? -1 : (thisValue==thatValue ? 0 : 1));      	
	      }
	      
	      return cmp;
    	} catch (IOException e) {
    		throw new RuntimeException(e);
    	}
    }
  }

  static {                                
    WritableComparator.define(LocationYearWritable.class, new Comparator());
  }
}
