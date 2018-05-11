import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;

//The model class. Responsible for providing an eventCalendar, copies of the eventCalendar and updating views
public class Model implements Serializable {

    private ArrayList<ChangeListener> listeners;
    private EventCalendar eventCalendar;

    /**
     * Constructor for Model object
     */
    public Model(){
        listeners = new ArrayList<>();
        eventCalendar = new EventCalendar();
    }

    /**
     * @param listener Listener to attach to this model
     */
    //attach a view to this model
    public void attach(ChangeListener listener){
        listeners.add(listener);
        listener.stateChanged(new ChangeEvent(this));                   //when adding new view, force an update to it
    }

    /**
     * Updates all attached models
     */
    //update all the views attached to this model
    public void update(){
        ChangeEvent changeEvent = new ChangeEvent(this);
        for(ChangeListener changeListener: listeners){
            changeListener.stateChanged(changeEvent);
        }
    }


    /**
     * @return an eventCalendar
     */
    public EventCalendar getCalendar(){
        return eventCalendar;
    }

    /**
     * @return copy of an eventCalendar
     */
    public EventCalendar getCalendarCopy(){
        return (EventCalendar) eventCalendar.clone();
    }

}
