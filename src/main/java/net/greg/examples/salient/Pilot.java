package net.greg.examples.salient;

import java.util.*;

/*
  Kafka message Keys can be Strings or as Avro
  messages - depending upon your Kafka config.

  This demo opts for a <b>Key</b> of type
  <code>String</code> (i.e., not AVRO); this
  demo examines the possible pitfalls of using
  Stringified representation of a scalar
  <code>long</code> value for a (presumably
  "incremental") Key value.

  The ability to programmatically increment the
  <b>Key</b> value is likely the only plausible
  reason to use a scalar (<code>long</code>)
  datatype in this case.

  The mechanics/limitations of a scalar
  <code>long</code> apply equally to an instance
  of the  Wrapper class: <code>Long</code>.

  With this constraint in mind, a hashed <code>String</code>
  representation of the datatype <code>Long</code>
  DOES hash to a different value than the hash of a datatype
  that is a true numerical <code>long/Long</code>).

  There is the cost of boxing/corecion/conversion between
  <code>Long</code> and <code>String</code>, plus the issue
  of "word-tearing" that is emndemic in incrementing a
  scalar <code>long</code> value.


  Consider instead using the following approach:

    <code>java.util.concurrent.atomic.AtomicLong.getAndIncrement().toString()<c/ode>
*/
public final class Pilot {


  private final Long numericLong =
    new Long(Long.MAX_VALUE);

  private final Long strLong =
    Long.parseLong("9223372036854775807"); // Long.MAX_VALUE



  /*
   *
   */
  private void contrastHashedValues(){

    int hashValue4StringLong =
      strLong.hashCode();

    int hashValue4numericLong =
      numericLong.hashCode();

    System.err.println(
      "\n\nActual value, numericLong: " + numericLong +
      "\nhashValue4StringLong[INT]: " + hashValue4StringLong);

    System.err.println(
      "\n\nActual value, strLong: " + strLong +
      "\nhashValue4numericLong[INT]: " + hashValue4numericLong);



    System.err.println(
      "\n\nA. Contrast (before hashing) " + YLW  + "Long2Long \n" + GRN +
      (numericLong.equals(strLong)) + NC);

    System.err.println(
      "\n\nB. Contrast (after hashing) " + YLW +
      "integer2integer " + NC +
      "(scalars, no boxing) \n" + GRN +
      (hashValue4StringLong == hashValue4numericLong) + NC);

  }


  public static final String RED = "\033[1;91m";
  public static final String GRN = "\033[1;92m";
  public static final String YLW = "\033[1;93m";
  public static final String NC = "\u001B[0m";


  public static void main(String[] args) {
    new Pilot().contrastHashedValues();
  }


  static {
    System.err.println(
      "\n\n" + RED + "Provided" + NC + " Kafka converts \"String-Keys to Long-Keys\" by observing/leveraging Java's\n" +
      "canonical hashCode() algorithm/semantics, there shouldn't be any discrepancies \n" +
      "when supplying:  A scalar long;  A Long instance;  A String representation for \n" +
      "any value nominated as a KEY.");
  }
}


/*
  https://docs.streamsets.com/platform-datacollector/latest/datacollector/UserGuide/Pipeline_Configuration/KMessageKey.html

  ßitly:
  https://bit.ly/3cMLQ4u
*/
