package dcs.suc.trip.ListView;

public class Cart {
    String userId,packageId,cartId,aa,img,tripName,tripDate,duration,dep, remaining,country;
    int price,cprice,sprice,AcounterTxt,CcounterTxt,ScounterTxt,totalAmount;

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getUserId() {
        return userId;

    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getAa() {
        return aa;
    }

    public void setAa(String aa) {
        this.aa = aa;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCprice() {
        return cprice;
    }

    public void setCprice(int cprice) {
        this.cprice = cprice;
    }

    public int getSprice() {
        return sprice;
    }

    public void setSprice(int sprice) {
        this.sprice = sprice;
    }
    public String getDep() {
        return dep;
    }

    public void setDep(String dep) {
        this.dep = dep;
    }

    public String getRemaining() {
        return remaining;
    }

    public void setRemaining(String remaining) {
        this.remaining = remaining;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getAcounterTxt() {
        return AcounterTxt;
    }

    public void setAcounterTxt(int AcounterTxt) {
        this.AcounterTxt = AcounterTxt;
    }

    public int getCcounterTxt() {
        return CcounterTxt;
    }

    public void setCcounterTxt(int CcounterTxt) {
        this.CcounterTxt = CcounterTxt;
    }


    public int getScounterTxt() {
        return ScounterTxt;
    }

    public void setScounterTxt(int ScounterTxt) {
        this.ScounterTxt = ScounterTxt;
    }



    public void setTotalAmount(){

        int totalAmount = (ScounterTxt * sprice) + (AcounterTxt * price) + (CcounterTxt * cprice);
        this.totalAmount = totalAmount;

    }
    public int gettotalAmount() {
        return totalAmount;
    }

}



