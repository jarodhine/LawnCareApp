package au.edu.jcu.cp3406.lawncare;

public class DeliveryItem {
    private String Time;
    private String Address;
    private String Type;

    public DeliveryItem(String time, String address, String type) {
        Time = time;
        Address = address;
        Type = type;
    }

    public String getTime() {
        return Time;
    }

    public String getAddress() {
        return Address;
    }

    public String getType() {
        return Type;
    }
}
