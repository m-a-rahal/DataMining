package Test;
import java.io.FileNotFoundException;

import data.Dataset;
import data.FileManager;

public class Test_dataset {
	public static void main(String[] args) throws FileNotFoundException {
		Dataset dataset = FileManager.extract_dataset(null);
		dataset.show();
	}
}
