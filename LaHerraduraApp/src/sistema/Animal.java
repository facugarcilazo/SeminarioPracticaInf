//Acá apliqué encapsulamiento, abstracción y constructores segun corrección realizada.
package sistema;

public class Animal {
    private int caravana;
    private String sexo;
    private String vacuna;
    private String fechaVacuna;
    private String preñez;
    private String fechaNacimiento;
    private String fechaPreñez;

    public Animal(int caravana, String sexo, String vacuna, String fechaVacuna,
                  String preñez, String fechaNacimiento, String fechaPreñez) {
        this.caravana = caravana;
        this.sexo = sexo;
        this.vacuna = vacuna;
        this.fechaVacuna = fechaVacuna;
        this.preñez = preñez;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaPreñez = fechaPreñez;
    }
    
    // Getters
    public int getCaravana() { return caravana; }
    public String getSexo() { return sexo; }
    public String getVacuna() { return vacuna; }
    public String getFechaVacuna() { return fechaVacuna; }
    public String getPreñez() { return preñez; }
    public String getFechaNacimiento() { return fechaNacimiento; }
    public String getFechaPreñez() { return fechaPreñez; }

    // Polimórfismo
    public String resumen() {
        return "ID: " + caravana + ", Sexo: " + sexo + ", Vacuna: " + vacuna +
               ", Fecha vacuna: " + fechaVacuna + ", Preñez: " + preñez;
    }
}
