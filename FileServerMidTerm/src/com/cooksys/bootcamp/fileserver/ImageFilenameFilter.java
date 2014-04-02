package com.cooksys.bootcamp.fileserver;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class ImageFilenameFilter implements FilenameFilter {
	ArrayList<String> extensions = new ArrayList<String>();

	public ImageFilenameFilter(String[] extensions) {
		// for each extension
		for (String ext : extensions)
			// add to the list of image extenions
			this.extensions.add(ext);
	}

	@Override
	public boolean accept(File dir, String name) {
		boolean ret = false;
		if (name.lastIndexOf('.') > 0) {
			// get last index for '.' char
			int lastIndex = name.lastIndexOf('.');

			// get extension
			String str = name.substring(lastIndex + 1);

			// match path name extension
			if (extensions.contains(str)) {
				ret = true;
			}
		}
		return ret;
	}

}
