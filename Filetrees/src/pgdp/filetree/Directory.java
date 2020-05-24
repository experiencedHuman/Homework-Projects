package pgdp.filetree;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class Directory extends File {

	private final List<File> fileS;

	private int actualFile = -1;
	private boolean returnedItself = false;
	private List<File> actualFiles = new ArrayList<>();

	public Directory(Path path, List<File> files) {
		super(path);
		this.fileS = files;
	}

	@Override
	public Iterator<File> iterator() {
		return new Iterator<>() {
			@Override
			public boolean hasNext() {
				if (actualFile == -1) {
					actualFiles.addAll(fileS);
					actualFile = fileS.isEmpty() ? -1 : actualFile + 1;
				}
				return actualFile > -1 && actualFile < actualFiles.size();
			}

			@Override
			public File next() {
				if (!hasNext())
					throw new NoSuchElementException("no elements left!");
				if (!returnedItself) {
					returnedItself = true;
					return Directory.this;
				}
				File ret = actualFiles.get(actualFile);
				if (!Files.isRegularFile(ret.getPath()))
					actualFiles.addAll(((Directory)ret).getFiles());
				actualFile++;
				return ret;
			}
		};
	}

	@Override
	public int getHeight() {
		if (fileS == null || fileS.isEmpty()) {
			return 0;
		}
		int max = 0;
		for (File file : fileS) {
			int height = file.getHeight();
			if (max < height)
				max = height;
		}
		return 1 + max;
	}

	@Override
	public boolean isRegularFile() {
		return false;
	}

	public List<File> getFiles() {
		return fileS;
	}
}
