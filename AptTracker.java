import java.util.Scanner;

public class AptTracker {

    private static PriorityQueue pq;

    public static void main(String[] args) {

        pq = new PriorityQueue();

        int selection = 0;
        while (selection != 8){
            selection = prompt();
            switch (selection){
                case 1:
                    newApartment();
                    break;
                case 2:
                    findApartment("update");
                    break;
                case 3:
                    findApartment("remove");
                    break;
                case 4:
                    System.out.println(pq.getCheapest(null));
                    break;
                case 5:
                    System.out.println(pq.getLargest(null));
                    break;
                case 6:
                    System.out.println(pq.getCheapest(byCity()));
                    break;
                case 7:
                    System.out.println(pq.getLargest(byCity()));
                    break;
                default:
                    break;
            }
        }
    }

    private static int prompt(){
        Scanner scan = new Scanner(System.in);
        int selection;
        System.out.println("\nApartment Tracker Options\n1 - Add a new apartment\n2 - Update existing apartment"+
        "\n3 - Remove apartment from consideration\n4 - Get cheapest apartment\n5 - Get largest apartment" +
        "\n6 - Get cheapest apartment by city\n7 - Get largest apartment by city\n8 - Quit application");
        System.out.println("\nSelect one of the options above (1-8)\n");
        selection = scan.nextInt();
        if(selection < 9 && selection > 0){
            return selection;
        }else{
            System.out.println("\nERROR: Please enter a valid option (1-8)");
            return prompt();
        }
    }

    private static void newApartment(){
        Scanner scan = new Scanner(System.in);
        System.out.print("\nStreet address of new apartment: ");
        String address = scan.nextLine();
        System.out.print("Apartment number of new apartment: ");
        int number = scan.nextInt();
        scan.nextLine();
        System.out.print("City of new apartment: ");
        String city = scan.nextLine();
        System.out.print("ZIP code of new apartment: ");
        int zip = scan.nextInt();
        System.out.print("Price of new apartment: ");
        double cost = scan.nextDouble();
        System.out.print("Square footage of new apartment: ");
        double sqr = scan.nextDouble();

        Apartment add = new Apartment(address, number, city, zip, cost, sqr);
        pq.addApartment(add);
    }

    private static void findApartment(String function){
        Scanner scan = new Scanner(System.in);
        System.out.print("Street address of apartment to " + function + ": ");
        String address = scan.nextLine();
        System.out.print("Apartment number of apartment to " + function + ": ");
        int number = scan.nextInt();
        System.out.print("ZIP code of apartment to " + function + ": ");
        int zip = scan.nextInt();

        Apartment temp = new Apartment(address, number, null, zip, -1, -1);
        if(function.equals("update")){
            System.out.print("Updated price of apartment: ");
            double cost = scan.nextDouble();
            temp.setPrice(cost);
            pq.editApartment(temp);
        }else{
            pq.removeApartment(temp);
        }

    }

    private static String byCity(){
        Scanner scan = new Scanner(System.in);
        System.out.print("What city: ");
        return scan.nextLine();
    }
}
