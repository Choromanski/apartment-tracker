public class PriorityQueue {

    private Apartment[] price, sqft;
    private SeparateChainingHashST<String, Integer> priceIndex, sqftIndex;
    private SeparateChainingHashST<String, Apartment> priceCity, sqftCity;
    private int numApartments;
    private int fullSize = 16;

    public PriorityQueue(){
        price = new Apartment[fullSize];
        sqft = new Apartment[fullSize];
        priceIndex = new SeparateChainingHashST<String, Integer>();
        sqftIndex = new SeparateChainingHashST<String, Integer>();
        priceCity = new SeparateChainingHashST<String, Apartment>();
        sqftCity = new SeparateChainingHashST<String, Apartment>();
        numApartments = 0;
    }

    public void addApartment(Apartment add){
        if(numApartments == fullSize){
            expand();
        }
        price[numApartments] = add;
        sqft[numApartments] = add;
        priceIndex.put(add.getKey(), numApartments);
        sqftIndex.put(add.getKey(), numApartments);
        swimPrice(numApartments);
        swimSqft(numApartments);
        numApartments++;
        if(!priceCity.contains(add.getCity()) || priceCity.get(add.getCity()).getPrice() > add.getPrice()){
            priceCity.delete(add.getCity());
            priceCity.put(add.getCity(), add);
        }
        if(!sqftCity.contains(add.getCity()) || sqftCity.get(add.getCity()).getSqft() < add.getSqft()){
            sqftCity.delete(add.getCity());
            sqftCity.put(add.getCity(), add);
        }
    }

    public void editApartment(Apartment edit){
        if(priceIndex.contains(edit.getKey())){
            int index = priceIndex.get(edit.getKey());
            double oldPrice = price[index].getPrice();
            price[index].setPrice(edit.getPrice());
            Apartment apt = price[index];
            swimPrice(index);
            sinkPrice(index);
            if(priceCity.get(apt.getCity()).equals(apt)){
                if(apt.getPrice() < oldPrice){
                    priceCity.delete(apt.getCity());
                    priceCity.put(apt.getCity(), apt);
                }else{
                    priceCity.delete(apt.getCity());
                    for(int i = 0; i<numApartments; i++){
                        if(price[i].getCity().equals(apt.getCity()) && (!priceCity.contains(apt.getCity()) || priceCity.get(apt.getCity()).getPrice() > price[i].getPrice())){
                            priceCity.delete(price[i].getCity());
                            priceCity.put(price[i].getCity(), price[i]);
                        }
                    }
                }
            }else if(apt.getPrice() < priceCity.get(apt.getCity()).getPrice()){
                priceCity.delete(apt.getCity());
                priceCity.put(apt.getCity(), apt);
            }
            sqft[sqftIndex.get(edit.getKey())].setPrice(edit.getPrice());
            if(sqftCity.get(apt.getCity()).equals(apt)){
                sqftCity.delete(apt.getCity());
                sqftCity.put(apt.getCity(),apt);
            }
        }
    }

    public void removeApartment(Apartment remove){
        if(priceIndex.contains(remove.getKey())){
            int index = priceIndex.get(remove.getKey());
            Apartment apt = price[index];
            numApartments--;
            exch(index, numApartments, "price");
            price[numApartments]=null;
            priceIndex.delete(remove.getKey());
            swimPrice(index);
            sinkPrice(index);
            if(priceCity.get(apt.getCity()).equals(apt)){
                priceCity.delete(apt.getCity());
                for(int i = 0; i<numApartments; i++){
                    if(price[i].getCity().equals(apt.getCity()) && (!priceCity.contains(price[i].getCity()) || priceCity.get(price[i].getCity()).getPrice() > price[i].getPrice())){
                        priceCity.delete(price[i].getCity());
                        priceCity.put(price[i].getCity(), price[i]);
                    }
                }
            }

            index = sqftIndex.get(remove.getKey());
            apt = sqft[index];
            exch(index, numApartments, "sqft");
            sqft[numApartments]=null;
            sqftIndex.delete(remove.getKey());
            swimSqft(index);
            sinkSqft(index);
            if(sqftCity.get(apt.getCity()).equals(apt)){
                sqftCity.delete(apt.getCity());
                for(int i = 0; i<numApartments; i++){
                    if(sqft[i].getCity().equals(apt.getCity()) && (!sqftCity.contains(sqft[i].getCity()) || sqftCity.get(sqft[i].getCity()).getSqft() < sqft[i].getPrice())){
                        sqftCity.delete(sqft[i].getCity());
                        sqftCity.put(sqft[i].getCity(), sqft[i]);
                    }
                }
            }
        }
    }

    private void expand(){
        Apartment[] temp1 = new Apartment[fullSize*2];
        Apartment[] temp2 = new Apartment[fullSize*2];
        for(int i = 0; i<fullSize; i++){
            temp1[i] = price[i];
            temp2[i] = sqft[i];
        }
        fullSize *= 2;
        price = temp1;
        sqft = temp2;
    }

    public Apartment getCheapest(String city){
        if(city == null){
            return price[0];
        }
        return priceCity.get(city);
    }

    public Apartment getLargest(String city){
        if(city == null){
            return sqft[0];
        }
        return sqftCity.get(city);
    }

    private void swimPrice(int k){
        while (k > 0 && lessPrice(k, (k-1)/2)) {
            exch(k, (k-1)/2, "price");
            k = (k-1)/2;
        }
    }

    private void swimSqft(int k){
        while (k > 0 && lessSqft(k, (k-1)/2)) {
            exch(k, (k-1)/2, "sqft");
            k = (k-1)/2;
        }
    }
    private void sinkPrice(int k) {
        while (2*(k+1) < numApartments) {
            int j = 2*(k+1);
            if (j < numApartments && lessPrice(j, j-1)) j++;
            if (!lessPrice(j-1, k)) break;
            exch(k, j-1, "price");
            k = j-1;
        }
    }

    private void sinkSqft(int k) {
        while (2*(k+1) < numApartments) {
            int j = 2*(k+1);
            if (j < numApartments && lessSqft(j, j-1)) j++;
            if (!lessSqft(j-1, k)) break;
            exch(k, j-1, "sqft");
            k = j-1;
        }
    }

    private void exch(int i, int j, String mode){
        if(mode.equals("price")){
            priceIndex.delete(price[i].getKey());
            priceIndex.delete(price[j].getKey());
            Apartment swap = price[i];
            price[i] = price[j];
            price[j] = swap;
            priceIndex.put(price[i].getKey(), i);
            priceIndex.put(price[j].getKey(), j);
        }else{
            sqftIndex.delete(sqft[i].getKey());
            sqftIndex.delete(sqft[j].getKey());
            Apartment swap = sqft[i];
            sqft[i] = sqft[j];
            sqft[j] = swap;
            sqftIndex.put(sqft[i].getKey(), i);
            sqftIndex.put(sqft[j].getKey(), j);
        }
    }

    private boolean lessPrice(int i, int j){
        return price[i] != null && price[j] != null && price[i].getPrice() < price[j].getPrice();
    }

    private boolean lessSqft(int i, int j){
        return sqft[i] != null && sqft[j] != null && sqft[i].getSqft() > sqft[j].getSqft();
    }
}
