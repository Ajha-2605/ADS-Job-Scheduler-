import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Vector;


public class jobscheduler {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {

		try {
			String strInputFile = "";
			if(args==null || args.length <= 0) {
				return;
			}
			else {
				strInputFile = args[0];
			}

			DataInputStream dis = new DataInputStream(new FileInputStream(strInputFile));
			DataOutputStream dos = new DataOutputStream(new FileOutputStream("output_file.txt"));
			String strLine = null;

			int nTimeSeek = 0;
			int nEventTime = -1;
			Vector<Job> jobList = new Vector<Job>();
			int nPlusTime = 0;
			boolean bExecutedLine = true;
			do {
				if(nTimeSeek < nEventTime) {
					nPlusTime = 5;
					nTimeSeek+=nPlusTime;
				}
				else {
					if(bExecutedLine) {
						strLine  = dis.readLine();
						if(strLine==null) {
							 break;
						}
						strLine = strLine.trim();
					}
					if(strLine!=null && strLine.length() > 0) {
						int index = strLine.indexOf(":");
						if(index==-1) {
							continue;
						}
						
						String strTime = strLine.substring(0, index);
						nEventTime = Integer.parseInt(strTime);
						String nextToken = strLine.substring(index + 1).trim();
						
						if(nextToken.length() < 0) {
							continue;
						}
						if(nTimeSeek < nEventTime) {
							bExecutedLine = false;
							continue;
						}
						int index_startBracket = nextToken.indexOf("(");
						int index_endBracket = nextToken.indexOf(")");
						
						if(index_endBracket > index_startBracket) {
							
							String command = nextToken.substring(0, index_startBracket);
							String strParameter = nextToken.substring(index_startBracket + 1, index_endBracket);
							String[] strParam = strParameter.split(",");
							
							switch(command) {
							case "Insert":
								if(strParam.length >= 2) {
									Job jobData = new Job(nTimeSeek);
									jobData.setJobID(Integer.parseInt(strParam[0]));
									jobData.setTotalTime(Integer.parseInt(strParam[1]));
									jobList.add(jobData);
									bExecutedLine = true;
								}
								continue;
							case "PrintJob":
								if(strParam.length >= 2) {
									int nCurrJob1 = Integer.parseInt(strParam[0]);
									int nCurrJob2 = Integer.parseInt(strParam[1]);
									
									RedBlackTree rbTree = new RedBlackTree();
									int a[] = new int[jobList.size()];
									for(int i = 0; i < jobList.size(); i++) {
										a[i] = jobList.get(i).jobID;
									}
									Arrays.sort(a);
									for(int v: a)
										rbTree.insert(v);
									
									Integer nNextJob1 = rbTree.next(nCurrJob1);
									Integer nNextJob2 = rbTree.next(nCurrJob2);
									
									String strWrite = null;
									if(nNextJob1==null && nNextJob2==null) {
										if(jobList.size() > 0) {
											strWrite = "";
											for(int i = 0; i < jobList.size(); i++) {
												if(i > 0) {
													strWrite += ",";
												}
												strWrite += String.format("(%d,%d,%d)", jobList.get(i).jobID, jobList.get(i).executedTime, jobList.get(i).totalTime);
												if(i == jobList.size() - 1) {
													strWrite+="\n";
												}
											}
										}
										
									}
									else {
										strWrite = "";
										if(nNextJob1!=null) {
											for(int i = 0; i < jobList.size(); i++) {
												if(jobList.get(i).jobID==nNextJob1) {
													strWrite += String.format("(%d,%d,%d)", jobList.get(i).jobID, jobList.get(i).executedTime, jobList.get(i).totalTime);
													break;
												}
											}
										}
										if(nNextJob2!=null) {
											for(int i = 0; i < jobList.size(); i++) {
												if(jobList.get(i).jobID==nNextJob2) {
													strWrite += String.format("(%d,%d,%d)", jobList.get(i).jobID, jobList.get(i).executedTime, jobList.get(i).totalTime);
													break;
												}
											}
										}
										strWrite+="\n";
									}
									if(strWrite==null) {
										strWrite = "(0,0,0)\n";
									}
									System.out.println(strWrite);
									dos.writeBytes(strWrite);
									bExecutedLine = true;
								}
								else if(strParam.length >= 1) {
									int nPrintJob = Integer.parseInt(strParam[0]);
									
									String strWrite = null;
									for(int i = 0; i < jobList.size(); i++) {
										if(jobList.get(i).jobID==nPrintJob) {
											strWrite = String.format("(%d,%d,%d)\n", jobList.get(i).jobID, jobList.get(i).executedTime, jobList.get(i).totalTime);
											break;
										}
									}
									if(strWrite==null) {
										strWrite = "(0,0,0)\n";
									}
									System.out.println(strWrite);
									dos.writeBytes(strWrite);
									
									bExecutedLine = true;
								}
								continue;
							case "NextJob":
								
								if(strParam.length >= 1) {
									int nCurrJob = Integer.parseInt(strParam[0]);
									
									RedBlackTree rbTree = new RedBlackTree();
									int a[] = new int[jobList.size()];
									for(int i = 0; i < jobList.size(); i++) {
										a[i] = jobList.get(i).jobID;
									}
									Arrays.sort(a);
									for(int v: a)
										rbTree.insert(v);
									
									Integer nNextJob = rbTree.next(nCurrJob);
									String strWrite = null;
									if(nNextJob!=null) {
										for(int i = 0; i < jobList.size(); i++) {
											if(jobList.get(i).jobID==nNextJob) {
												strWrite = String.format("(%d,%d,%d)\n", jobList.get(i).jobID, jobList.get(i).executedTime, jobList.get(i).totalTime);
												break;
											}
										}
									}
									if(strWrite==null) {
										strWrite = "(0,0,0)\n";
									}
									System.out.println(strWrite);
									dos.writeBytes(strWrite);
									bExecutedLine = true;
								}
								continue;
							case "PreviousJob":
								if(strParam.length >= 1) {
									int nCurrJob = Integer.parseInt(strParam[0]);
									
									RedBlackTree rbTree = new RedBlackTree();
									int a[] = new int[jobList.size()];
									for(int i = 0; i < jobList.size(); i++) {
										a[i] = jobList.get(i).jobID;
									}
									Arrays.sort(a);
									for(int v: a)
										rbTree.insert(v);
									
									Integer nPrevJob = rbTree.prev(nCurrJob);
									String strWrite = null;
									if(nPrevJob!=null) {
										for(int i = 0; i < jobList.size(); i++) {
											if(jobList.get(i).jobID==nPrevJob) {
												strWrite = String.format("(%d,%d,%d)\n", jobList.get(i).jobID, jobList.get(i).executedTime, jobList.get(i).totalTime);
												break;
											}
										}
									}
									if(strWrite==null) {
										strWrite = "(0,0,0)\n";
									}
									System.out.println(strWrite);
									dos.writeBytes(strWrite);
									bExecutedLine = true;
								}
								
								continue;
							}
						}
						else {
							continue;
						}
					}
				}
				//nPlusTime
				minHeap minHeap = new minHeap(jobList.size() + 3);
				RedBlackTree rbTree = new RedBlackTree();
				
				int a[] = new int[jobList.size()];
				for(int i = 0; i < jobList.size(); i++) {
					minHeap.insert(jobList.get(i).executedTime);
					a[i] = jobList.get(i).jobID;
				}
				
				Arrays.sort(a);
				for(int v: a)
					rbTree.insert(v);
				
				minHeap.minHeap();
				int nMinExecuteTime = -1;
				if(jobList.size()==1) {
					nMinExecuteTime = jobList.get(0).getExecutedTime();
				}
				else {
					nMinExecuteTime = minHeap.remove();
				}
		        for(int i = 0; i < jobList.size(); i++) {
		        	if(jobList.get(i).executedTime==nMinExecuteTime) {
		        		jobList.get(i).executedTime += nPlusTime;
		        		if(jobList.get(i).executedTime>=jobList.get(i).totalTime) {
		        			jobList.remove(i);
		        		}
		        		break;
		        	}
		        }
			}
			while(strLine!=null);
			
			dos.close();
			dis.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}

class Job{
	public Job(int nStartTimeSeek) {
		this.startSeekTime = nStartTimeSeek;
	}
	int jobID;
	int totalTime;
	int executedTime;
	int startSeekTime;
	
	public int getJobID() {
		return jobID;
	}
	public void setJobID(int jobID) {
		this.jobID = jobID;
	}
	public int getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}
	public int getExecutedTime() {
		return executedTime;
	}
	public void setExecutedTime(int executedTime) {
		this.executedTime = executedTime;
	}
}