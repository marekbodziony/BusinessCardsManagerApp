package mbodziony.businesscardsmanager;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by MBodziony on 2017-01-22.
 *
 * Unit tests of methods implemented in Card class
 */

public class CardUnitTest {

    private Card card;

    @Before
    public void setUp(){
        card = new Card(null,"Jan Kowalski","+48500100100","0226669988",null,"jan.kowalski@gmail.com","http://www.google.pl",
                "Google Inc.","ul. Marszalkowska 1, 01-222 Warszawa","Senior Android Developer","http://facebook.com/jan.kowalski",
                "jan@kowalski","jan.kowalski",null);
    }

    @Test
    public void shouldGetLogoPath(){
        assertTrue("Test OK",card.getLogoImgPath() == null);
    }
    @Test
    public void shouldSetLogoPath(){
        card.setLogoImgPath("/sdcard/logos/logo1.png");
        assertTrue("Test OK",card.getLogoImgPath().equals("/sdcard/logos/logo1.png"));
    }
    @Test
    public void shouldGetName(){
        assertTrue("Test OK",card.getName().equals("Jan Kowalski"));
    }
    @Test
    public void shouldSetName(){
        card.setName("Jan Krzysztof Kowalski");
        assertTrue("Test OK",card.getName().equals("Jan Krzysztof Kowalski"));
    }
    @Test
    public void shouldGetMobile(){
        assertTrue("Test OK",card.getMobile().equals("+48500100100"));
    }
    @Test
    public void shouldSetMobile(){
        card.setMobile("+48666333789");
        assertTrue("Test OK",card.getMobile().equals("+48666333789"));
    }
    @Test
    public void shouldGetPhone(){
        assertTrue("Test OK",card.getPhone().equals("0226669988"));
    }
    @Test
    public void shouldSetPhone(){
        card.setPhone("0227771256");
        assertTrue("Test OK",card.getPhone().equals("0227771256"));
    }
    @Test
    public void shouldGetfax(){
        assertTrue("Test OK",card.getFax() == null);
    }
    @Test
    public void shouldSetFax(){
        card.setFax("0224567878");
        assertTrue("Test OK",card.getFax().equals("0224567878"));
    }
    @Test
    public void shouldGetEmail(){
        assertTrue("Test OK",card.getEmail().equals("jan.kowalski@gmail.com"));
    }
    @Test
    public void shouldSetEmail(){
        card.setEmail("jan.kowalski@yahoo.com");
        assertTrue("Test OK",card.getEmail().equals("jan.kowalski@yahoo.com"));
    }
    @Test
    public void shouldGetWeb(){
        assertTrue("Test OK",card.getWeb().equals("http://www.google.pl"));
    }
    @Test
    public void shouldSetWeb(){
        card.setWeb("http://www.yahoo.com");
        assertTrue("Test OK",card.getWeb().equals("http://www.yahoo.com"));
    }
    @Test
    public void shouldGetCompany(){
        assertTrue("Test OK",card.getCompany().equals("Google Inc."));
    }
    @Test
    public void shouldSetCompany(){
        card.setCompany("Yahoo");
        assertTrue("Test OK",card.getCompany().equals("Yahoo"));
    }
    @Test
    public void shouldGetAddress(){
        assertTrue("Test OK",card.getAddress().equals("ul. Marszalkowska 1, 01-222 Warszawa"));
    }
    @Test
    public void shouldSetAddress(){
        card.setAddress("ul. Ciolka 33, 01-375 Warszawa");
        assertTrue("Test OK",card.getAddress().equals("ul. Ciolka 33, 01-375 Warszawa"));
    }
    @Test
    public void shouldGetJob(){
        assertTrue("Test OK",card.getJob().equals("Senior Android Developer"));
    }
    @Test
    public void shouldSetJob(){
        card.setJob("Java Developer");
        assertTrue("Test OK",card.getJob().equals("Java Developer"));
    }
    @Test
    public void shouldGetFacebook(){
        assertTrue("Test OK",card.getFacebook().equals("http://facebook.com/jan.kowalski"));
    }
    @Test
    public void shouldSetFacebook(){
        card.setFacebook("http://facebook.com/jan.kowalski13");
        assertTrue("Test OK",card.getFacebook().equals("http://facebook.com/jan.kowalski13"));
    }
    @Test
    public void shouldGetTweeter(){
        assertTrue("Test OK",card.getTweeter().equals("jan@kowalski"));
    }
    @Test
    public void shouldSetTweeter(){
        card.setTweeter("jan@kowalski13");
        assertTrue("Test OK",card.getTweeter().equals("jan@kowalski13"));
    }
    @Test
    public void shouldGetSkype(){
        assertTrue("Test OK",card.getSkype().equals("jan.kowalski"));
    }
    @Test
    public void shouldSetSkype(){
        card.setSkype("jan.kowalski13");
        assertTrue("Test OK",card.getSkype().equals("jan.kowalski13"));
    }
    @Test
    public void shouldGetOther(){
        assertTrue("Test OK",card.getOther() == null);
    }
    @Test
    public void shouldSetOther(){
        card.setOther("best friend");
        assertTrue("Test OK",card.getOther().equals("best friend"));
    }
}
