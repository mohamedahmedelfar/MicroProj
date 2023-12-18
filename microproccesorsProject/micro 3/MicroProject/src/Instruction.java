import java.util.Arrays;
// import java.util.List;


	 class Instruction {
		    public  String opcode;
		    public String[] operands;
			public String operand1;
		    public  String destination;
		    public int Address;
		    public int imm;
			public String label;
			

		  public Instruction(String opcode, String operand1 ,String label){
			this.opcode=opcode;
			this.operand1=operand1;
			this.label=label;
		   }

		   public Instruction(String opcode, String operand1 , String destination,int imm){
			this.opcode=opcode;
			this.operand1=operand1;
			this.imm=imm;
			this.destination=destination;
		   }

		    public Instruction(String opcode, String[] operands, String destination) {
		      this.opcode = opcode;
		      this.operands = operands;
		      this.destination = destination;
		    }
		    
		    public Instruction(String opcode,int Address,String destination) {
		    	this.opcode=opcode;
		    	this.Address=Address;
		    	this.destination=destination;
		    }
			public Instruction(String label, String opcode, String operand1 ,int imm , String destination){
			this.label=label;
			this.opcode=opcode;
			this.operand1=operand1;
			this.imm=imm;
			this.destination=destination;
		   }

		    public Instruction(String label,String opcode, String[] operands, String destination) {
		      this.label=label;
			  this.opcode = opcode;
		      this.operands = operands;
		      this.destination = destination;
		    }
		    
		    public Instruction(String label,String opcode,int Address,String destination) {
		    	this.label=label;
				this.opcode=opcode;
		    	this.Address=Address;
		    	this.destination=destination;
		    }
			
		    public String getOpcode() {
		      return opcode;
		    }

			public String getOperand1(){
			  return operand1;
			}

			public int getImm(){
				return imm;
			}
			public String getLabel(){
				return label;
			}

		    public String[] getOperands() {
		      return operands;
		    }

		    public String getDestination() {
		      return destination;
		    }

		    public int getAddress() {
				return Address;
			}

			public void setAddress(int address) {
				Address = address;
			}
			@Override
		    public String toString() {
				if(this.operands!=null) {
				      return opcode + " " + Arrays.toString(operands) + " " + destination;
				}
				else {
					   return opcode + " " + Address + " " + destination;
				}
		
		    }
		    
		   
}
