
public class AluReservationStation  {
	public int Latency;

	public boolean Busy;
	public String op;
	public Float vj;
	public Float vk;
	public String Qj;
	public String Qk;
	public String name;
	public String destination;
	
	public int c;
	public boolean ready;
	public AluReservationStation(String name) {
		
		this.Busy=false;
		this.name=name;
	}

	public String getDestination() {
		return destination;
	}



	public void setDestination(String destination) {
		this.destination = destination;
	}

	public boolean isReady() {
		return ready;
	}



	public void setReady(boolean ready) {
		this.ready = ready;
	}



	public void setLatency(int latency) {
		Latency = latency;
	}



	public void setBusy(boolean busy) {
		Busy = busy;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public void setVj(Float vj) {
		this.vj = vj;
	}

	public void setVk(Float vk) {
		this.vk = vk;
	}

	public void setQj(String qj) {
		Qj = qj;
	}

	public void setQk(String qk) {
		Qk = qk;
	}

	public int getLatency() {
		return Latency;
	}

	

	public boolean isBusy() {
		return Busy;
	}

	public String getOp() {
		return op;
	}

	public void reset() {
		Busy = false;
		op = null;
		vj = null;
		vk = null;
		Qj = null;
		Qk = null;
		c = 0;
		ready = false;
		destination = null;
	}
	

	public Float getVj() {
		return vj;
	}

	public Float getVk() {
		return vk;
	}

	public String getQj() {
		return Qj;
	}

	public String getQk() {
		return Qk;
	}

	public String getName() {
		return name; 
	}
	
	public int getC() {
		return c;
	}



	public void setC(int c) {
		this.c = c;
	}



	public void print() {
		System.out.println("Time"+", "+"Name"+"  "+"busy"+",    "+"op"+",   "+"vj"+",   "+"vk"+",    "+"Qj"+",  "+"Qk");
		System.out.println(c+"   ,"+name+"  ,"+Busy+"   "+op+"   "+vj+"   "+vk+"   "+Qj+"   "+Qk);
	}
	public static void main(String[]args) {
		
		
	}
	
	
}
