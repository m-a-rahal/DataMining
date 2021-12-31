package input_output_classes;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import data.Dataset;

public class URLManager {
	public static String default_dataset_url = "https://archive.ics.uci.edu/ml/machine-learning-databases/00236/seeds_dataset.txt";
	public static String temp_download_file = "resources/temp~.txt";

	public static Dataset extract_dataset(String dataset_url) throws IOException {
		if (dataset_url == null) {
			dataset_url = default_dataset_url;
		}

		InputStream in = new URL(dataset_url).openStream();
		Files.copy(in, Paths.get(temp_download_file), StandardCopyOption.REPLACE_EXISTING);
		Dataset dataset = FileManager.extract_dataset(temp_download_file);

		// try to delete the temp file of the downloaded dataset untill sucessfull (because it might still be used by the download thread and locked)
		while (true) {
			try {
				Files.delete(Paths.get(temp_download_file));
				break;
			} catch (Exception e) {}
		}

		return dataset;
	}
}
