/**
//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title:           project 3
// Files:         	HastTable.java, HashTableTest.java
// Course:          CS400, Spring, 2019
//
// Author:          Nick Havlicek
// Email:           nhavlicek@wisc.edu
// Lecturer's Name: Andrew Kuemmel
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ///////////////////
//
// Partner Name:    (name of your pair programming partner)
// Partner Email:   (email address of your programming partner)
// Partner Lecturer's Name: (name of your partner's lecturer)
// 
// VERIFY THE FOLLOWING BY PLACING AN X NEXT TO EACH TRUE STATEMENT:
//   ___ Write-up states that pair programming is allowed for this assignment.
//   ___ We have both read and understand the course Pair Programming Policy.
//   ___ We have registered our team prior to the team registration deadline.
//
///////////////////////////// CREDIT OUTSIDE HELP /////////////////////////////
//
// Students who get help from sources other than their partner must fully 
// acknowledge and credit those sources of help here.  Instructors and TAs do 
// not need to be credited here, but tutors, friends, relatives, room mates, 
// strangers, and others do.  If you received no outside help from either type
//  of source, then please explicitly indicate NONE.
//
// Persons:         (identify each person and describe their help in detail)
// Online Sources:  (identify each URL and describe their assistance in detail)
//
/////////////////////////////// 80 COLUMNS WIDE ///////////////////////////////
*/

import java.util.ArrayList;

/**
 * 
 * @author Nick Stores Key and value pairs using the bucket method of hashing.
 *         Resizes the bucket array when the load factor threshold is reached by
 *         doubling and adding 1 to the size of the bucket array and re
 *         inserting each element
 *
 * @param <K> key
 * @param <V> value
 */
public class HashTable<K extends Comparable<K>, V> implements HashTableADT<K, V> {

	// stores the array of buckets
	private ArrayList<ArrayNode<K, V>> bucketArray;

	// capacity of the arrayList. how many buckets there are total
	private int numBuckets;

	// How long the bucketArray is. used to rehash when load factor is reached
	// size of the array list
	private int size;

	// LF defined as size/numBuckets
	private double loadFactorThreshold;

	// Class that defines a Node that stores a key value pair, as well as a pointer
	// to another node
	class ArrayNode<K, V> {
		K key;
		V value;
		ArrayNode<K, V> next;

		/**
		 * constructor for a new Node
		 * 
		 * @param key
		 * @param value
		 */
		public ArrayNode(K key, V value) {
			this.key = key;
			this.value = value;
		}
	}

	/**
	 * default no-arg constructor with a default size of 5 buckets
	 */
	public HashTable() {
		bucketArray = new ArrayList<>();
		size = 0;
		numBuckets = 5; // arbitrary number

		// initializes the ArrayList to have the right number of buckets.
		for (int i = 0; i < numBuckets; i++) {
			bucketArray.add(null);
		}

	}

	// a constructor that accepts
	// initial capacity and load factor threshold
	// threshold is the load factor that causes a resize and rehash
	public HashTable(int initialCapacity, double loadFactorThreshold) {
		bucketArray = new ArrayList<>();
		size = 0;
		numBuckets = initialCapacity;
		this.loadFactorThreshold = loadFactorThreshold;

		// initializes the ArrayList to have the right number of buckets.
		for (int i = 0; i < numBuckets; i++) {
			bucketArray.add(null);
		}

	}

	/**
	 * hash function that determines which bucket chain the key is placed in.
	 * 
	 * @param key
	 * @return the bucket that the Node should be placed in
	 */
	private int getBucketHash(K key) {
		int hashCode = key.hashCode();
		int bucket = Math.abs(hashCode % numBuckets);
		return bucket;
	}

	@Override
	public void insert(K key, V value) throws IllegalNullKeyException, DuplicateKeyException {
		if (key == null) {
			throw new IllegalNullKeyException();
		}
		// finds head of bucket chain for key
		int bucketIndex = getBucketHash(key);
		ArrayNode<K, V> headOfChain = bucketArray.get(bucketIndex);

		// looks for duplicate key
		while (headOfChain != null) {
			if (headOfChain.key.equals(key)) { // key already exists in bucket chain
				throw new DuplicateKeyException();
			}
			headOfChain = headOfChain.next; // looks at next key
		} // no duplicate key

		// insert the key
		size++;
		headOfChain = bucketArray.get(bucketIndex);// resets headOfChain from while loop
		ArrayNode<K, V> insertNode = new ArrayNode<K, V>(key, value); // new node to insert into chain

		// puts new node at the beginning of the chain
		insertNode.next = headOfChain;
		bucketArray.set(bucketIndex, insertNode);

		// if LF >= LFThreshold, double hash table size then +1

		if (((double) size) / numBuckets >= loadFactorThreshold) {
			ArrayList<ArrayNode<K, V>> tempArray = bucketArray;
			bucketArray = new ArrayList<>();
			numBuckets = (2 * numBuckets) + 1; // doubles the number of chains
			size = 0;
			// adds the new increased number of chains
			for (int i = 0; i < numBuckets; i++) {
				bucketArray.add(null);
			}

			// reinserts every node into the new array with more buckets
			for (ArrayNode<K, V> node : tempArray) { // for each node in temp array
				while (node != null) {
					insert(node.key, node.value);
					node = node.next;
				}

			}
		}

	}

	@Override
	public boolean remove(K key) throws IllegalNullKeyException {
		if (key == null) {
			throw new IllegalNullKeyException();
		}
		// finds head of bucket chain for key
		int bucketIndex = getBucketHash(key);
		ArrayNode<K, V> headOfChain = bucketArray.get(bucketIndex);
		ArrayNode<K, V> last = null;

		// looks for key
		while (headOfChain != null) {
			if (headOfChain.key.equals(key)) { // key found
				break;
			} else { // looks at next key. keeps last key
				last = headOfChain;
				headOfChain = headOfChain.next;
			}
		}
		if (headOfChain == null) {
			return false; // key not found
		}
		// key found, reduce size
		size--;
		if (last != null) {
			last.next = headOfChain.next;// sets lastkey.next to foundkey.next
		} else { // if found key is head of bucket chain, replaces next key as head of chain
			bucketArray.set(bucketIndex, headOfChain.next);
		}
		return true; // key found and removed
	}

	@Override
	public V get(K key) throws IllegalNullKeyException, KeyNotFoundException {
		if (key == null) {
			throw new IllegalNullKeyException();
		}
		// find head of chain for the key
		int bucketIndex = getBucketHash(key);
		ArrayNode<K, V> headOfChain = bucketArray.get(bucketIndex);

		// search the bucket chain
		while (headOfChain != null) {
			if (headOfChain.key.equals(key)) { // key found
				return headOfChain.value;
			}
			headOfChain = headOfChain.next; // next key to search
		}
		throw new KeyNotFoundException(); // key not found
	}

	@Override
	public int numKeys() {
		return size;
	}

	@Override
	public double getLoadFactorThreshold() {
		return loadFactorThreshold;
	}

	@Override
	public double getLoadFactor() {
		double ret = (double) size / (double) numBuckets;
		return ret;
	}

	@Override
	public int getCapacity() {
		return numBuckets;
	}

	@Override
	public int getCollisionResolution() { // 5 CHAINED BUCKET: array of linked nodes
		return 5;
	}
}
