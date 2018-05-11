import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.*;

//serialization strategy adopted from: https://www.tutorialspoint.com/java/java_serialization.htm
//Main class to test the application
public class SimpleCalendar {
    public static void main(String[] args){

        JFrame jFrame = new JFrame();                                               //create frame to run app
        BorderLayout borderLayout = new BorderLayout();
        jFrame.setBackground(Color.WHITE);
        jFrame.setLayout(borderLayout);

        //Get the model; make a new one and initialize if one is found through the serialization.
        Model model = new Model();
        try {
            FileInputStream file = new FileInputStream("events.txt");     //read from events.txt
            ObjectInputStream in = new ObjectInputStream(file);
            model = (Model) in.readObject();
            in.close();                                 //close streams
            file.close();
        } catch (FileNotFoundException f) {                       //File doesn't exist --> ok.
            f.printStackTrace();
        }catch (IOException i) {                       //some weird thing happened
            i.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            System.out.println("Employee class not found");
        }

        //VIEWS
        //create the DayView
        DayView dayView = new DayView(model.getCalendar());
        //VIEWS WITH CONTROLLER LOGIC
        //create the MonthViewController
        MonthViewController monthViewController = new MonthViewController(model);
        //create the CreateEventView
        CreateEventViewController createEventViewController = new CreateEventViewController(model, jFrame);
        createEventViewController.setVisible(false);            //false by default


        //CONTROLLERS. BUTTONS THAT CONTAIN CONTROLLER LOGIC
        //Switch calendar date Buttons
        JButton previousButton = new JButton("<");
        Model finalModel = model;
        previousButton.addActionListener(e -> {
            finalModel.getCalendar().toPreviousDay(); finalModel.update();});
        previousButton.setBackground(Color.LIGHT_GRAY);
        JButton nextButton = new JButton(">");
        nextButton.addActionListener(e -> {finalModel.getCalendar().toNextDay(); finalModel.update();});
        nextButton.setBackground(Color.LIGHT_GRAY);
        //Quit Application Button
        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> {
            //serialize
            try {
                FileOutputStream file = new FileOutputStream("events.txt");     //save to events.txt
                ObjectOutputStream out = new ObjectOutputStream(file);
                out.writeObject(finalModel);                 //write the object --> save object data
                out.close();                        //close streams
                file.close();
            } catch (IOException i) {               //some weird thing happened
                i.printStackTrace();
            }
            jFrame.dispatchEvent(new WindowEvent(jFrame, WindowEvent.WINDOW_CLOSING));
        });   //ADOPTED FROM INTERNENT --> CLOSES THE APPLICATION RATHER THAN JUST THE FRAME
        quitButton.setBackground(Color.WHITE);
        //Create Event Button
        JButton createEventButton = new JButton("Create");
        createEventButton.addActionListener(e -> createEventViewController.setVisible(true));
        createEventButton.setBackground(Color.RED);
        createEventButton.setForeground(Color.WHITE);


        // panel for the switch dates and quit buttons
        JPanel switchAndQuit = new JPanel();
        switchAndQuit.setLayout(new BoxLayout(switchAndQuit, BoxLayout.LINE_AXIS));
        switchAndQuit.setBackground(Color.WHITE);
        switchAndQuit.add(Box.createHorizontalGlue());
        switchAndQuit.add(previousButton);
        switchAndQuit.add(nextButton);
        switchAndQuit.add(Box.createHorizontalGlue());
        switchAndQuit.add(quitButton);
        switchAndQuit.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        jFrame.add(switchAndQuit, BorderLayout.NORTH);

        JPanel monthAndDayViews = new JPanel(new GridBagLayout());      //layout for distributing space between monthcalendar and days events views
        monthAndDayViews.add(createEventButton);
        monthAndDayViews.setBackground(Color.WHITE);
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.gridwidth = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        monthAndDayViews.add(monthViewController, c);
        c.weightx = 0.75;
        c.gridwidth = 3;
        monthAndDayViews.add(dayView, c);

        jFrame.add(monthAndDayViews, BorderLayout.CENTER);

        jFrame.add(createEventViewController, BorderLayout.SOUTH);


        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
        jFrame.setSize(600,200);

        jFrame.repaint();

        //attach views to the model
        model.attach(createEventViewController);
        model.attach(monthViewController);
        model.attach(dayView);
    }
}
