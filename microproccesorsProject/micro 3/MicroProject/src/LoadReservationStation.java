public class LoadReservationStation {
public int Latency;
public int Address;
public boolean Busy;
public String name;
public int c;
public String destination;

public LoadReservationStation(String name ) {

	this.Busy=false;
	this.name=name;
}


public void setName(String name) {
	this.name = name;
}


public String getDestination() {
	return destination;
}


public void setDestination(String destination) {
	this.destination = destination;
}


public void setLatency(int latency) {
	Latency = latency;
}

public String getName() {
	return name;
}



public int getAddress() {
	return Address;
}

public void setAddress(int address) {
	Address = address;
}

public boolean isBusy() {
	return Busy;
}

public void setBusy(boolean busy) {
	Busy = busy;
}

public int getLatency() {
	return Latency;
}

public int getC() {
	return c;
}

public void setC(int c) {
	this.c = c;
}

public void print() {
	System.out.println("Time"+", "+ "Name"+":"+"Address"+"busy");
	System.out.println(c+"  "+name+" "+Address+Busy);
}

}
