public class Date {

    private int day;
    private int month;
    private int year;

    public Date() {
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        boolean leap=(((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0));
        if(day<0 || day>31){
            throw new IllegalArgumentException("Day must be a value between 1 and 31");
        }
        else if (month==2){
            if(leap==true){
                if(day>29) {
                    throw new IllegalArgumentException("Day must be a value between 1 and 29");
                }
                else{
                    this.day=day;
                }
            }
            else{
                if(day>28){
                        throw new IllegalArgumentException("Day must be a value between 1 and 28 ");
                }
                else{
                    this.day=day;
                }
            }
        }
        else if(month==4 || month==6 || month==9 || month==11){
            if(day>30){
                throw new IllegalArgumentException("Day must be a value between 1 and 30");
            }
            else{
                this.day=day;
            }
        }
        else{
            this.day=day;
        }
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        if(month<0 || month>12){
            throw new IllegalArgumentException("Month must be a value between 1 and 12");
        }
        else{
            this.month=month;
        }
    }

    public int getYear() {
        return year;
    }

    public void setYear (int year) {
        if(year<1000){
            throw new IllegalArgumentException("Year should be a value greater than 1000");
        }
        else{
            this.year=year;
        }
    }





}
