/**
 * Filename:   MyProfiler.java
 * Project:    p3b-201901     
 * Authors:    Nick Havlicek
 *
 * Semester:   Spring 2019
 * Course:     CS400
 * 
 * Due Date:   3/28
 * Version:    1.0
 * 
 * Credits:    
 * 
 * Bugs:       
 */

// Used as the data structure to test our hash table against
import java.util.TreeMap;

public class MyProfiler<K extends Comparable<K>, V> {

	HashTableADT<K, V> hashtable;
	TreeMap<K, V> treemap;

	public MyProfiler() {
		hashtable = new HashTable<K, V>(50, 100);
		treemap = new TreeMap<K, V>();

	}

	public void insert(K key, V value) {
		try {
			hashtable.insert(key, value);
			treemap.put(key, value);
		} catch (IllegalNullKeyException e) {
			e.printStackTrace();
		} catch (DuplicateKeyException e) {
			e.printStackTrace();
		}

	}

	public void retrieve(K key) {
		try {
			V hashValue = hashtable.get(key);
			V treeValue = treemap.get(key);
			System.out.println(hashValue + "," + treeValue);
		} catch (IllegalNullKeyException e) {
			e.printStackTrace();
		} catch (KeyNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * main driver method. inserts and retrieves increasing integers from 0 to
	 * numElements - 1 into created hash table and java treeMap
	 * 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			int numElements = Integer.parseInt(args[0]); 
			MyProfiler<Integer, Integer> profile = new MyProfiler<Integer, Integer>();

			for (int i = 0; i < numElements; i++) {
				profile.insert(i, i);
			}
			for (int i = 0; i < numElements; i++) {
				profile.retrieve(i);
			}

			String msg = String.format("Inserted and retreived %d (key,value) pairs", numElements);
			System.out.println(msg);
		} catch (Exception e) {
			System.out.println("Usage: java MyProfiler <number_of_elements>");
			System.exit(1);
		}
	}
}
