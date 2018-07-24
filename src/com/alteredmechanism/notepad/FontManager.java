package com.alteredmechanism.notepad;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FontManager {

	public static final String BASE_DIR = "fonts";

	public static final String[] fontFiles = new String[] { "anonymous-pro/Anonymous Pro B.ttf",
			"anonymous-pro/Anonymous Pro BI.ttf", "anonymous-pro/Anonymous Pro I.ttf",
			"anonymous-pro/Anonymous Pro.ttf", "monofur/monof55.ttf", "monofur/monof56.ttf" };

	private Map fonts = new HashMap();

	public FontManager() throws FontFormatException, IOException {
		StringBuffer path = new StringBuffer();

		for (int i = 0; i < fontFiles.length; i++) {
			path.setLength(0);
			path.append(BASE_DIR);
			path.append('/');
			path.append(fontFiles[i]);
			InputStream stream = null;
			try {
				stream = ClassLoader.getSystemClassLoader().getResourceAsStream(path.toString());
				Font font = Font.createFont(Font.TRUETYPE_FONT, stream);
				fonts.put(font.getName(), font);
			}
			finally {
				close(stream);
			}
		}
	}

	private void close(InputStream stream) {
		if (stream != null) {
			try {
				stream.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public Map getFonts() {
		return fonts;
	}

}