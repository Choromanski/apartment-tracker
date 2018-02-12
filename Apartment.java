public class Apartment {
    private String address, city;
    private int apartmentNumber, zipCode;
    private double price, sqft;

    public Apartment(String address, int number, String city, int zip, double price, double sqft){
        this.address = address;
        this.apartmentNumber = number;
        this.city = city;
        this.zipCode = zip;
        this.price = price;
        this.sqft = sqft;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public double getPrice(){
        return price;
    }

    public double getSqft(){
        return sqft;
    }

    public String getCity(){
        return city;
    }

    public String getKey(){
        return address + apartmentNumber + zipCode;
    }

    public String toString(){
        return address + ", " + city + ", " + zipCode + "\nApartment #" + apartmentNumber + "\n" + String.format("%.2f", sqft) +" square feet for $" + String.format("%.2f", price) + " a month";
    }
}
