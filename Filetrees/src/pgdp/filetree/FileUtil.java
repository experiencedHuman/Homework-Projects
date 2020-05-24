package pgdp.filetree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtil {

	public static File toFileRepresentation(Path path) throws IOException {
		if (Files.isRegularFile(path)) {
			return new RegularFile(path);

		} else if (Files.isDirectory(path)) {
			Directory dir = new Directory(path, new ArrayList<>());
			callRec(dir.getPath(),dir.getFiles());
			return dir;

		} else throw new RuntimeException("File is neither regular nor a directory");
	}

	private static void callRec(Path path, List<File> files) throws IOException {
		List<Path> paths = Files.list(path).collect(Collectors.toList());
		for (Path p : paths) {
			if (Files.isRegularFile(p)) {
				files.add(new RegularFile(p));
			} else {
				Directory dir = new Directory(p,new ArrayList<>());
				callRec(dir.getPath(), dir.getFiles());
				files.add(dir);
			}
		}
	}

}
