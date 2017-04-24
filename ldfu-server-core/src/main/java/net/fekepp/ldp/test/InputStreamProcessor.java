package net.fekepp.ldp.test;

import java.io.IOException;
import java.io.InputStream;

public interface InputStreamProcessor {
	public void process(InputStream input) throws IOException;
}
