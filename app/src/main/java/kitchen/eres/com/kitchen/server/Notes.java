package kitchen.eres.com.kitchen.server;


import java.util.Collection;

public class Notes {
    private ObservableCollection<NotificationData> Notes=new ObservableCollection<>();

    public Collection<? extends NotificationData> getNotes() {
        return Notes;}

    public void setNotes(ObservableCollection<NotificationData> notes) {
        Notes = notes;
    }
}