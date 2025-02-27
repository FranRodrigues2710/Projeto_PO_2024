package hva.core;


public class Veterinarian extends Employee {
    //Constructor
    public Veterinarian(String id, String n, Hotel h) {
        super(id, n, h);
    }

    //Methods
    @Override
    double calculateSatisfaction(IEmployeeSatisfaction i) {
        return i.calculate(this);
    }

    @Override
    void addResponsibility(String id) {
        responsibilities.add(id);
    }

    @Override
    void removeResponsibility(String id) {
        responsibilities.remove(id);
    }

    public int dano(Vaccine v, Animal a) {
        if (v.getVaccineSpecies().contains(a.getSpecies().getId())) {
            return -1;
        }

        String specieVaccine = v.getVaccineSpecies().get(0);
        for (String s : v.getVaccineSpecies()) {
            if (s.length() > specieVaccine.length()) {
                specieVaccine = s;
            }
        }
        
        return bigger(specieVaccine, a.getSpecies().getId()).length() - sameCharacters(specieVaccine, a.getId());
    }

    int sameCharacters(String str1, String str2) {
        int c = 0;

        for (int i = 0; i < str1.length(); i++) {
            if (str2.indexOf(str1.charAt(i)) >= 0) {
                c += 1;
            }
        }
        return c;
    }

    String bigger(String str1, String str2) {
        if (str1.length() > str2.length()) {
            return str1;
        }
        return str2;
    }

    void vaccinate(Animal a, Vaccine v) {
        VaccinationResults result;
        String toAdd = "REGISTO-VACINA|" + v.getId() + "|" + this.getId() + "|" + a.getSpecies().getId();
        int dano = dano(v, a);
        if (dano == -1) {
            result = VaccinationResults.NORMAL;
        }
        if (dano == 0) {
            result = VaccinationResults.CONFUSION;
        }
        if (0 < dano && dano < 5) {
            result = VaccinationResults.ACCIDENT;
            getHotel().getVaccinesUsed().add(toAdd);
        } else {
            result = VaccinationResults.ERROR;
            getHotel().getVaccinesUsed().add(toAdd);
        }
        VaccineApplication vaccineUsed = new VaccineApplication(result, this, a, v);
        if (vaccineUsed.isCorrect()) {
            a.getHealth().add(result);
            v.addUsage(vaccineUsed);
            workDone.add(toAdd);
            
        }
        
    }

    @Override
    public String toString() {
        if (responsibilities.isEmpty()) {
            return "VET|" + getId() + "|" + getName();
        } else {
            return "VET|" + getId() + "|" + getName() + "|" + responsibilities;
        }
    }
}
