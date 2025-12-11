package com.example.model;

/**
 *
 * @author Binnur Kurt <binnur.kurt@gmail.com>
 */
public class Country {

    private String Code;
    private String Name;
    private String Continent;
    private int IndepYear;
    private int Population;
    private String GovernmentForm;
    private String HeadOfState;

    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public String getContinent() {
        return Continent;
    }

    public void setContinent(String Continent) {
        this.Continent = Continent;
    }

    public String getGovernmentForm() {
        return GovernmentForm;
    }

    public void setGovernmentForm(String GovernmentForm) {
        this.GovernmentForm = GovernmentForm;
    }

    public String getHeadOfState() {
        return HeadOfState;
    }

    public void setHeadOfState(String HeadOfState) {
        this.HeadOfState = HeadOfState;
    }

    public int getIndepYear() {
        return IndepYear;
    }

    public void setIndepYear(int IndepYear) {
        this.IndepYear = IndepYear;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public int getPopulation() {
        return Population;
    }

    public void setPopulation(int Population) {
        this.Population = Population;
    }
}
