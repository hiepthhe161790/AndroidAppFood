package vn.edu.fpt.fooddelivery.model;

public class Food {
    private String title;
    private int pic;
    private String description;
    private Double fee;
    private int numberInCart;
    private int calo;
    private String category;
    public Food(String title, int pic, String description, Double fee, int numberInCart,int calo) {
        this.title = title;
        this.pic = pic;
        this.description = description;
        this.fee = fee;
        this.numberInCart = numberInCart;
        this.calo = calo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public int getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }
    public Food(String title, int pic, String description, Double fee, int calo, int numberInCart, String category) {
        this.title = title;
        this.pic = pic;
        this.description = description;
        this.fee = fee;
        this.calo = calo;
        this.numberInCart = numberInCart;
        this.category = category;
    }
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCalo() {
        return calo;
    }

    public void setCalo(int calo) {
        this.calo = calo;
    }

    public static String TABLE_NAME = "Food table";
    public static String COL_ID = "id";
    public static String COL_TITLE = "title";
    public static String COL_IMG = "imge";

}
