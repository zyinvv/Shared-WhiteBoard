package client;

import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;

public class CanvasFileFilter extends FileFilter {
	public static enum CanvasFilterType {
		OPEN, SAVE, COF_OPEN, COF_SAVE;
		private CanvasFilterType() {
		}
	}

	CanvasFilterType type = CanvasFilterType.OPEN;
	String[] exts;

	public CanvasFileFilter(CanvasFilterType t) {
		type = t;
		if (type == CanvasFilterType.OPEN) {
			exts = ImageIO.getReaderFormatNames();
		} else if (type == CanvasFilterType.SAVE) {
			
			exts = ImageIO.getWriterFormatNames();
		} else {
			exts = new String[1];
			exts[0] = "cof";
		}
	}

	public boolean accept(File f) {
		if (f.isDirectory())
			return true;
		String ext = getExtension(f.getPath());
		for (String s : exts) {
			if (ext.equals(s))
				return true;
		}
		return false;
	}

	public String getDescription() {
		StringBuffer sb = new StringBuffer();
		if ((type == CanvasFilterType.OPEN) || (type == CanvasFilterType.SAVE)) {
			for (String s : exts) {
				s = s.toLowerCase();
				if (sb.indexOf("*." + s + " ") == -1) {
					sb.append("*." + s + " ");
				}
			}
		} else
		{
			sb.append("[Canvas Object File] *.cof");
		}
		return sb.toString();
	}

	public String getExtension(String pth) {
		String ret = "";
		int ind;
		if ((ind = pth.lastIndexOf(".")) > -1) {
			ind++;
			if (ind < pth.length())
				ret = pth.substring(ind).toLowerCase();
		}
		return ret;
	}
}

