
package cn.javak.pojo;

public class Weather {
    private String cityid;

    private String date;

    private String week;

    private String update_time;

    private String city;//城市

    private String cityEn;

    private String country;

    private String countryEn;

    private String wea;//天气状况

    private String wea_img;

    private String tem;//当前温度

    private String tem1;//最高温度

    private String tem2;//最低温度

    private String win;//风向

    private String win_speed;//风级

    private String win_meter;//风速

    private String humidity;

    private String visibility;//能见度
    private String air_level;//空气等级
    private String air_tips;//小提示

    private String pressure;

    private String air;

    private String air_pm25;



    private Alarm alarm;

    public void setCityid(String cityid){
        this.cityid = cityid;
    }
    public String getCityid(){
        return this.cityid;
    }
    public void setDate(String date){
        this.date = date;
    }
    public String getDate(){
        return this.date;
    }
    public void setWeek(String week){
        this.week = week;
    }
    public String getWeek(){
        return this.week;
    }
    public void setUpdate_time(String update_time){
        this.update_time = update_time;
    }
    public String getUpdate_time(){
        return this.update_time;
    }
    public void setCity(String city){
        this.city = city;
    }
    public String getCity(){
        return this.city;
    }
    public void setCityEn(String cityEn){
        this.cityEn = cityEn;
    }
    public String getCityEn(){
        return this.cityEn;
    }
    public void setCountry(String country){
        this.country = country;
    }
    public String getCountry(){
        return this.country;
    }
    public void setCountryEn(String countryEn){
        this.countryEn = countryEn;
    }
    public String getCountryEn(){
        return this.countryEn;
    }
    public void setWea(String wea){
        this.wea = wea;
    }
    public String getWea(){
        return this.wea;
    }
    public void setWea_img(String wea_img){
        this.wea_img = wea_img;
    }
    public String getWea_img(){
        return this.wea_img;
    }
    public void setTem(String tem){
        this.tem = tem;
    }
    public String getTem(){
        return this.tem;
    }
    public void setTem1(String tem1){
        this.tem1 = tem1;
    }
    public String getTem1(){
        return this.tem1;
    }
    public void setTem2(String tem2){
        this.tem2 = tem2;
    }
    public String getTem2(){
        return this.tem2;
    }
    public void setWin(String win){
        this.win = win;
    }
    public String getWin(){
        return this.win;
    }
    public void setWin_speed(String win_speed){
        this.win_speed = win_speed;
    }
    public String getWin_speed(){
        return this.win_speed;
    }
    public void setWin_meter(String win_meter){
        this.win_meter = win_meter;
    }
    public String getWin_meter(){
        return this.win_meter;
    }
    public void setHumidity(String humidity){
        this.humidity = humidity;
    }
    public String getHumidity(){
        return this.humidity;
    }
    public void setVisibility(String visibility){
        this.visibility = visibility;
    }
    public String getVisibility(){
        return this.visibility;
    }
    public void setPressure(String pressure){
        this.pressure = pressure;
    }
    public String getPressure(){
        return this.pressure;
    }
    public void setAir(String air){
        this.air = air;
    }
    public String getAir(){
        return this.air;
    }
    public void setAir_pm25(String air_pm25){
        this.air_pm25 = air_pm25;
    }
    public String getAir_pm25(){
        return this.air_pm25;
    }
    public void setAir_level(String air_level){
        this.air_level = air_level;
    }
    public String getAir_level(){
        return this.air_level;
    }
    public void setAir_tips(String air_tips){
        this.air_tips = air_tips;
    }
    public String getAir_tips(){
        return this.air_tips;
    }
    public void setAlarm(Alarm alarm){
        this.alarm = alarm;
    }
    public Alarm getAlarm(){
        return this.alarm;
    }
}
