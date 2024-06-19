package replayes;

public class Response {
	private int serial;
	private Message message;

	public Response(int serial, Message message) {
		this.serial = serial;
		this.message = message;
	}

	public int getSerial() {
		return serial;
	}

	public Message getMessage() {
		return message;
	}

	public void setSerial(int serial) {
		this.serial = serial;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

}


