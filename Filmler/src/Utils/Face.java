package Utils;

import java.io.File;

/* A face is an instance of a person in a picture. It belongs to that person.
 * People have photos through faces.
 */
public class Face {
	public File croppedFile; // A cropped photo containing just the person to whom the face belongs
	public String ID;

	public Face(String ID) {
		this.ID = ID;
	}

	public Face(File croppedFile, String ID) {
		this.croppedFile = croppedFile;
		this.ID = ID;
	}

	@Override
	public String toString() {
		return "(photo: " + croppedFile.getName() + ", id:" + ID + ")";
	}
}
