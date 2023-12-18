import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Tomasulo {

  public  Map<String, Object> registerFile;

 
  public Queue<Instruction> instructionQueue;
  public  ArrayList<Float> memory ;
  public AluReservationStation A1;
  public AluReservationStation A2;
  public AluReservationStation M1;
  public AluReservationStation M2;
  public LoadReservationStation L1;
  public LoadReservationStation L2;
  public StoreReservationStation S1;
  public StoreReservationStation S2;
  public int cycle;
  public int add;
  public int sub;
  public int div;
  public int mul;
  public int Ld;
  public int Sd;
  public int bnezl;
  public int addil;
  public boolean issue;
  public boolean flag;
  public boolean stall;
  public boolean branchtaken;
  public static Map<String, Integer> labelTargets = new HashMap<>();
  public boolean instructionIssuedThisCycle;
  
  

  public Tomasulo(Queue<Instruction> instructions,int add,int sub,int mul,int div,int Ld,int Sd, int bnezl,int addil ) {
    // Initialize reservation stations
	  this.A1=new AluReservationStation("A1");
	  this.A2=new AluReservationStation("A2");
	  this.M1=new AluReservationStation("M1");
	  this.M2= new AluReservationStation("M2");
	  this.L1=new LoadReservationStation("L1");
	  this.L2=new LoadReservationStation("L2");
	  this.S1=new StoreReservationStation("S1");
	  this.S2=new StoreReservationStation("S2");
	  this.flag=true;
	// Initialize instruction queue
	    instructionQueue = new LinkedList<>();
	    this.instructionQueue=instructions;
		instructionIssuedThisCycle=false;
		stall=false;
	    
	    
      // Initialize register file
    registerFile = new HashMap<>();
    registerFile.put("F0", 7.0f);
    registerFile.put("F1", 4.0f);
    registerFile.put("F2", 2.0f);
    registerFile.put("F3", 1.0f);
    registerFile.put("F4", 4.0f);
     

	  // Initialize memory
	  memory=new ArrayList<>();
	  this.memory.add(9.0f);
	  this.memory.add(2.0f);
	  this.memory.add(40.0f);
	  this.memory.add(12.0f);
	  this.memory.add(0.0f);
	 
	  //Initialize Latencies
	  this.add=add;
	  this.sub=sub;
	  this.div=div;
	  this.mul=mul;
	  this.Sd=Sd;
	  this.Ld=Ld;
	  this.bnezl=1;
	  this.addil=1;

    issue=true;
    
    cycle = 0;
  }
  


 

 


public void processADDiOperand(AluReservationStation reservationStation, Instruction instruction) {
    String operand = instruction.getOperand1();
    int immediateValue = instruction.getImm();
    
    boolean isOperandString = isOperandOfTypeString(operand);
    
    reservationStation.setReady(true);

    if (isOperandString) {
        reservationStation.setQj((String) registerFile.get(operand));
        reservationStation.setReady(false);
    } else {
        reservationStation.setVj((Float) registerFile.get(operand));
    }

    reservationStation.setVk((float) immediateValue);
    reservationStation.setDestination(instruction.getDestination());
    
    registerFile.put(instruction.getDestination(), reservationStation.getName());
}

public void Runload(LoadReservationStation x) {
    // Check if the reservation station is busy
    if (x.isBusy()) {
        System.out.println(x.getDestination());

        // If the countdown is still going, decrement it
        if (x.getC() > 0) {
            x.setC(x.getC() - 1);
        } else {
            // If the countdown has reached zero, perform the load operations
            Float temp = memory.get(x.getAddress());
            registerFile.put(x.getDestination(), temp);
            Alucheck(x.name, A1, temp);
            Alucheck(x.name, A2, temp);
            Alucheck(x.name, M1, temp);
            Alucheck(x.name, M2, temp);
            storecheckfinal(x.name, S1, temp);
            storecheckfinal(x.name, S2, temp);

            // Reset the reservation station
            x.setBusy(false);
            x.setAddress(0);
            x.setDestination(null);
            x.setLatency(0);
            x.setC(0);
        }
    }
}


public void processSDOperand(StoreReservationStation reservationStation, Instruction instruction) {
    Object destinationValue = registerFile.get(instruction.getDestination());
    
    if (isValueOfTypeString(destinationValue)) {
        reservationStation.setQ((String) destinationValue);
        reservationStation.setReady(false);
    } else {
        reservationStation.setV((Float) destinationValue);
        reservationStation.setReady(true);
    }
}
private boolean isValueOfTypeString(Object value) {
    return value.getClass().isInstance("");
}
 

  
  public void IssueInstruction(Instruction instruction) {
        if (issue == true && !instructionIssuedThisCycle && stall == false) {
            String opcode = instruction.getOpcode();
            switch (opcode) {
                case "ADD.D":
                    if (A1.isBusy() == true && A2.isBusy() == true) {
                        issue = false;
                    } else {
                        instructionQueue.remove();
                        if (A1.isBusy() == false) {
                            A1.setBusy(true);
                            A1.setC(add);
                            A1.setLatency(add);
                            A1.setOp(opcode);
                            processAluOperand(A1, instruction);
                            instructionIssuedThisCycle = true;
                        } else {
                            A2.setBusy(true);
                            A2.setC(add);
                            A2.setLatency(add);
                            A2.setOp(opcode);
                            processAluOperand(A2, instruction);
                            instructionIssuedThisCycle = true;
                        }
                    }
                    break;

                case "SUB.D":
                    if (A1.isBusy() == true && A2.isBusy() == true) {
                        issue = false;
                    } else {
                        instructionQueue.remove();
                        if (A1.isBusy() == false) {
                            A1.setBusy(true);
                            A1.setC(sub);
                            A1.setLatency(sub);
                            A1.setOp(opcode);
                            processAluOperand(A1, instruction);
                            instructionIssuedThisCycle = true;
                        } else {
                            A2.setBusy(true);
                            A2.setC(sub);
                            A2.setLatency(sub);
                            A2.setOp(opcode);
                            processAluOperand(A2, instruction);
                            instructionIssuedThisCycle = true;
                        }
                    }
                    break;

                case "ADDi":
                    if (A1.isBusy() == true && A2.isBusy() == true) {
                        issue = false;
                    } else {
                        instructionQueue.remove();
                        if (A1.isBusy() == false) {
                            A1.setBusy(true);
                            A1.setC(addil);
                            A1.setLatency(addil);
                            A1.setOp(opcode);
                            processADDiOperand(A1, instruction);
                            instructionIssuedThisCycle = true;
                        } else {
                            A2.setBusy(true);
                            A2.setC(addil);
                            A2.setLatency(addil);
                            A2.setOp(opcode);
                            processADDiOperand(A2, instruction);
                            instructionIssuedThisCycle = true;
                        }
                    }
                    break;

                case "DIV.D":
                    if (M1.isBusy() == true && M2.isBusy() == true) {
                        issue = false;
                    } else {
                        instructionQueue.remove();
                        if (M1.isBusy() == false) {
                            M1.setBusy(true);
                            M1.setC(div);
                            M1.setLatency(div);
                            M1.setOp(opcode);
                            processAluOperand(M1, instruction);
                            instructionIssuedThisCycle = true;
                        } else {
                            M2.setBusy(true);
                            M2.setC(div);
                            M2.setLatency(div);
                            M2.setOp(opcode);
                            processAluOperand(M2, instruction);
                            instructionIssuedThisCycle = true;
                        }
                    }
                    break;

                case "MUL.D":
                    if (M1.isBusy() == true && M2.isBusy() == true) {
                        issue = false;
                    } else {
                        instructionQueue.remove();
                        if (M1.isBusy() == false) {
                            M1.setBusy(true);
                            M1.setC(mul);
                            M1.setLatency(mul);
                            M1.setOp(opcode);
                            processAluOperand(M1, instruction);
                            instructionIssuedThisCycle = true;
                        } else {
                            M2.setBusy(true);
                            M2.setC(mul);
                            M2.setLatency(mul);
                            M2.setOp(opcode);
                            processAluOperand(M2, instruction);
                            instructionIssuedThisCycle = true;
                        }
                    }
                    break;

                case "BNEZ":
                    if (A1.isBusy() == false) {
                        A1.setBusy(true);
                        A1.setC(bnezl);
                        A1.setLatency(bnezl);
                        A1.setOp(opcode);
                        handleBNEZ(A1, instruction);
                        instructionIssuedThisCycle = true;
                    } else if (A2.isBusy() == false) {
                        A2.setBusy(true);
                        A2.setC(bnezl);
                        A2.setLatency(bnezl);
                        A2.setOp(opcode);
                        handleBNEZ(A2, instruction);
                        instructionIssuedThisCycle = true;
                    }
                    break;

                case "SD.D":
                    if (S1.isBusy() == true && S2.isBusy() == true) {
                        issue = false;
                    } else {
                        instructionQueue.remove();
                        if (S1.isBusy() == false) {
                            S1.setBusy(true);
                            S1.setC(Sd);
                            S1.setLatency(Sd);
                            S1.setAddress(instruction.getAddress());
                            processSDOperand(S1, instruction);
                            instructionIssuedThisCycle = true;
                        } else {
                            S2.setBusy(true);
                            S2.setC(Sd);
                            S2.setLatency(Sd);
                            S2.setAddress(instruction.getAddress());
                            processSDOperand(S2, instruction);
                            instructionIssuedThisCycle = true;
                        }
                    }
                    break;

                case "LD.D":
                    if (L1.isBusy() == true && L2.isBusy() == true) {
                        issue = false;
                    } else {
                        instructionQueue.remove();
                        if (L1.isBusy() == false) {
                            L1.setBusy(true);
                            L1.setC(Ld);
                            L1.setLatency(Ld);
                            L1.setDestination(instruction.getDestination());
                            L1.setAddress(instruction.getAddress());
                            registerFile.put(L1.getDestination(), L1.getName());
                            instructionIssuedThisCycle = true;
                        } else {
                            L2.setBusy(true);
                            L2.setC(Ld);
                            L2.setLatency(Ld);
                            L2.setDestination(instruction.getDestination());
                            L2.setAddress(instruction.getAddress());
                            registerFile.put(L2.getDestination(), L2.getName());
                            instructionIssuedThisCycle = true;
                        }
                    }
                    break;

                default:
                    // Handle default case or throw an exception if needed
                    break;
            }
        }
    }
	

		  
  

	  public void handleBNEZ(AluReservationStation station, Instruction instr) {
		String regIdentifier = instr.getOperand1();
		String instructionLabel = instr.getLabel();
		station.setReady(true);
	
		Object regValue = registerFile.get(regIdentifier);
	
		if (regValue instanceof String) {
			station.setQj((String) regValue);
			station.setReady(false);
		} else {
			station.setVj((Float) regValue);
		}
	
		station.setVk(0.0f); // BNEZ always compares against zero
		station.setQk(instructionLabel);
	
		station.setDestination(instructionLabel);
	}
	
	
 public void processAluOperand(AluReservationStation reservationStation, Instruction instruction) {
    String[] operands = instruction.getOperands();
    reservationStation.setReady(true);

    if (isOperandOfTypeString(operands[0])) {
        reservationStation.setQj((String) registerFile.get(operands[0]));
        reservationStation.setReady(false);
    } else {
        reservationStation.setVj((Float) registerFile.get(operands[0]));
    }

    if (isOperandOfTypeString(operands[1])) {
        reservationStation.setQk((String) registerFile.get(operands[1]));
        reservationStation.setReady(false);
    } else {
        reservationStation.setVk((Float) registerFile.get(operands[1]));
    }

    reservationStation.setDestination(instruction.getDestination());
    registerFile.put(instruction.getDestination(), reservationStation.getName());
}

private boolean isOperandOfTypeString(String operand) {
    return registerFile.get(operand).getClass().isInstance("");
}

	


public void Execute() {
    List<Instruction> instructionList = new ArrayList<>(instructionQueue);

    while (flag) {
	instructionIssuedThisCycle = false;
        if (!instructionQueue.isEmpty()) {
            examineIssue();
        }

        RunAlu(A1);
        RunAlu(A2);
        RunAlu(M1);
        RunAlu(M2);
        Runload(L1);
        Runload(L2);
        executeStoreOperation(S1);
        executeStoreOperation(S2);

		if (!instructionQueue.isEmpty()) {
            Instruction currentInstruction = instructionQueue.peek();

            if (!stall) {
                if (branchtaken) {
                    
                    branchtaken = false;
					
					
                    int labelIndex = locateInstructionWithLabel(currentInstruction.getLabel(), instructionList);
                    if (labelIndex != -1) {
                        
                        instructionQueue = new LinkedList<>(instructionList.subList(labelIndex, instructionList.size()));
                    }
                } else {
					
                    IssueInstruction(instructionQueue.peek());
                }
				
            }
        }

        System.out.println("Cycle:" + cycle);
        A1.print();
        System.out.println(" ");
        A2.print();
        System.out.println(" ");
        M1.print();
        System.out.println(" ");
        M2.print();
        System.out.println(" ");
        S1.print();
        System.out.println(" ");
        S2.print();
        System.out.println(" ");
        L1.print();
        System.out.println(" ");
        L2.print();
        System.out.println(" ");

        System.out.println("Register File Content: " + registerFile);
        
        System.out.println("Memory Content: " + memory);

        // Print the instruction queue content
        System.out.println("Instruction Queue Content: " + instructionQueue);



        cycle++;
       
		System.out.println(registerFile.get("F4"));

        if (A1.isBusy() == false && A2.isBusy() == false && M1.isBusy() == false && M2.isBusy() == false &&
                S1.isBusy() == false && S2.isBusy() == false && L1.isBusy() == false && L2.isBusy() == false) {
            flag = false;
            break;
        }
    }
}

private int locateInstructionWithLabel(String label, List<Instruction> instructions) {
    int position = 0;
    for (Instruction instruction : instructions) {
        if (instruction.getLabel() != null && instruction.getLabel().equals(label) && !instruction.getOpcode().equals("BNEZ")) {
            return position;
        }
        position++;
    }
    return -1; // Return -1 if the label is not found
}



private void clearInstructionsUpToIndex(int endIndex) {
	if (endIndex >= 0) {
		for (int i = 0; i <= endIndex; i++) {
			instructionQueue.remove();
		}
	}
}
public void executeStoreOperation(StoreReservationStation storeUnit) {
    if(storeUnit.isBusy() && storeUnit.isReady()) {
        if(storeUnit.getC() > 0) {
            storeUnit.setC(storeUnit.getC() - 1);
        } else {
            memory.add(storeUnit.getAddress(), storeUnit.getV());
            System.out.println(memory.get(storeUnit.getAddress()));
            storeUnit.reset();
        }
    }
}
public void Aluoperations(AluReservationStation x) {
    Float temp = 0.0f;
    String op = x.getOp();

    switch (op) {
        case "ADD.D":
        case "ADDi":
            temp = x.getVj() + x.getVk();
            registerFile.put(x.getDestination(), temp);
            break;
        case "SUB.D":
            temp = x.getVj() - x.getVk();
            registerFile.put(x.getDestination(), temp);
            break;
        case "MUL.D":
            temp = x.getVj() * x.getVk();
            registerFile.put(x.getDestination(), temp);
            break;
        case "DIV.D":
            temp = x.getVj() / x.getVk();
            registerFile.put(x.getDestination(), temp);
            break;
        case "BNEZ":
            if (x.vj > 0) {
                branchtaken = true;
                stall = false;
            } else if (x.vj == 0) {
                x.Busy = false;
                for (Instruction i : instructionQueue) {
                    if (i.getOpcode().equals("BNEZ")) {
                        instructionQueue.remove(i);
                        break;
                    }
                }
            }
            break;
        default:
            // Handle other cases or throw an exception
            break;
    }

    Alucheck(x.getName(), A1, temp);
    Alucheck(x.getName(), A2, temp);
    Alucheck(x.getName(), M1, temp);
    Alucheck(x.getName(), M2, temp);
    storecheckfinal(x.name, S1, temp);
    storecheckfinal(x.name, S2, temp);
    x.setBusy(false);
    x.setC(0);
    x.setLatency(0);
    x.setOp(null);
    x.setVj(null);
    x.setVk(null);
    x.setQj(null);
    x.setQk(null);
    x.setReady(false);
}

  
public void RunAlu(AluReservationStation x) {
    // Check if the reservation station is busy and ready
    if (x.isBusy() && x.isReady()) {
        System.out.println(x.getC());

        // If the countdown is still going, decrement it
        if (x.getC() > 0) {
            x.setC(x.getC() - 1);
        } else {
            // If the countdown has reached zero, perform the ALU operations
            Aluoperations(x);
        }
    }
}



public void Alucheck(String x, AluReservationStation y, Float temp) {
    // Check if the reservation station is busy and not ready
    if (y.isBusy() && !y.isReady()) {
        // If the reservation station is waiting for the result of operation x
        if (y.getQj()==x) {
            y.setVj(temp);  // Set the result
            y.setQj(null);  // Clear the dependency
        }

        // If the reservation station is waiting for the result of operation x
        if (y.getQk().equals(x)) {
            y.setVk(temp);  // Set the result
            y.setQk(null);  // Clear the dependency
        }

        // If both operands are ready, set the reservation station to ready
        if (y.getVj() != null && y.getVk() != null) {
            y.setReady(true);
        }
    }
}
private void clearInstructionsUpToIndex2(int endIndex) {
	if (endIndex >= 0) {
		for (int i = 0; i <= endIndex; i++) {
			instructionQueue.remove();
		}
	}
}

public void examineIssue() {
		System.out.println(issue);
		if (issue) {
			// Examine if there are instructions in the queue
			if (!instructionQueue.isEmpty()) {
				Instruction currentInst = instructionQueue.peek();
				String operation = currentInst.getOpcode();
	
				if (operation.equals("BNEZ")) {
					// Examine if BNEZ instruction is ready
					if (!A1.isBusy() || !A2.isBusy()) {
						IssueInstruction(currentInst);
					}
				} else {
					// Examine if other instructions can be issued
					if ((operation.equals("ADD.D") || operation.equals("SUB.D") || operation.equals("ADDi"))
							&& (!A1.isBusy() || !A2.isBusy())) {
						IssueInstruction(currentInst);
					} else if ((operation.equals("DIV.D") || operation.equals("MUL.D"))
							&& (!M1.isBusy() || !M2.isBusy())) {
						IssueInstruction(currentInst);
					} else if (operation.equals("SD.D")
							&& (!S1.isBusy() || !S2.isBusy())) {
						IssueInstruction(currentInst);
					} else if (operation.equals("LD.D")
							&& (!L1.isBusy() || !L2.isBusy())) {
						IssueInstruction(currentInst);
					}
				}
			}
		}
	}
	



public void storecheckfinal(String z, StoreReservationStation rs, Float temp) {
    // If the reservation station is waiting for the result of operation x
    if (rs.getQ() ==z) {
        rs.setV(temp);  // Set the result
        rs.setQ(null);  // Clear the dependency
        rs.setReady(true);  // Set the reservation station to ready
    }
}

  
  public void print() {
	  S1.print();
	  System.out.println(memory.get(2));
  }
 

  
  


public static void main(String[] args) throws IOException {
	
	
	boolean flag = true;
	
    BufferedReader bfn = new BufferedReader(
    new InputStreamReader(System.in));
    
    Queue<Instruction> instructionsT = new LinkedList<>();
    
    while(flag) {
    
    System.out.println("Enter Instruction  : ");
    
    String inst = bfn.readLine();
	
    
    
    String[] comp = inst.split(" ");
	System.out.println(Arrays.toString(comp));
    String func = comp[0];
	if((func.equals("label"))){
		String func1 = comp[1];
		labelTargets.put(func1, instructionsT.size() - 1);
		

	    if (func1.equals("ADDi")) {
		String oper = comp[2];
		String[] ops = oper.split(",");
		String dest = ops[0];
		String op1 = ops[1];
		int imm = Integer.parseInt(ops[2]);
	
		Instruction i = new Instruction(func,func1, op1,imm,dest);
	
		instructionsT.add(i);
	
		System.out.println("Entered Instruction : " + inst);
		System.out.println(" Function : " + func);
		System.out.println(" Operands : " + oper);
		System.out.println(" Destination : " + dest);
		System.out.println(" Register 1 : " + op1);
		System.out.println(" Immediate : " + imm);
		System.out.println(" Instruction Array : " + instructionsT);
	}
    else{
    if(func1.equals("LD.D") || func1.equals("SD.D")) {
        String oper = comp[2];
        String[] ops = oper.split(",");
        String op1 = ops[0];
        int address = Integer.parseInt(ops[1]);

        Instruction i = new Instruction(func,func1,address,op1);
        
        
        instructionsT.add(i);
        
        System.out.println("Entered Instruction : " + inst);
        System.out.println(" Function : " + func);
        System.out.println(" Operands : " + oper);
        System.out.println(" Register : " + op1);
        System.out.println(" Address : " +  address);
        System.out.println(" Instruction Array : " + instructionsT);
    	
    } 
	else {
        String oper = comp[2];
        String[] ops = oper.split(",");
        String dest = ops[0];
        String op1 = ops[1];
        String op2 = ops[2];
        
        String[] operands = new String[] {op1,op2};
        
        
        
        instructionsT.add(new Instruction(func,func1,operands,dest));
        
        System.out.println("Entered Instruction : " + inst);
        System.out.println(" Function : " + func);
        System.out.println(" Operands : " + oper);
        System.out.println(" Destination : " + dest);
        System.out.println(" Register 1 : " + op1);
        System.out.println(" Register 2 : " + op2);
        System.out.println(" Instruction Array : " + instructionsT);
        
    }
}

	}else{
	
	if (func.equals("ADDi")) {
		String oper = comp[1];
		String[] ops = oper.split(",");
		String dest = ops[0];
		String op1 = ops[1];
		int imm = Integer.parseInt(ops[2]);
	
		Instruction i = new Instruction(func, op1,dest,imm);
	
		instructionsT.add(i);
	
		System.out.println("Entered Instruction : " + inst);
		System.out.println(" Function : " + func);
		System.out.println(" Operands : " + oper);
		System.out.println(" Destination : " + dest);
		System.out.println(" Register 1 : " + op1);
		System.out.println(" Immediate : " + imm);
		System.out.println(" Instruction Array : " + instructionsT);
	}
    else{
    if(func.equals("LD.D") || func.equals("SD.D")) {
        String oper = comp[1];
        String[] ops = oper.split(",");
        String op1 = ops[0];
        int address = Integer.parseInt(ops[1]);

        Instruction i = new Instruction(func,address,op1);
        
        
        instructionsT.add(i);
        
        System.out.println("Entered Instruction : " + inst);
        System.out.println(" Function : " + func);
        System.out.println(" Operands : " + oper);
        System.out.println(" Register : " + op1);
        System.out.println(" Address : " +  address);
        System.out.println(" Instruction Array : " + instructionsT);
    	
    } else if (func.equals("BNEZ")) {
		if (comp.length == 2) {
			String[] ops = comp[1].split(",");
			if (ops.length == 2) {
				String op1 = ops[0];
				String label = ops[1];
	
				Instruction i = new Instruction(func, op1, label);
				instructionsT.add(i);
	
				System.out.println("Entered Instruction : " + inst);
				System.out.println(" Function : " + func);
				System.out.println(" Operand : " + op1);
				System.out.println(" Label : " + label);
				System.out.println(" Instruction Array : " + instructionsT);
			} else {
				System.out.println("Invalid BNEZ instruction format. Please use: BNEZ F1,label");
			}
		} else {
			System.out.println("Invalid BNEZ instruction format. Please use: BNEZ F1,label");
		}
	}else {
        String oper = comp[1];
        String[] ops = oper.split(",");
        String dest = ops[0];
        String op1 = ops[1];
        String op2 = ops[2];
        
        String[] operands = new String[] {op1,op2};
        
        
        
        instructionsT.add(new Instruction(func,operands,dest));
        
        System.out.println("Entered Instruction : " + inst);
        System.out.println(" Function : " + func);
        System.out.println(" Operands : " + oper);
        System.out.println(" Destination : " + dest);
        System.out.println(" Register 1 : " + op1);
        System.out.println(" Register 2 : " + op2);
        System.out.println(" Instruction Array : " + instructionsT);
        
    }
}
	}
    
    System.out.println("Add another instruction?  : ");
    
    // String reading internally
    String res = bfn.readLine();
    
    if(res.equalsIgnoreCase("no")) {
    	flag = false;
    }
    
    }
    
    System.out.println("Enter ADD.D Latency : ");
    // Integer reading internally
    int ADDL = Integer.parseInt(bfn.readLine());
    
    System.out.println("Enter SUB.D Latency : ");
    // Integer reading internally
    int SUBL = Integer.parseInt(bfn.readLine());
    
    System.out.println("Enter MUL.D Latency : ");
    // Integer reading internally
    int MULL = Integer.parseInt(bfn.readLine());
    
    System.out.println("Enter DIV.D Latency : ");
    // Integer reading internally
    int DIVL = Integer.parseInt(bfn.readLine());
    
    System.out.println("Enter SD.D Latency : ");
    // Integer reading internally
    int SDL = Integer.parseInt(bfn.readLine());
    
    System.out.println("Enter LD.D Latency : ");
    // Integer reading internally
    int LDL = Integer.parseInt(bfn.readLine());
	int bnezl =1;
	int addil =1;

	
	
	Tomasulo processor = new Tomasulo(instructionsT,ADDL,SUBL,MULL,DIVL,LDL,SDL,bnezl,addil);
	;
	
processor.Execute();



}


  } 

