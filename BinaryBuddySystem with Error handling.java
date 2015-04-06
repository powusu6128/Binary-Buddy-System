import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.io.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class BinaryBuddySystem 
{
	public static int BINARY_MODIFIER = 2;
	public static Scanner scanInt = new Scanner(System.in);
	private static boolean incorrectInput = false;

	public static void main(String[] args) throws IOException 
	{
			do{
				int inputChoiceNum = 0;
				//get input from user from either a File or from Keyboard
				try{
				System.out.println("Please Choose Input Type: File: 1  Keyboard: 2");
				inputChoiceNum = scanInt.nextInt();
				incorrectInput = false;
				}catch(RuntimeException ex){
					scanInt.next();
					
				}
				
			//check if user chooses correct int from menu option above 1 or 2
			if(inputChoiceNum<1 || inputChoiceNum>2)
			{
				System.out.println("Invalid Numeric Input, choose 1 or 2 from menu option");
				incorrectInput=true;
			}
			//if user inputs 1 runFileInput
			else if(inputChoiceNum == 1)
			{
				runFileInput();
			}
			//if user inputs 2 take input from keyboard
			else if(inputChoiceNum == 2)
			{
				int memoryBlockSize = setSystemSize();
				//System.out.println(memoryBlockSize);

				ArrayList<Memory> currentBank = createMemoryBlock(memoryBlockSize);
				System.out.println("Memory Storage Size: " + currentBank.get(0).getValue() + "\n");

				while(0==0)
				{
					do{
						int input = 0;
						incorrectInput=false;
						//allow the user to choose from int 1 - 4 for actions check status, add process, deactivate, exit
						try{
						System.out.println("Please indicate a numeric action:"+"\n"+"1: Check System Status" +"\n"+"2: Add A Process"+"\n"+"3: Deactivate A Process"+"\n"+"4: Exit");
						input = scanInt.nextInt();
						
						}catch(RuntimeException ex1){
							
							scanInt.next();
							
						}
						if(!(input>0 && input<5))
						{
							System.out.println("Invalid Input, please input a number 1 - 4 for correct menu options" + "\n");
							incorrectInput = true;
						}
						else
						{
							if(input==1)
							{
								for(int i = 0; i<currentBank.size();i++)
								{
									System.out.println("Location: "+i+"\t"+"Value: "+currentBank.get(i).getValue()+"\t"+"Active Process? "+currentBank.get(i).getActive()+"\t"+"ID Name: "+currentBank.get(i).getName());
								}
							}
							else if(input==2)
							{
							
								addProcess(currentBank,createProcess(currentBank));
								
							}
							else if(input==3)
							{
								System.out.println("Please input the name of the process to deallocate: ");
								String target = scanInt.next();
								currentBank = deallocate(currentBank, target);
								System.out.println(currentBank);
							}
							else if(input==4)
							{
								System.exit(0);
							}
						}
					}while(incorrectInput);
				}
			}
			else
			{
				incorrectInput=true;
				System.out.println("ERROR: INCORRECT INPUT"+"\n"+"TRY AGAIN");
			}
		}while(incorrectInput);
	}



	//method that takes in input from user to set the size of space for memory storage
	private static int setSystemSize()
	{
		do{
			int size = 0;
			try{
			incorrectInput=false;
			System.out.println("Please input the the size of allocated space for memory storage (rounded to the nearest power of 2 in Mb): ");
			size = scanInt.nextInt();
			
			}catch(RuntimeException ex){
				scanInt.next();
			}
			if(size < 1)
			{
				System.out.println("ERROR, Please input the the size of allocated space for memory storage integer greater than 0 ");
				incorrectInput=true;
			}
			else
			{
				//round user input to a power of 2
				return roundSize(size);
			}
		}while(incorrectInput);
		return (Integer) null;
	}
	//create and new momory block of int size
	private static ArrayList<Memory> createMemoryBlock(int systemSize)
	{
		Memory x = new Memory(systemSize);
		ArrayList<Memory> memoryBlock = new ArrayList<Memory>();
		memoryBlock.add(x);
		return memoryBlock;
	}

	private static Memory createProcess(ArrayList<Memory> currentBank)
	{
		
		
		do{
			incorrectInput=false;
			int size = 0;
			
			try{
				
				
			System.out.println("Please input the size of the process (in Mb): ");
			size = scanInt.nextInt();
			
			
			}catch(RuntimeException ex){
				System.out.println("Invalid input, Please input a integer");
				incorrectInput= true;
				scanInt.next();
				return createProcess(currentBank);
					
				
			}
			if(size < 1 )
			{
				System.out.println("ERROR: Please input size greater than 0");
				incorrectInput=true;
				return createProcess(currentBank);
			}
			else if(size>returnMaxPossibleInput(currentBank))
			{
				incorrectInput=true;
				System.out.println("ERROR: Process too demanding for allocated space"+"\n"+"Please deallocate a running process or begin a smaller one"+"\n"+"The max process space for allocation is currently: "+returnMaxPossibleInput(currentBank));
				return createProcess(currentBank);
			}
			
			
			else
			{
				Memory newProcess = new Memory(size);
				System.out.println("Please input a string to identify this process: ");
				String name = scanInt.next();
				newProcess.setID(name);
				newProcess.setActive(true);
				return newProcess;
			}
			//return null;
		}while(incorrectInput);
	}
	//create a new process with size and the process name Id
	private static Memory createProcess(int size, String name)
	{
		Memory newProcess = new Memory(size);
		newProcess.setID(name);
		newProcess.setActive(true);
		return newProcess;
	}

	private static ArrayList<Memory> addProcess(ArrayList<Memory> currentState, Memory newProcess)
	{
		for(int i = 0; i<currentState.size(); i++)
		{
			int roundedProcessValue = roundSize(newProcess.getValue());
			if(!currentState.get(i).getActive())
			{
				if(currentState.get(i).getValue()==roundedProcessValue)
				{
					currentState.get(i).setValue(newProcess.getValue());
					currentState.get(i).setActive(true);
					currentState.get(i).setID(newProcess.getName());
					break;
				}
				else if(currentState.get(i).getValue()>roundedProcessValue)
				{
					currentState = split(currentState, i);
					i=-1;
				}
			}
		}
		return currentState;
	}
	//split a buddy
	private static ArrayList<Memory> split(ArrayList<Memory> editableList, int locationOfEdit)
	{
		if(editableList.get(locationOfEdit).getValue()>1)
		{
			int tempNum = editableList.get(locationOfEdit).getValue()/2;
			editableList.get(locationOfEdit).setValue(tempNum);
			Memory splitMem = new Memory(tempNum);
			editableList.add(locationOfEdit, splitMem);
			editableList.get(locationOfEdit).setHasBuddy(true);
			editableList.get(locationOfEdit+1).setHasBuddy(true);
			editableList.get(locationOfEdit+1).setBuddy(editableList.get(locationOfEdit));
			editableList.get(locationOfEdit).setBuddy(editableList.get(locationOfEdit+1));

			return editableList;
		}
		return null;
	}
	//deallocate method arrayList and using process name ID
	private static ArrayList<Memory> deallocate(ArrayList<Memory> editableList, String name)
	{
		Memory finishedMem = findProcess(editableList, name);
		finishedMem.setActive(false);
		finishedMem.setID("");
		return merge(editableList,finishedMem);
	}
	//using helper methods to find a buddy merge buddies to before split state
	private static ArrayList<Memory> merge(ArrayList<Memory> editableList,Memory emptyMem)
	{
		boolean flag = false;
		while(!flag)
		{
			flag=true;
			if(!(emptyMem.getActive())&& (emptyMem.getHasBuddy()))
			{
				if(!(emptyMem.getBuddy().getActive()))
				{
					editableList.remove(emptyMem.getBuddy());
					emptyMem.setValue(roundSize(emptyMem.getValue())*BINARY_MODIFIER);
					//Memory newMem = editableList.get(editableList.indexOf(emptyMem));
					for(int i=1;i<editableList.size();i++)
					{
						if(!editableList.get(i).getActive()&&!editableList.get(i-1).getActive()&&editableList.get(i).getValue()==editableList.get(i-1).getValue())
						{
							flag=false;
							emptyMem=editableList.get(i);
							emptyMem.setBuddy(editableList.get(i-1));
						}
					}
				}
			}
		}

		return editableList;
	}
	
	//method to round the size of user block size input to a power of 2
	private static int roundSize (int inputBlock)
	{
		int power = 2;        
		while (power < inputBlock) 
		{
			power = power * 2;
		}
		int blockAsPowerTwo = power;
		return blockAsPowerTwo;
	}
	//find a process in memory via process name
	private static Memory findProcess(ArrayList<Memory> editableList,String name)
	{


		for(int i = 0;i<editableList.size()-1;i++)
		{
			if(editableList.get(i).getName().equals(name))
				return editableList.get(i);
		}
		System.out.println("Process was not found in memory!");
		return new Memory(0);


	}
	
	//assign a buddy to its correct partner for later  merging / splitting
	private static void assignBuddy(Memory first, Memory second)
	{
		first.setHasBuddy(true);
		second.setHasBuddy(true);
		second.setBuddy(first);
		first.setBuddy(second);
	}

	//read file from user
	private static void runFileInput() throws IOException
	{
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt Files Only", "txt");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) 
		{
			try 
			{
		BufferedReader br = new BufferedReader(new FileReader(chooser.getSelectedFile()));;
		// need to change file location
		//BufferedReader br = new BufferedReader(reader);
		String line;
		if((line = br.readLine()) != null)
		{
			int memoryBlockSize = roundSize(Integer.parseInt(line));
			ArrayList<Memory> currentBank = createMemoryBlock(memoryBlockSize);
			while ((line = br.readLine()) != null) 
			{
				int input = Integer.parseInt(line);

				if(input==1)
				{
					for(int i = 0; i<currentBank.size();i++)
					{
						System.out.println("Location: "+i+"\t"+"Value: "+currentBank.get(i).getValue()+"\t"+"Active Process? "+currentBank.get(i).getActive()+"\t"+"ID Name: "+currentBank.get(i).getName());	
					}						
				}
				else if(input==2)
				{
					int i = Integer.parseInt(br.readLine());
					String s = br.readLine();
					addProcess(currentBank,createProcess(i, s));
					System.out.println("\n");
				}
				else if(input==3)
				{
					String s = br.readLine();
					currentBank = deallocate(currentBank, s);
					System.out.println("\n");
				}
			}
		}
		br.close();
			}
			
		catch (IOException e) 
		{
			System.out.println("File Read Error");
		}
		System.out.println("You chose to open this file: " +
				chooser.getSelectedFile().getName());
	}
		
	}
	//method that returngs max possible inputs 
	private static int returnMaxPossibleInput(ArrayList<Memory> currentBlock)
	{
		int maxAllowedInput = 0;
		for(int i=0;i<currentBlock.size();i++)
		{
			if(!currentBlock.get(i).getActive()&&currentBlock.get(i).getValue()>maxAllowedInput)
			{
				maxAllowedInput=currentBlock.get(i).getValue();
			}
		}
		return maxAllowedInput;
	}
}