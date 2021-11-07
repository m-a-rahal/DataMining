package Test;
import java.io.IOException;

import data.Dataset;
import input_sources.FileManager;
import input_sources.URLManager;

public class Test_dataset {
	public static void main(String[] args) throws IOException {
		Dataset dataset = FileManager.extract_dataset(null);
		Dataset dataset2 = URLManager.extract_dataset(null);
		dataset2.show();
	}
}
