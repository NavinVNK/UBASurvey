package ubasurvey.nawin.com.ubasurvey;

public class ElectricalappDetails {
    public ElectricalappDetails(Integer ubaelecno, String name) {
        this.ubaelecno = ubaelecno;
        this.name = name;
    }

    public Integer getUbaelecno() {
        return ubaelecno;
    }

    public void setUbaelecno(Integer ubaelecno) {
        this.ubaelecno = ubaelecno;
    }


    Integer ubaelecno;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;
}
