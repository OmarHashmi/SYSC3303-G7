package project;

// @author Tom P
public class Message {
	private String msgData = null;
	private String floor = null;
	
	synchronized public void setMessage(String newMessage) {
		while (msgData != null) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		msgData = newMessage;
		this.notify();
	}
	
	synchronized public String getMessage() {
		while(msgData == null) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
		}
		String dataHolder = msgData;
		msgData = null;
		this.notify();
		return dataHolder;
	}
	
	synchronized public void setFloor(String newFloor) {
		while(floor != null) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
		}
		floor = newFloor;
		this.notify();
	}
	
	synchronized public String getFloor() {
		while(floor == null) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
		}
		String dataHolder = floor;
		floor = null;
		this.notify();
		return dataHolder;
	}
}