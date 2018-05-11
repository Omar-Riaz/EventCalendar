import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

//class that represents the Month View, along with holding the controller for switching dates
public class MonthViewController extends JPanel implements ChangeListener{

//    private ArrayList<JTextArea> dates;       //dates contain a set of dummy variables, followed by a set of Dates --> JtextAreas  with ChangeListeners

    Model model;
    JLabel monthAndYear;

    /**
     * @param model the model object that this view will get its data from
     */
    MonthViewController(Model model){
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        setOpaque(true);

        this.model = model;
        monthAndYear = new JLabel();
        draw();
    }

    /**
     * @param e the changeEvent; I.E, the model
     */
    @Override
    public void stateChanged(ChangeEvent e) {

        EventCalendar calendar = ((Model) e.getSource()).getCalendarCopy();
        monthAndYear.setText(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + " " + calendar.get(Calendar.YEAR));

        draw();

        repaint();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
//        Graphics2D g2 = (Graphics2D) g;
    }

    //initialize the layout of the calendar
    public void draw(){

        removeAll();

        JPanel gridPanel = new JPanel(new GridLayout(7,7));

        //put in row containing days of the week
        gridPanel.add(new JLabel("S"));
        gridPanel.add(new JLabel("M"));
        gridPanel.add(new JLabel("T"));
        gridPanel.add(new JLabel("W"));
        gridPanel.add(new JLabel("T"));
        gridPanel.add(new JLabel("F"));
        gridPanel.add(new JLabel("S"));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        //top part
        JPanel monthAndYearPanel = new JPanel(new FlowLayout());
        monthAndYearPanel.add(monthAndYear);
        monthAndYearPanel.setBackground(Color.WHITE);

        //add to bigger panel
        add(monthAndYearPanel, c);


        //set calendar to the first date of this month. It is a copy because it's used to simply iterate through the calendar to draw it
        EventCalendar calendarCopy = (EventCalendar) model.getCalendarCopy();
        calendarCopy.set(Calendar.DAY_OF_MONTH, 1);
        int dayOfWeek = calendarCopy.get(Calendar.DAY_OF_WEEK);
        calendarCopy.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        //render some single space textAreas before the first date of the month
        while(calendarCopy.get(Calendar.DAY_OF_WEEK) != dayOfWeek) {
            calendarCopy.add(Calendar.DAY_OF_MONTH, 1);
            gridPanel.add(new JLabel(" "));
        }
        //generate dates from calendar
        for(int i = 1; i <= calendarCopy.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            Date aDate =  new Date(""+calendarCopy.get(Calendar.DAY_OF_MONTH));
            if(model.getCalendarCopy().get(Calendar.DAY_OF_MONTH) == calendarCopy.get(Calendar.DAY_OF_MONTH)){
                aDate.setBackground(Color.GRAY);
                aDate.setOpaque(true);
            }
            gridPanel.add(aDate);
            calendarCopy.toNextDay();
        }

        //fill in with dummy textAreas again
        while(calendarCopy.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            calendarCopy.add(Calendar.DAY_OF_MONTH, 1);
            gridPanel.add(new JLabel(" "));
        }
        while(gridPanel.getComponentCount() < 43){
            gridPanel.add(new JLabel(" ")); //needed to obey grid and draw stuff properly
        }
//
        gridPanel.setBackground(Color.WHITE);
//

        //finally, add calendar portion to GridBagLayout
        c.gridx = 0;            //do I need a y???
        c.gridy = 1;
        c.gridwidth = 7;
        c.gridheight = 7;
        c.weightx = 1;
        add(gridPanel, c);

        revalidate();
    }

    //private class to classify each date as a controller
    private class Date extends JLabel{

        /**
         * @param date the date to create this JLabel out of
         */
//        //add date to the collection
//        @Override
//        public boolean add(JTextArea date){
//            return super.add(date);
//        };
        public Date(String date){
            super(date);
            setText(String.valueOf(date));
            JLabel jLabel = this;
            addMouseListener(new MouseListener() {
                //when a date is clicked, it tells the model to switch date to the date it represents
                @Override
                public void mouseClicked(MouseEvent e){
                    GregorianCalendar newDate = new GregorianCalendar();
                    newDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(jLabel.getText()));         //the JTextArea contains the date to navigate
                    model.getCalendar().switchDate(newDate);
                    model.update();
                }

                @Override
                public void mousePressed(MouseEvent e) { }

                @Override
                public void mouseReleased(MouseEvent e) { }

                @Override
                public void mouseEntered(MouseEvent e) { }

                @Override
                public void mouseExited(MouseEvent e) { }
            });
        }
    }
}
