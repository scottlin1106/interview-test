
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Scanner;

public class ShoppingReceipt {

    public static void main(String[] args) {
        HashMap<String, Double> cityTaxMap = new HashMap<String, Double>();
        cityTaxMap.put("CA", 9.75);
        cityTaxMap.put("NY", 8.875);

        HashMap<String, Double> itemMap = new HashMap<String, Double>();
        itemMap.put("book", 17.99);
        itemMap.put("potato chips", 3.99);
        itemMap.put("pencil", 2.99);
        itemMap.put("shirt", 29.99);

        HashMap<String, String> typeMap = new HashMap<String, String>();
        typeMap.put("book", "book");
        typeMap.put("potato chips", "food");
        typeMap.put("pencil", "stationery");
        typeMap.put("shirt", "clothing");

        JSONObject itemList = JSONObject.fromObject(getBuyList(itemMap));
        String city = getCity(cityTaxMap);
        double cityTax = cityTaxMap.get(city) / 100;
        double subtotal = 0.0;
        double tax = 0.0;
        System.out.printf("%-15s %-10s %-10s%n", "Item", "Price", "Quantity");
        for (Object item : itemList.keySet()) {
            System.out.printf("%-15s $%-9.2f %-10d%n", item, itemMap.get(item), itemList.get(item));
            subtotal += itemMap.get(item) * itemList.getInt(item.toString());
            if ("CA".equalsIgnoreCase(city)) {
                if (!"food".equalsIgnoreCase(typeMap.get(item))) {
                    tax += roundUpToNearestFiveCents(itemMap.get(item) * itemList.getInt(item.toString()) * cityTax);
                }
            }
            if ("NY".equalsIgnoreCase(city)) {
                if (!typeMap.get(item).equals("food") && !typeMap.get(item).equals("clothing")) {
                    tax += roundUpToNearestFiveCents(itemMap.get(item) * itemList.getInt(item.toString()) * cityTax);
                }
            }
        }
        System.out.printf("%-15s %-10s $%-10.2f%n", "subtotal:", "", subtotal);
        System.out.printf("%-15s %-10s $%-10.2f%n", "tax:", "", tax);
        System.out.printf("%-15s %-10s $%-10.2f%n", "total:", "", subtotal + tax);
    }

    public static String getCity(HashMap<String, Double> cityTaxMap) {
        String keyIn = "";
        System.out.println("可購買地點及稅率如下:");
        for (String item : cityTaxMap.keySet()) {
            System.out.println(item + " : " + cityTaxMap.get(item));
        }
        Scanner Scanner = new Scanner(System.in);
        System.out.println("請選擇購買地點");
        while (true) {
            Scanner = new Scanner(System.in);
            keyIn = Scanner.nextLine();
            if (cityTaxMap.containsKey(keyIn)) {
                System.out.println("您選擇了" + keyIn + "該地區，稅率為" + cityTaxMap.get(keyIn) + "%");
                break;
            } else {
                System.out.println("該城市並無再購買範圍內，請依清單輸入，謝謝。");
            }
        }
        return keyIn;
    }

    public static JSONObject getBuyList(HashMap<String, Double> itemMap) {
        Scanner Scanner, num;
        JSONObject buyList = new JSONObject();

        for (String item : itemMap.keySet()) {
            System.out.println(item + " : " + itemMap.get(item));
        }
        System.out.println("請選擇購買商品，選購完畢請輸入 exit 結束選購");
        while (true) {
            Scanner = new Scanner(System.in);
            String keyIn = Scanner.nextLine();
            if (itemMap.containsKey(keyIn)) {
                if (buyList.containsKey(keyIn)) {
                    System.out.println("先前已經選購過該商品，請輸入欲調整購買的數量，不須購買請輸入0");
                    num = new Scanner(System.in);
                    int quantity = num.nextInt();
                    if (quantity == 0) {
                        buyList.remove(keyIn);
                    } else {
                        buyList.put(keyIn, quantity);
                    }
                } else {
                    System.out.println("請輸入購買數量");
                    num = new Scanner(System.in);
                    buyList.put(keyIn, num.nextInt());
                }
                System.out.println("請繼續選購商品，選購完畢請輸入 exit 結束選購");

            } else if ("exit".equals(keyIn)) {
                if(buyList.size()<1){
                    System.out.println("您尚未選購任何商品，期待您下次光臨。");
                    System.exit(0);
                }
                System.out.println("感謝選購以下商品" + buyList.toString());
                break;
            } else {
                System.out.println("該商家無此商品，請確認後重新輸入");
            }
        }
        return buyList;
    }

    public static double roundUpToNearestFiveCents(double value) {
        return Math.ceil(value / 0.05) * 0.05;
    }
}
