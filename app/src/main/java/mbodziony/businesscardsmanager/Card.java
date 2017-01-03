package mbodziony.businesscardsmanager;

import android.widget.ImageView;

/**
 * Created by MBodziony on 2016-12-08.
 */
public class Card {

    // fields
    private long id;
    private ImageView logo;
    private String logoImgPath;
    private String name;
    private String mobile;
    private String phone;
    private String fax;
    private String email;
    private String web;
    private String company;
    private String address;
    private String job;
    private String facebook;
    private String tweeter;
    private String skype;
    private String other;

    // constructor
    public Card (String logoImgPath, String name, String mobile, String phone, String fax, String email, String web, String company,
                 String address, String job, String facebook, String tweeter, String skype, String other){
        this.logoImgPath = logoImgPath;
        this.name = name;
        this.mobile = mobile;
        this.phone = phone;
        this.fax = fax;
        this.email = email;
        this.web = web;
        this.company = company;
        this.address = address;
        this.job = job;
        this.facebook = facebook;
        this.tweeter = tweeter;
        this.skype = skype;
        this.other = other;
    }

    // getters
    public long getId()         {return id;}
    public ImageView getLogo()  {return logo;}
    public String getLogoImgPath() {return logoImgPath;}
    public String getName()     {return name;}
    public String getMobile()   {return mobile;}
    public String getPhone()    {return phone;}
    public String getFax()      {return fax;}
    public String getEmail()    {return email;}
    public String getWeb()      {return web;}
    public String getCompany()  {return company;}
    public String getAddress()  {return address;}
    public String getJob()      {return job;}
    public String getFacebook() {return facebook;}
    public String getTweeter()  {return tweeter;}
    public String getSkype()    {return skype;}
    public String getOther()    {return other;}

    // setters
    public void setId(long id)         {this.id = id;}
    public void setLogo(ImageView logo)  {this.logo = logo;}
    public void setLogoImgPath(String logoImgPath)    {this.logoImgPath = logoImgPath;}
    public void setName(String name)  {this.name = name;}
    public void setMobile(String mobile)  {this.mobile = mobile;}
    public void setPhone(String phone)  {this.phone = phone;}
    public void setFax(String fax)  {this.fax = fax;}
    public void setEmail(String email)  {this.email = email;}
    public void setWeb(String web)  {this.web = web;}
    public void setCompany(String company)  {this.company = company;}
    public void setAddress(String address)  {this.address = address;}
    public void setJob(String job)  {this.job = job;}
    public void setFacebook(String facebook)  {this.facebook = facebook;}
    public void setTweeter(String tweeter)  {this.tweeter = tweeter;}
    public void setSkype(String skype)  {this.skype = skype;}
    public void setOther(String other)  {this.other = other;}

    @Override
    public String toString(){
        return "";
    }

}
