import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.synth.SynthScrollBarUI;
import java.util.*;


/**
 * A model object that represents a GregorianCalendar; with the ability to store a collection of events
 */
public class EventCalendar extends GregorianCalendar {

    //could also make so that Events contain a date(GregorianCalendar) ---> Properly better when serializing???
    private Map<GregorianCalendar, ArrayList<Event>> events;
    public EventCalendar(){
        super();
        events = new HashMap<>();
    }

    /**
     * @param date the date of which to retrieve events from
     * @return the events of the requested date
     */
    private ArrayList<Event> getEvents(GregorianCalendar date){
        if(!events.containsKey(date))   events.put(date, new ArrayList<>());            //create mapping if not already existing
        return events.get(date);
    }

    /**
     * @return events of the current date
     */
    public ArrayList<Event> getEventsOfCurrentDate(){
        return getEvents(this);
    }

    /**
     * @param event event wished to add
     * @return true if added
     * @throws EventException exception w/ message if event not added
     */
    //add a date if there is no conflict
    public boolean addEvent(Event event) throws EventException{
        ArrayList<Event> eventsOfSelectedDate = getEventsOfCurrentDate();
        for(Event e: eventsOfSelectedDate){
            if((event.getStart() > e.getStart() && event.getStart() < e.getEnd()) ||
                    (event.getEnd() >  e.getStart() && event.getEnd() < e.getEnd()) ||
                    (event.getStart() < e.getStart() && event.getEnd() > e.getEnd())) throw new EventException("Event has a time conflict with another Event");           //return false if there is a time collision
            if(event.getStart() > event.getEnd())                                     throw new EventException("Event ends before it starts");           //return false if the endTime is earlier than startTime
        }
        return eventsOfSelectedDate.add(event);
    }

    public void toNextDay(){
        add(Calendar.DAY_OF_MONTH, 1);

    }

    public void toPreviousDay(){
        add(Calendar.DAY_OF_MONTH, -1);
    }

    public void switchDate(GregorianCalendar newDate){
        set(Calendar.DAY_OF_MONTH, newDate.get(Calendar.DAY_OF_MONTH));
    }

    public GregorianCalendar getCalendarCopy(){
        return (GregorianCalendar) clone();
    }
}
