package main;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import main.net.LinkCS;
import main.net.ServerNetThread;

public class GameServer {
	
	public final String PATH_MAP = "map";
	public final String PATH_IMAGE = "image";  
	
	public DataOutputStream[] out;
	public DataInputStream[] in;
	public ServerNetThread[] serverThread;
	public InetAddress[] inetAdr;
	public Vector mess;
	public Vector idExc;
	public int peopleMax;
	public int peopleNow;
	public int port;
	public int[] tankX;
	public int[] tankY;
	public boolean genTank = false;//��������� �� ��������� ������?
	public String pathFull; //���� � �����
	public int widthMap;//������� �����
	public int heightMap;
	public boolean[] connect;//����������� �������
	public int disconnect;
	
	public GameServer() throws IOException{
		BufferedReader bReader = new BufferedReader (new InputStreamReader(System.in));
		int port;
		int peopleMax;
		String str;
		
		Global.linkCS = new LinkCS();
		Global.linkCS.initSprite();
		
		System.out.println("Port (Default 25566): ");
		str = bReader.readLine();
		if (str.equals("")){
			port = 25566;
		} else {
			port = Integer.parseInt(str);
		}
		this.port = port;
		
		System.out.println("Max people (Default 1): ");
		str = bReader.readLine();
		if (str.equals("")){
			peopleMax = 1;
		} else {
			peopleMax = Integer.parseInt(str);
		}
		this.peopleMax = peopleMax;
		
		this.out = new DataOutputStream[peopleMax];
		this.in = new DataInputStream[peopleMax];
		this.serverThread = new ServerNetThread[peopleMax];
		this.inetAdr = new InetAddress[peopleMax];
		this.tankX = new int[peopleMax];
		this.tankY = new int[peopleMax];
		this.connect = new boolean[peopleMax];
		this.disconnect = 0;
		this.mess = new Vector();
		this.idExc = new Vector();
		for(int i=0;i<peopleMax;i++){
			connect[i] = false;
		}
		ServerSocket ServerSocket = new ServerSocket(port);
		this.peopleNow = 0;
		
		genTank();
		System.out.println("Server started.");
		
		while(peopleNow != peopleMax){
			Socket sock = ServerSocket.accept();
			inetAdr[peopleNow] = sock.getInetAddress();
			out[peopleNow] = new DataOutputStream(sock.getOutputStream());
			in[peopleNow] = new DataInputStream(sock.getInputStream());
			System.out.println("New client.");
			serverThread[peopleNow] = new ServerNetThread(this, peopleNow, peopleMax);
			this.peopleNow++;	
		}
		
		System.out.println("All users connected.");
		
		boolean mapDownAll;//��� �� ������� �����
		do{
			mapDownAll = true;
			for(int i=0;i<peopleMax;i++){
				if (connect[i] == false){
					mapDownAll = false;
				}
			}
		}while(!mapDownAll);
		
		System.out.println("All users map download.");
		
		for(int i=0;i<peopleMax;i++){
			serverThread[i].mapDownAll = true;
		}
		
		System.out.println("All users game start.");
		
		conMessSend();	
	}
	
	public void genTank(){
		//������� ����� � ������
		String pathFull;
		File[] fList = new File(PATH_MAP).listFiles();
		int fListNum;
		Vector<Integer> vecX = new Vector<Integer>();
		Vector<Integer> vecY = new Vector<Integer>();
		Vector<String> vecSprite = new Vector<String>();
		while (true) {
			fListNum = (int) Math.round(Math.random()*(fList.length-1));
			pathFull = PATH_MAP + "/" + fList[fListNum].getName().substring(0,fList[fListNum].getName().lastIndexOf('.')) + ".map";
			if (new File(pathFull).exists()){
				System.out.println("Map: " + pathFull);
				try {
					BufferedReader fileReader = new BufferedReader(new FileReader(pathFull));
					String s;
					s = fileReader.readLine();
					this.widthMap = Integer.parseInt(Global.linkCS.parsString(s,1));
					this.heightMap = Integer.parseInt(Global.linkCS.parsString(s,2));

					while (true){ 
						s = fileReader.readLine();
						if (s == null){
							break;
						}
						
						vecX.add(Integer.parseInt(Global.linkCS.parsString(s,1)));
						vecY.add(Integer.parseInt(Global.linkCS.parsString(s,2)));
						vecSprite.add(Global.linkCS.parsString(s,4));
						
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			} 
		}
		this.pathFull = pathFull;
		
		//��������� ������
		BufferedReader bReader = new BufferedReader (new InputStreamReader(System.in));//
		int wTank = Global.c_default.getWidth(0);
		int hTank = Global.c_default.getHeight(0);
		double disTank = Math.sqrt(wTank*wTank + hTank*hTank)/2;
		boolean gen;
		int xRand,yRand,x,y,w,h;
		double disHome,disPointToHome,dxRand,dyRand;
		String sprite;
		for(int j=0;j<this.peopleMax;j++){
			do{
				gen = false;
				dxRand = Math.random()*(widthMap-200)+100;//���� �������� ����� � ��� - ������
				dyRand = Math.random()*(heightMap-200)+100;//������� �����
				xRand = (int) dxRand;
				yRand = (int) dyRand;
				for(int i=0;i<vecX.size();i++){
					x = (int) vecX.get(i);//���� �������
					y = (int) vecY.get(i);
					w = (int) Global.linkCS.getSprite((String) vecSprite.get(i)).getWidth();//������� �������
					h = (int) Global.linkCS.getSprite((String) vecSprite.get(i)).getHeight();
					disHome = Math.sqrt(w*w + h*h)/2;
					disPointToHome = Math.sqrt((x-xRand)*(x-xRand)+(y-yRand)*(y-yRand));
					if ((disHome+disTank+30) > (disPointToHome)){
						gen = true;
					}
				}
			} while(gen);
			this.tankX[j] = xRand;
			this.tankY[j] = yRand;
			vecX.add(xRand);
			vecY.add(yRand);
			vecSprite.add("player_color");
		}
	}
	
	public void conMessSend(){
		String str;
		int id;
		try{
			while (true){
				if ((this.mess.size() != 0) && (this.idExc.size() != 0)){
					str = (String) this.mess.remove(0);
					id = (int) this.idExc.remove(0);
					for(int i=0;i<peopleMax;i++){
						if (i != id){
							out[i].writeUTF(str);
						}
					}
				}
			}
		} catch (IOException e){
			System.out.println("Error send message!");
		}
	}
	
	public void giveMess(String s, int id){
		this.mess.add(s);
		this.idExc.add(id);
	}
	
	public static void main (String args[]) throws IOException{
		new GameServer();
	}
}

