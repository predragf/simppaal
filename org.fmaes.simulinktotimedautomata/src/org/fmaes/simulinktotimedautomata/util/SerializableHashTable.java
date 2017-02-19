package org.fmaes.simulinktotimedautomata.util;

import java.util.Enumeration;
import java.util.Hashtable;

public class SerializableHashTable extends Hashtable<String, String> {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public String serialize() {
    Enumeration<String> allKeys = this.keys();
    String serializedHastable = "";
    while (allKeys.hasMoreElements()) {
      String key = (String) allKeys.nextElement();
      String value = (String) this.get(key);
      serializedHastable = serializedHastable.concat(String.format("%s;%s;", key, value));
    }
    return serializedHastable;
  }

  public static SerializableHashTable deserialize(String input) {
    SerializableHashTable serializedHashTable = new SerializableHashTable();
    String[] entries = input.split(";");
    int numOfEntries = entries.length;
    if (numOfEntries % 2 != 0) {
      /*
       * There was some mistake since the number of entries is not 2n maybe throw and exception or
       * log, but that is in the second version.
       */

      return serializedHashTable;
    }
    for (int i = 0; i < (numOfEntries / 2); i++) {
      int takeIndex = 2 * i;
      serializedHashTable.put(entries[takeIndex], entries[takeIndex + 1]);
    }
    return serializedHashTable;
  }

  @Override
  public String toString() {
    return serialize();
  }

  @Override
  public String put(String key, String value) {
    String existingValue = this.get(key);
    if (existingValue == null) {
      super.put(key, value);
    }
    else{
      System.err.println(String.format("%s already in the cache", key));
    }
    return value;
  }

}
