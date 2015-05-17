package main.setting;

public class SettingStorage {

	public int TPS; //���-�� ���������� update � �������
	public int SKIP_TICKS;//������� � ������������ ����� ������������
	
	public boolean DEBUG_CONSOLE;//�������� � ������� ��������� �������?
	public boolean DEBUG_CONSOLE_IMAGE;//�������� � ������� ��������� �������� � ��������?
	public boolean DEBUG_CONSOLE_MASK;//�������� � ������� ��������� �������� �����?
	public boolean DEBUG_CONSOLE_FPS;//�������� � ������� ���?
	public boolean DEBUG_MONITOR_FPS;//�������� � ���� ���?
	public boolean MASK_DRAW;//��������� �����
	
	public int WIDTH_SCREEN;
	public int HEIGHT_SCREEN;//������ ����
	public String WINDOW_NAME;
	
	public int SEND_STEP_MAX;//���������� ������ � ������ ������ n updat'��
	
	public String fileName = "main.properties";
	
	public void initFromFile(){
		ConfigReader cr = new ConfigReader(fileName);
		
		TPS = cr.findInteger("TPS");
		WIDTH_SCREEN = cr.findInteger("WIDTH_SCREEN");
		HEIGHT_SCREEN = cr.findInteger("HEIGHT_SCREEN");
		SEND_STEP_MAX = cr.findInteger("SEND_STEP_MAX");
		
		WINDOW_NAME = cr.findString("WINDOW_NAME");
		
		DEBUG_CONSOLE = cr.findBoolean("DEBUG_CONSOLE");
		DEBUG_CONSOLE_IMAGE = cr.findBoolean("DEBUG_CONSOLE_IMAGE");
		DEBUG_CONSOLE_MASK = cr.findBoolean("DEBUG_CONSOLE_MASK");
		DEBUG_CONSOLE_FPS = cr.findBoolean("DEBUG_CONSOLE_FPS");
		DEBUG_MONITOR_FPS = cr.findBoolean("DEBUG_MONITOR_FPS");
		MASK_DRAW = cr.findBoolean("MASK_DRAW");
		
		SKIP_TICKS = 1000/TPS;
	}
}
