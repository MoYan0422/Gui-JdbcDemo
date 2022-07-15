package tw.Project_1.material.fileHandling;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class MyFileFilter extends FileFilter{

	@Override
	public boolean accept(File f) {
		String fileName = f.getName();
		fileName = fileName.substring(fileName.lastIndexOf(".")+1);
		if (fileName.equals("csv")) {
			return true;
		} else if (fileName.equals("json")) {
			return true;
		}
		return false;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}


}
