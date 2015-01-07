package org.utad.analisisLogs.ejemplos.hadoop.writables;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.RawComparator;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * This class is a copy of {@link IntWritable} that
 * can be used as reference with exercises writing your
 * own writables.
 */
public class IntWritableCopy implements WritableComparable<IntWritableCopy> {
  private int value;

  public IntWritableCopy() {}

  public IntWritableCopy(int value) { set(value); }

  public void set(int value) { this.value = value; }
  public int get() { return value; }

  @Override
  public String toString() {
    return Integer.toString(value);
  }
  
  // ********************************************
  // * Important methods to override from Object
  // ********************************************

  /**
   * HashCode must be implemented properly. Otherwise, Hadoop
   * MapReduce won't work properly, as the {@link Partition}
   * won't route properly tuples to the corresponding reducer. 
   */
  @Override
  public int hashCode() {
    return value;
  }
  
  /**
   * Convenient to implement
   */
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof IntWritableCopy))
      return false;
    IntWritableCopy other = (IntWritableCopy)o;
    return this.value == other.value;
  }

  
  // *********************
  // * Writable interface
  // *********************
  public void readFields(DataInput in) throws IOException {
    value = in.readInt();
  }

  public void write(DataOutput out) throws IOException {
    out.writeInt(value);
  }

  // *********************
  // * Comparable interface
  // *********************  
  
  /**
   * Hadoop would use this method if no {@link WritableComparator}
   * is registered with the method {@link WritableComparator#define(Class, WritableComparator)}
   * or no custom {@link RawComparator} is provided in Job configuration.
   * 
   * It is not recommended this method of comparison as it is
   * highly inefficient. It is recommended to use {@link WritableComparator}
   * or {@link RawComparator}
   * 
   * Anyway, it is interesting to have this method implemented as
   * can be useful for testing, or other Java code.
   */
  @Override
  public int compareTo(IntWritableCopy o) {
    int thisValue = this.value;
    int thatValue = o.value;
    return (thisValue<thatValue ? -1 : (thisValue==thatValue ? 0 : 1));
  }

  // ***********************************
  // * More efficient way of comparison 
  // ***********************************  
  
  /** A Comparator optimized for IntWritable. */ 
  public static class Comparator extends WritableComparator {
    public Comparator() {
      super(IntWritableCopy.class);
    }
    
    @Override
    public int compare(byte[] b1, int start1, int length1,
                       byte[] b2, int start2, int length2) {
      int thisValue = readInt(b1, start1);
      int thatValue = readInt(b2, start2);
      return (thisValue<thatValue ? -1 : (thisValue==thatValue ? 0 : 1));
    }
  }

  /**
   * Comparator registration. Hadoop visits
   * this registry to check if there is any
   * WritableComparator defined. This comparator
   * can be overriding by setting a custom one
   * in the Job configuration.
   */
  static {                                
    WritableComparator.define(IntWritableCopy.class, new Comparator());
  }
}
