package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TextReader {

	private final String link;
	private final URL url;
	private final BufferedReader in;
	private boolean closed = false;
	private final List<String> text = new ArrayList();
	public TextReader(String link, boolean close) throws MalformedURLException, IOException {
		this.link = link;
		this.url = new URL(link);
		this.in = new BufferedReader(new InputStreamReader(url.openStream()));
		while (getReader().readLine() != null) {
			this.text.add(getReader().readLine());
		}
		if (close) {
			in.close();
			this.closed = true;
		}
	}
	public String getLink() {
		return link;
	}
	public URL getURL() {
		return url;
	}
	public void close() throws IOException {
		if (!closed) {
			getReader().close();
		}
	}
	public BufferedReader getReader() {
		return in;
	}
	public List<String> getDocument() {
		return text;
	}
}	
