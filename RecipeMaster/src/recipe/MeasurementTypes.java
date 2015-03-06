package recipe;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class MeasurementTypes implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static File typesFile = new File(recipe.Constants.FileConstants.RESOURCE_DIRECTORY.toString(), "MeasurementTypes.ser");
	
	public static Set<MeasurementType> getMeasurementTypes() {
		SortedSet<MeasurementType> typeSet = new TreeSet<>();
		try (InputStream is = new FileInputStream(typesFile);
				InputStream bufferIn = new BufferedInputStream(is);
				ObjectInput input = new ObjectInputStream(bufferIn)) {
			MeasurementType type = null;
			while((type = (MeasurementType)input.readObject()) != null){
				typeSet.add(type);
			}
		}catch(FileNotFoundException e){
			try {
				Files.createFile(typesFile.toPath());
			} catch (IOException e1) {
				return typeSet;
			}
		}catch (ClassNotFoundException e) {
			return typeSet;
		} catch (IOException e) {
			return typeSet;
		}
		return typeSet;
	}
	
	public static boolean storeTypes(Set<MeasurementType> types) {
		try (OutputStream os = new FileOutputStream(typesFile);
				OutputStream bufferOut = new BufferedOutputStream(os);
				ObjectOutput output = new ObjectOutputStream(bufferOut)) {
			for(MeasurementType type : types){
				output.writeObject(type);
			}
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
}
