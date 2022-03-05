import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.*;

public class Pomodoro {
	
	static class OnePomodoro{
		private int onePom = 1500000;
		
		public int getOnePom() {
			return onePom;
		}
		
		public void decreaseOnePomByOneSec() {
			this.onePom -= 1000;
		}
	}
	
	static OnePomodoro onePomodoro;
	static boolean isStarted=false;
	static Timer timer;
	static DateTimeFormatter dtf=DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	static LocalDateTime now;
	

	public static void main(String[] args) {
		
		//timer
		//start,stop,cancel
		//text field
		//archive
		
		JFrame f= new JFrame("Pomodoro Application");
		
		JPanel panelOfTimer= new JPanel();
		panelOfTimer.setBounds(128, 70, 120, 60);
		panelOfTimer.setBackground(Color.ORANGE);
		
		JLabel labelOfTimer= new JLabel("25.00");
		labelOfTimer.setFont(new Font("Serif", Font.BOLD,36));
		labelOfTimer.setForeground(Color.WHITE);
		panelOfTimer.add(labelOfTimer);
		
		JLabel titleOfNameOfPomodoro = new JLabel("Name of Pomodoro");
		titleOfNameOfPomodoro.setBounds(135, 120, 280, 40);
		titleOfNameOfPomodoro.setForeground(Color.CYAN);
		panelOfTimer.add(titleOfNameOfPomodoro);
		
		JTextField nameOfPomodoro = new JTextField();
		nameOfPomodoro.setBounds(55, 155, 273, 30);
		nameOfPomodoro.setFont(new Font("Helvetica", Font.BOLD,12));
		nameOfPomodoro.setForeground(Color.GRAY);
		panelOfTimer.add(nameOfPomodoro);
		
		JLabel warningToUser= new JLabel(" After entering the name of the Pomodoro, please press enter.");
		warningToUser.setBounds(15,270,350,100);
		
		JLabel warningToUser2= new JLabel("            Your work will be saved when the time is up.");
		warningToUser2.setBounds(15,286,350,100);
		
		JButton button= new JButton("Start");
		button.setBounds(140, 190, 100, 20);
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				warningToUser.setText("When the time is up for "+nameOfPomodoro.getText());
				warningToUser2.setText("Your work will be saved.");
				if(!isStarted) {
					isStarted=true;
					if(onePomodoro==null) {
						onePomodoro=new OnePomodoro();
					}
					
					File fileOfProgress = new File("archive.txt");
					try {
						if(fileOfProgress.createNewFile()) {
							System.out.println("Your file has been saved: "+fileOfProgress.getName());
						}
						else {
							System.out.println("The file already exist.");
						}
						
					} catch (Exception e) {
						System.out.println("Error.404");
						e.printStackTrace();
					}
					timer=new Timer(1000, new ActionListener() {
						
						String timerText= " ";
						@Override
						public void actionPerformed(ActionEvent e) {
							
							onePomodoro.decreaseOnePomByOneSec();
							if(onePomodoro.getOnePom() <= 0) {
								
								System.out.println("Pomodoro is completed.");
								
								now=LocalDateTime.now();
								
								FileWriter writer=null;
								
								try {
									writer=new FileWriter(fileOfProgress, true);
								}catch (Exception e2) {
									e2.printStackTrace();
								}
								
								try {
									writer.append(nameOfPomodoro.getText()+"-> "+dtf.format(now)+ " dated pomodoro is completed. \n");
									warningToUser.setText("Your work is saved.");
									warningToUser2.setText("");
									writer.close();
								}catch (Exception e2) {
									e2.printStackTrace();
								}
					
								//Pomodoro stoped so timer should stop and isStarted code turn false again.
								timer.stop();
								isStarted=false;
								onePomodoro= new OnePomodoro();
							}
							
							int minute= (onePomodoro.getOnePom()/1000) / 60;
							int second= (onePomodoro.getOnePom()/1000) % 60;
							
							if(minute<10 && second<10) {
								timerText= 0 +minute+ ":" +0+ second; 
							}
							else if(minute<10 && !(second<10)) {
								timerText= 0 +minute+ ":" +second;
							}
							else if(second<10) {
								timerText= minute+ ":" +0+ second; 
							}
							else
								timerText= minute+ ":" +second;
							labelOfTimer.setText(timerText);
						}
					});
					timer.start();
				}
			}
		});
		
		JButton button2= new JButton("Stop");
		button2.setBounds(140, 215, 100, 20);
		button2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(onePomodoro==null) {
					return;
				}
				timer.stop();
				isStarted=false;
				
				if(onePomodoro.getOnePom()!=(new OnePomodoro().getOnePom())) {
					warningToUser.setText("Your work is stopped.");
					warningToUser2.setText("When you are working with Pomodoro, you should not be stop.");
				}
				else {
					warningToUser.setText("You can not stop wihtout start.");
					warningToUser2.setText("");
				}
			}
		});
		
		JButton button3=new JButton("Cancel");
		button3.setBounds(140, 240, 100, 20);
        button3.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(onePomodoro==null) {
					return;
				}
				timer.stop();
				isStarted=false;
				onePomodoro=new OnePomodoro();
				
				String timerText= " ";
				
				int minute= (onePomodoro.getOnePom()/1000) / 60;
				int second= (onePomodoro.getOnePom()/1000) % 60;
				
				if(minute<10 && second<10) {
					timerText= 0 +minute+ ":" +0+ second; 
				}
				else if(minute<10 && !(second<10)) {
					timerText= 0 +minute+ ":" +second;
				}
				else if(second<10) {
					timerText= minute+ ":" +0+ second; 
				}
				else
					timerText= minute+ ":" +second;
				labelOfTimer.setText(timerText);
				
				warningToUser.setText("You cancelled your work.");
				warningToUser2.setText("");
				
			}
		});
        
        JButton progress= new JButton("Archive");
        progress.setBounds(140, 265, 100, 30);
        progress.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					Desktop.getDesktop().open(new java.io.File("archive.txt"));
				} catch (IOException e1) {
					warningToUser.setText("It is not openning because the file has not been created.");
					warningToUser2.setText("");
					e1.printStackTrace();
				}
				
			}

        });
        
        f.add(progress);
        f.add(nameOfPomodoro);
        f.add(titleOfNameOfPomodoro);
        f.add(warningToUser2);
        f.add(warningToUser);
        f.add(button3);
		f.add(button2);
		f.add(panelOfTimer);
		f.add(button);
		f.setLayout(null);
		f.setSize(400,400);
		f.setVisible(true);
		
	}

}
