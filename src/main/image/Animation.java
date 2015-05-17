package main.image;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

import javax.imageio.*;

import main.Global;

import java.io.*;
import java.net.URL;

public class Animation implements Cloneable, Rendering{
    private Image[] image;
    private int frameNumber=0; //���-�� ������ [1;inf)
    private int frameSpeed; //����� ������� ��������� update ������ ����
    private int frameNow; //����� �������� ����� [0;inf)
    private int update; //������� ������ ��������� update � ��������� ����� �����
    public String path;//���� � �����, ���� ��� �������� ����� � �������
    public Mask mask;
    
    public Animation(String path, int frameSpeed, int frameNumber) {
		this.path = path; //��� �������� ����� � �������
		this.frameSpeed = frameSpeed;
		this.frameNumber = frameNumber;
		this.image = new Image[frameNumber];
		String urlStr;
		URL url;
		//�������� �����������
		for(int i=0;i<frameNumber;i++){
			BufferedImage sourceImage = null;
			urlStr = path + "/" + (i+1) + ".png";
			try {
				url = this.getClass().getClassLoader().getResource(urlStr);
				sourceImage = ImageIO.read(url);
			} catch (IOException e) {
				e.printStackTrace();
			}
			image[i] = Toolkit.getDefaultToolkit().createImage(sourceImage.getSource());
		}
        
        this.update = 0;
        
        if (Global.setting.DEBUG_CONSOLE_IMAGE) System.out.println("Load animation \"" + path + "\" complited.");
        this.mask = new Mask(path + "/1.", getWidth(0), getHeight(0));
    }
    
    public int getFrameNow() {
        return frameNow;
    }
    
    public int getFrameSpeed() {
        return frameSpeed;
    }
    
    public void setFrameNow(int frameNow) {
        this.frameNow = frameNow;
    }
    
    public void setFrameSpeed(int frameSpeed) {
		this.update = 0;
        this.frameSpeed = frameSpeed;
    }
    
    public int getFrameNumber() {
        return frameNumber;
    }
    
    public int getWidth(int frame) {
        return image[frame].getWidth(null);
    }

    public int getHeight(int frame) {
        return image[frame].getHeight(null);
    }
    
    public Mask getMask(){
		return this.mask;
	}
	
	public Animation clone() throws CloneNotSupportedException {
		return (Animation)super.clone();
	}
    
    public void update() {
		this.update++;
		if (this.update == this.frameSpeed) {
			this.update = 0;
			if (this.frameNow == this.frameNumber - 1) {
				this.frameNow = 0;
			} else {
				this.frameNow++;
			}
		}
	}
    
    public void draw(Graphics2D g,int x,int y,double direction) {
		direction-=Math.PI/2; //������� ���������� ���� � ������� �� �����
		AffineTransform at = new AffineTransform(); 
		at.rotate(-direction,x+getWidth(frameNow)/2,y+getHeight(frameNow)/2); //�������� ���������� � ���������
        g.setTransform(at); //��� �������� ������� �� direction
        g.drawImage(image[frameNow], x, y, null);//��� ��������� ������� ����� ������� ����� ����
    }

	public int getWidth() {
		return getWidth(getFrameNow());
	}

	public int getHeight() {
		return getHeight(getFrameNow());
	}

	public Image getImage() {
		return image[frameNow];
	}
}