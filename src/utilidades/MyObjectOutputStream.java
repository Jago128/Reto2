package utilidades;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class MyObjectOutputStream extends ObjectOutputStream {
	protected void writeStreamHeader() throws IOException {
		reset();
	}
	public MyObjectOutputStream () throws IOException {
		super();
	}
	public MyObjectOutputStream(OutputStream out) throws IOException {
		super(out);
	}

}
