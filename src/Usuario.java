public class Usuario extends Thread {
    private final int id;
    private final int origen;
    private final int destino;
    private final Ascensor ascensor;

    public Usuario(int id, int origen, int destino, Ascensor ascensor) {
        this.id = id;
        this.origen = origen;
        this.destino = destino;
        this.ascensor = ascensor;
    }

    public int getIdUsuario() {
        return id;
    }

    public int getOrigen() {
        return origen;
    }

    public int getDestino() {
        return destino;
    }

    @Override
    public void run() {
        System.out.println("Usuario " + id + " espera en piso " + origen);
        try {
            ascensor.subir(this);   // espera a que el ascensor llegue a su piso y sube
            ascensor.bajar(this);   // espera a llegar a su destino y baja
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
