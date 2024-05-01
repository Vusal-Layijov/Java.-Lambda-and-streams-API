package learn;

public class StudentSummary {
    String country;
    String major;
    double iq;

    public StudentSummary(String country, String major, double iq) {
        this.country = country;
        this.major = major;
        this.iq = iq;
    }

    @Override
    public String toString() {
        return "StudentSummary{" +
                "country='" + country + '\'' +
                ", major='" + major + '\'' +
                ", iq=" + iq +
                '}';
    }
}
