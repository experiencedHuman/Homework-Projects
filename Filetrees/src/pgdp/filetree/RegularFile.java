package pgdp.filetree;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RegularFile extends File {

	private boolean used = false;
	public RegularFile(Path path) {
		super(path);
	}

	@Override
	public Iterator<File> iterator() {
		return new Iterator<>() {
			@Override
			public boolean hasNext() {
				return !used;
			}

			@Override
			public File next() {
				if (used)
					throw new NoSuchElementException("no file left");
				used = true;
				return RegularFile.this;
			}
		};
	}

	@Override
	public int getHeight() {
		return 0;
	}

	@Override
	public boolean isRegularFile() {
		return true;
	}

}
