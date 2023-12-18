
public class StoreReservationStation   {
	public int Latency;
	public int Address;
	public boolean Busy;
	public Float  V;
	public String Q;
	public String name;
	public int c;
	boolean ready ;
	public StoreReservationStation(String name) {
	
		this.Busy=false;
		this.name=name;
		
	}
	public void setC(int c) {
		this.c = c;
	}
	

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public void setLatency(int latency) {
		Latency = latency;
	}

	public int getLatency() {
		return Latency;
	}
	public int getAddress() {
		return Address;
	}
	public boolean isBusy() {
		return Busy;
	}
	public Float getV() {
		return V;
	}
	public String getQ() {
		return Q;
	}
	public void setAddress(int address) {
		Address = address;
	}
	public void reset() {
		this.setAddress(0);
		this.setBusy(false);
		this.setC(0);
		this.setReady(false);
		this.setLatency(0);
		this.setQ(null);
		this.setV(null);
	}
	public void setBusy(boolean busy) {
		Busy = busy;
	}
	public void setV(Float v) {
		V = v;
	}
	public void setQ(String q) {
		Q = q;
	}
	public String getName() {
		return name;
	}
	public boolean isReady() {
		return ready;
	}
	
	public int getC() {
		return c;
	}
	
	public void print() {
		System.out.println("Time"+" , " +"Name"+":"+"Address"+"busy"+","+"V"+","+"Q");
		System.out.println(c+"  "+name+" "+Address+Busy+" "+V+" "+Q);
	}
	
}
