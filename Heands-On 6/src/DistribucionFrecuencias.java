import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class DistribucionFrecuencias {

    private ArrayList<Double> datos;
    private int numClases;
    private double rango;
    private double amplitud;
    private HashMap<Double, Intervalo> intervalos;

    public DistribucionFrecuencias(ArrayList<Double> datos, int numClases) {
        this.datos = datos;
        this.numClases = numClases;
        this.intervalos = new HashMap<>();
        calcularRango();
        calcularAmplitud();
        agruparDatos();
    }

    private void calcularRango() {
        this.rango = Collections.max(datos) - Collections.min(datos);
    }

    private void calcularAmplitud() {
        this.amplitud = rango / numClases;
    }

    private void agruparDatos() {
        Collections.sort(datos);
        double inicio = Collections.min(datos);

        for (int i = 0; i < numClases; i++) {
            double fin = inicio + amplitud;
            Intervalo intervalo = new Intervalo(inicio, fin);
            intervalos.put(inicio, intervalo);
            inicio = fin;
        }

        for (Double dato : datos) {
            for (Intervalo intervalo : intervalos.values()) {
                if (dato >= intervalo.getInicio() && dato < intervalo.getFin()) {
                    intervalo.incrementarFrecuencia();
                    break;
                }
            }
        }
    }

    public void mostrarTablaDescriptiva() {
        System.out.println("Tabla Descriptiva");
        System.out.println("Intervalo\tFrecuencia\tPunto Medio\tFrecuencia Acumulada\tFrecuencia Relativa\tFrecuencia Relativa Acumulada\tPorcentaje");

        int frecuenciaAcumulada = 0;
        for (Intervalo intervalo : intervalos.values()) {
            double puntoMedio = (intervalo.getInicio() + intervalo.getFin()) / 2.0;
            double frecuenciaRelativa = (double) intervalo.getFrecuencia() / datos.size();
            frecuenciaAcumulada += intervalo.getFrecuencia();
            double frecuenciaRelativaAcumulada = (double) frecuenciaAcumulada / datos.size();
            double porcentaje = frecuenciaRelativa * 100;

            System.out.printf("%.2f - %.2f\t%d\t%.2f\t%d\t%.2f\t%.2f\t%.2f%%\n",
                    intervalo.getInicio(), intervalo.getFin(), intervalo.getFrecuencia(),
                    puntoMedio, frecuenciaAcumulada, frecuenciaRelativa, frecuenciaRelativaAcumulada, porcentaje);
        }
    }

    public static void main(String[] args) {
        ArrayList<Double> datos = new ArrayList<>();
        // Añade tus datos al ArrayList
        datos.add(10.0);
        datos.add(15.0);
        datos.add(20.0);
        datos.add(25.0);
        datos.add(30.0);

        int numClases = 3;

        DistribucionFrecuencias distribucion = new DistribucionFrecuencias(datos, numClases);
        distribucion.mostrarTablaDescriptiva();
    }
}

class Intervalo {
    private double inicio;
    private double fin;
    private int frecuencia;

    public Intervalo(double inicio, double fin) {
        this.inicio = inicio;
        this.fin = fin;
        this.frecuencia = 0;
    }

    public double getInicio() {
        return inicio;
    }

    public double getFin() {
        return fin;
    }

    public int getFrecuencia() {
        return frecuencia;
    }

    public void incrementarFrecuencia() {
        this.frecuencia++;
    }
}
