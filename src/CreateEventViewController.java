import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.chrono.JapaneseDate;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

//A class representing the View for the CreateEvent menu, along with the controller responsible for collecting user data
public class CreateEventViewController extends JPanel implements ChangeListener {

    private JLabel date;
    private JTextField eventName;
    private JTextField startTime;
    private JTextField endTime;
//    private SaveButtonController saveButton;            //the strictly controller portion --> invokes the model

    public CreateEventViewController(Model model, JFrame jFrame) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.LINE_AXIS));
        date = new JLabel();
        eventName = new JTextField();
        startTime = new JTextField();
        endTime = new JTextField();
        SaveButtonController saveButton = new SaveButtonController(model, jFrame);
        add(eventName);
        jPanel.add(date);
        jPanel.add(startTime);
        jPanel.add(new JLabel("to"));
        jPanel.add(endTime);
        jPanel.add(saveButton);
        add(jPanel);
    }

    //update the Date text box with the selected date
    @Override
    public void stateChanged(ChangeEvent e) {
        Model model = (Model) e.getSource();
        GregorianCalendar calendar = model.getCalendar();
        date.setText(String.valueOf(calendar.get(Calendar.MONTH)+1 + "/" + calendar.get(Calendar.DAY_OF_MONTH)+ "/" + calendar.get(Calendar.YEAR)));
        draw();
    }

    public void draw(){
    }

    //A button that serves as a controller
    private class SaveButtonController extends JButton {

        private Model model;        //only the controller has access to the model
        private JFrame jFrame;

        /**
         * @param model model object to update
         * @param jFrame frame of application to use to render dialog
         */
        SaveButtonController(Model model, JFrame jFrame){
            super("Save");
            this.model = model;
            addActionListener(new ActionListener() {
                @Override        //create a new event when this button is triggered
                public void actionPerformed(ActionEvent e) {
                    JDialog jDialog = new JDialog();
                    boolean addResult = false;
                    try {
                        addResult = model.getCalendar().addEvent(new Event(eventName.getText(), startTime.getText(), endTime.getText()));
                    } catch (EventException exception) {
                        JOptionPane.showMessageDialog(jFrame, exception.getMessage());
                    }
                    if(addResult) JOptionPane.showMessageDialog(jFrame, "Event added succesfully");
                    model.update();
                }
            });
        }

    }
}
