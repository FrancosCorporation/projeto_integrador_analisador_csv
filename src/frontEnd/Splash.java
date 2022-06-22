package frontEnd;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.TitledBorder;

public class Splash {
    JFrame frame;
    JLabel image=new JLabel(new ImageIcon("hiccups.png"));
    JProgressBar progressBar=new JProgressBar();
    JLabel message=new JLabel();
    private final JLabel lblAnalizadorCsv = new JLabel("Analizador CSV");
    Splash()
    {
        createGUI();
        addImage();
        addText();
        addProgressBar();
        addMessage();
        runningPBar();
    }
    public void createGUI(){
        frame=new JFrame();
        frame.getContentPane().setLayout(null);
        frame.setUndecorated(true);
        frame.setSize(350,500);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setVisible(true);

    }
    public void addImage(){
        image.setLocation(12, 29);
        image.setSize(326,433);
        frame.getContentPane().add(image);
    }
    public void addText()
    {
    }
    public void addMessage()
    {
        message.setBounds(12,397,319,24);
        message.setForeground(Color.black);
        message.setFont(new Font("UBUNTU",Font.PLAIN,15));
        frame.getContentPane().add(message);
        {
        	lblAnalizadorCsv.setForeground(Color.WHITE);
        	lblAnalizadorCsv.setBounds(118, 12, 113, 10);
        	frame.getContentPane().add(lblAnalizadorCsv);
        }
    }
    public void addProgressBar(){
        progressBar.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        progressBar.setBounds(12,464,326,24);
        progressBar.setBorderPainted(false);
        progressBar.setStringPainted(true);
        progressBar.setBackground(Color.black);
        progressBar.setForeground(Color.darkGray);
        progressBar.setValue(0);
        frame.getContentPane().add(progressBar);
    }
    public void runningPBar(){
        int i=0;
        while( i<=100)
        {
            try{
                Thread.sleep(50);
                progressBar.setValue(i);
                message.setText("LOADING "+Integer.toString(i)+"%");
                i++;
                if(i==100)
                    frame.dispose();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
