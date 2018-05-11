import javafx.scene.control.TextFormatter;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DayView extends JPanel implements ChangeListener {

    EventCalendar calendar;     //I save an instance of calendar to avoid access to the model. View should not have access to whole model, just the data it needs
    JLabel date;
    JLabel startTime;

    /**
     * @param calendar the calendar to associate the dayView with --> is on a particular day with with the view can retrieve events from
     */
    DayView(EventCalendar calendar){
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBackground(Color.WHITE);
        this.calendar = calendar;

        date = new JLabel(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG,Locale.getDefault()) + " " + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.DAY_OF_MONTH));

        draw(calendar);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
    }

    /**
     * @param e the model object evoking the stateChange to notify the view
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        Model model = (Model) e.getSource();
        calendar = model.getCalendar();
        date = new JLabel(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG,Locale.getDefault()) + " " + (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.DAY_OF_MONTH));
        draw(calendar);
        repaint();
    }

    public void draw(EventCalendar eventCalendar){

        //clear previous view
        this.removeAll();

        //show the current date at the top
        add(date);

        //rendering a list of events for this day
        JPanel eventsPanel = new JPanel();
        eventsPanel.setLayout(new BoxLayout(eventsPanel, BoxLayout.PAGE_AXIS));
        for(int i = 0 ; i < 24; i++){
            JPanel jPanel = new JPanel(new BorderLayout(20,0));
            jPanel.add(new JLabel(""+ (i == 0 ? 12 : (i < 13 ? i : i-12)) + (i < 12 ? "am" : "pm")), BorderLayout.WEST);
            jPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            JPanel eventsAtHour = new JPanel();
            for(Event e: eventCalendar.getEventsOfCurrentDate()){
                int eventHour = e.getStart() / 100;
                System.out.println(eventHour);
                eventsAtHour.setLayout(new BoxLayout(eventsAtHour, BoxLayout.PAGE_AXIS));
                if(eventHour == i) {
                    int minutes = (e.getStart() % (int) (Math.floor((double) e.getStart() / 100) * 100));
                    String startTimeFormat = e.getStart() / 100 + ":" + (minutes < 10 ? "0"+minutes : ""+minutes);
                    JLabel timeAndEvent = new JLabel(startTimeFormat + (e.getStart() >= 12 ? "am" : "pm") + " - " + e.getDescription());
                    timeAndEvent.setBackground(Color.LIGHT_GRAY);
                    eventsAtHour.add(timeAndEvent);
                }
            }
            jPanel.add(eventsAtHour, BorderLayout.CENTER);
            jPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            add(jPanel);
            eventsPanel.add(jPanel);
        }
        JScrollPane jScrollPane = new JScrollPane(eventsPanel);
        add(jScrollPane);
//        //rendering a list of events for this day
//        for(Event e: eventCalendar.getEventsOfCurrentDate()){
//            JPanel jPanel = new JPanel(new BorderLayout(20,0));
//            String startTimeFormat = e.getStart()/100 + ":" + (e.getStart() % (int)(Math.floor((double)e.getStart()/100)*100));
//            JLabel startTime = new JLabel(startTimeFormat + (e.getStart() >= 12 ? "am" : "pm"));           //add am or pm as needed
//            JLabel description = new JLabel(e.getDescription());
//            description.setBackground(Color.LIGHT_GRAY);
//            jPanel.add(startTime, BorderLayout.WEST);
//            jPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
//            jPanel.add(description, BorderLayout.CENTER);
//            add(jPanel);
//        }



        revalidate();
    }
}
