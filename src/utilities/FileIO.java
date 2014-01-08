package utilities;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;

/**
 * Class for File reading and writing methods.
 * @author Michael Bragg
 * Contains load(DataStore) and save() methods
 *
 */
public abstract class FileIO {
	
	private static final String FILENAME = "contacts.txt";
	
	protected DataStore load(DataStore data) {

		if (!new File(FILENAME).exists()) {
			// file does not exist or is directory or isn't readable, create a new data store.
			data = new DataStore();
		} else {
			System.out.print("  Loading data...");

			try (FileInputStream fileInput = new FileInputStream(FILENAME);
					BufferedInputStream bufferedFileInput = new BufferedInputStream(fileInput);	
					ObjectInputStream d = new ObjectInputStream(bufferedFileInput);) {
				data = (DataStore) d.readObject();
			}catch (ClassNotFoundException ex) {
				System.err.println("Class of a serialized object cannot be found. " + ex);
			}catch (InvalidClassException ex) {
				System.err.println("Something is wrong with a class used by serialization. " + ex);
			}catch (StreamCorruptedException ex) {
				System.err.println("Control information in the stream is inconsistent. " + ex);
			} catch (OptionalDataException ex) {
				System.err.println("Primitive data was found in the stream instead of objects. " + ex);
			}catch (IOException ex) {
				System.err.println("I/O error occured. " + ex);
			}
			System.out.println("Complete");
			
		}
		return data;

	}
	
	protected void save(DataStore data) {
		System.out.println("Saving data...");
		try (ObjectOutputStream encode = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(FILENAME)));) {
			encode.writeObject(data);
		} catch (FileNotFoundException ex) {
			System.out.println("File not found: " + ex);
		} catch (InvalidClassException ex) {
			System.err.println("Something is wrong with a class used by serialization: " + ex);
		} catch (NotSerializableException ex) {
			System.err.println("Some object to be serialized does not implement the java.io.Serializable interface: " + ex);
		} catch (IOException ex) {
			System.err.println("write error: " + ex);
		} 
	}

}
