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
import java.util.HashSet;
import java.util.Set;

public class MeasurementTypes implements Serializable {
	private static final long serialVersionUID = 1L;

	private static Set<String> types = new HashSet<>();
	private static MeasurementTypes instance = null;
	private static File typesFile = new File(
			recipe.Resources.RESOURCE_DIRECTORY.toString(),
			"MeasurementTypes.ser");

	private MeasurementTypes() {
		types = new HashSet<>();
	}

	public static MeasurementTypes getInstance() {
		if (instance == null) {
			try (InputStream is = new FileInputStream(typesFile);
					InputStream bufferIn = new BufferedInputStream(is);
					ObjectInput input = new ObjectInputStream(bufferIn)) {
				instance = (MeasurementTypes) input.readObject();
				return instance;
			}catch(FileNotFoundException e){
				try {
					Files.createFile(typesFile.toPath());
				} catch (IOException e1) {
					instance = new MeasurementTypes();
					return instance;
				}
			}catch (ClassNotFoundException e) {
				instance = new MeasurementTypes();
				return instance;
			} catch (IOException e) {
				instance = new MeasurementTypes();
				return instance;
			}
		}
		return instance;
	}

	public Set<String> getTypes() {
		return types;
	}

	public boolean storeTypes(Set<String> types) {
		try (OutputStream os = new FileOutputStream(typesFile);
				OutputStream bufferOut = new BufferedOutputStream(os);
				ObjectOutput output = new ObjectOutputStream(bufferOut)) {
			MeasurementTypes.types = types;
			output.writeObject(instance);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}
