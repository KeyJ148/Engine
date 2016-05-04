package tow.image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import tow.Global;

public class SpriteAWT{
	
    private Image image;
    private Image imageDefault;
    public String path;//���� � ��������
    
    public SpriteAWT(String path) {
		this.path = path; 
		
		//�������� ��������
		BufferedImage sourceImage = null;
        try {
			sourceImage = ImageIO.read(new File(path));
			if (Global.setting.DEBUG_CONSOLE_IMAGE) Global.p("Load image \"" + path + "\" complited.");
        } catch (IOException e1) {
			Global.error("Image \"" + path + "\" not loading");
		} catch (UnsupportedOperationException e2){
			Global.error("Image \"" + path + "\" not loading");
		}
       
		this.image = Toolkit.getDefaultToolkit().createImage(sourceImage.getSource());
		this.imageDefault = image;
    }
    
    public int getWidth() {
        return image.getWidth(null);
    }

    public int getHeight() {
        return image.getHeight(null);
    }
    
    public Image getImage(){
		return image;
	}
    
    public String getPath(){
		return path;
	}
    
    public void draw(Graphics2D g,int x,int y,double direction) {
    	direction-=Math.PI/2; 
		AffineTransform at = new AffineTransform(); 
		at.rotate(-direction,x+getWidth()/2,y+getHeight()/2); 
        g.setTransform(at); 
        g.drawImage(image, x, y, null);
    }
    
    public void draw(Graphics g,int x,int y) {
        g.drawImage(image, x, y, null);
    }
    
    public void setColor(Color c){
    	BufferedImage bi = toBufferedImage();
    	bi = setColorBufferedImage(bi, c);
    	this.image = Toolkit.getDefaultToolkit().createImage(bi.getSource());
    }
    
    private BufferedImage setColorBufferedImage(BufferedImage image, Color setColor) {
        int width = getWidth();
        int height = getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
            	
                Color originalColor = new Color(image.getRGB(x, y), true);
                int red = originalColor.getRed();
                int green = originalColor.getGreen();
                int blue = originalColor.getBlue();
                int alpha = originalColor.getAlpha();
                
                int newRed = setColor.getRed() * red/255;
                int newGreen = setColor.getGreen() * green/255;
                int newBlue = setColor.getBlue() * blue/255;
                Color newColor = new Color(newRed, newGreen, newBlue, alpha);
                image.setRGB(x, y, newColor.getRGB());
                
            }
        }
        return image;
    }
    
    private BufferedImage toBufferedImage(){
    	
        if (this.image instanceof BufferedImage)
        {
            return (BufferedImage) this.image;
        }

        BufferedImage bimage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(this.image, 0, 0, null);
        bGr.dispose();

        return bimage;
    }
    
    public void setDefaultImage(){
    	this.image = imageDefault;
    }
    
    public boolean isAnim(){
    	return false;
    }
}
