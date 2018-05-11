import java.io.Serializable;

class Event implements Serializable {

    //times
    private int start;
    private int end;
    private String description;

    Event(String description, int start, int end){
        this.description = description;
        this.start = start;
        this.end = end;
    }

    //alternative constructor that converts String times to Integer equivalents
    Event(String description, String start, String end){
        this.description = description;
        String[] startSplit = start.split(":|AM|PM|am|pm");               //regex to use :, AM and PM as delimiters
        String[] endSplit = end.split(":|AM|PM|am|pm");
        this.start = Integer.parseInt(startSplit[0]) * 100 + Integer.parseInt(startSplit[1]) + (start.matches("pm|PM")? 1200 : 0); //add extra 1200 if a PM time is given
        this.end = Integer.parseInt(endSplit[0]) * 100 + Integer.parseInt(endSplit[1]) + (end.matches("pm|PM")? 1200 : 0);
        System.out.println(this.start);
        this.start = ((this.start - 1200) >= 0) && start.matches("am|AM") ? Integer.parseInt(startSplit[1]) : this.start;           //if either time is 12am, it should be equal to zero!
        this.end = ((this.end - 1200) >= 0) && end.matches("am|AM") ? Integer.parseInt(startSplit[1]) : this.end;
    }


    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String formattedStartTime(){
        return String.format("%02d:00", start);
    }

    public String formattedEndTime(){
        return String.format("%02d:00", end);
    }
}
