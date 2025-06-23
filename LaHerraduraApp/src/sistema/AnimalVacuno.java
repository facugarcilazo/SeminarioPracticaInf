package sistema;

public class AnimalVacuno extends Animal {

    public AnimalVacuno(int caravana, String sexo, String vacuna, String fechaVacuna,
                        String preñez, String fechaNacimiento, String fechaPreñez) {
        super(caravana, sexo, vacuna, fechaVacuna, preñez, fechaNacimiento, fechaPreñez);
    }

    @Override
    public String resumen() {
        return super.resumen() + " ✅ [Vacuno]";
    }
}
