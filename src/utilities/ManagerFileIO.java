package utilities;

import java.io.*;

/**
 * Class for File reading and writing methods.
 * @author Michael Bragg
 * Contains load(ManagerData) and save(ManagerData) methods
 *
 */
public class ManagerFileIO {

	private static final String FILENAME = "contacts.txt";

	public ManagerData load(ManagerData data) {

			if (new File(FILENAME).exists()) {
				System.out.print("  Loading data...");

				try (FileInputStream fileInput = new FileInputStream(FILENAME);
						BufferedInputStream bufferedFileInput = new BufferedInputStream(fileInput);	
						ObjectInputStream d = new ObjectInputStream(bufferedFileInput);) {
					data = (ManagerData) d.readObject();
				} catch (ClassNotFoundException ex) {
					System.err.println("Class of a serialized object cannot be found. " + ex);
				} catch (InvalidClassException ex) {
					System.err.println("Something is wrong with a class used by serialization. " + ex);
				} catch (StreamCorruptedException ex) {
					System.err.println("Control information in the stream is inconsistent. " + ex);
				} catch (OptionalDataException ex) {
					System.err.println("Primitive data was found in the stream instead of objects. " + ex);
				} catch (IOException ex) {
					System.err.println("I/O error occured. " + ex);
				}
				System.out.println("Complete");
			} 
		
		return data; 
	}

	public static void save(ManagerData data) {
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
