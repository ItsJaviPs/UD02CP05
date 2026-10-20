import java.util.LinkedList;

public class Ascensor extends Thread {
    private int pisoActual = 0;
    private final LinkedList<Usuario> pendientes = new LinkedList<>();
    private Usuario enAtencion = null;     // usuario al que se está atendiendo
    private boolean puedeEntrar = false;   // ascensor parado en el origen del usuario
    private boolean puedeSalir = false;    // ascensor parado en el destino del usuario
    private boolean usuarioDentro = false;

    // Se llama desde el Main, ANTES de lanzar las hebras
    public synchronized void registrar(Usuario u) {
        pendientes.add(u);
    }

    // Lo llama el usuario: espera su turno y sube
    public synchronized void subir(Usuario u) throws InterruptedException {
        while (enAtencion != u || !puedeEntrar) {
            wait();
        }
        puedeEntrar = false;
        usuarioDentro = true;
        System.out.println("Usuario " + u.getIdUsuario() + " sube al ascensor en piso " + pisoActual + ".");
        notifyAll();
    }

    // Lo llama el usuario: espera a llegar a su destino y baja
    public synchronized void bajar(Usuario u) throws InterruptedException {
        while (enAtencion != u || !puedeSalir) {
            wait();
        }
        puedeSalir = false;
        usuarioDentro = false;
        System.out.println("Usuario " + u.getIdUsuario() + " baja en piso " + pisoActual + ".");
        notifyAll();
    }

    @Override
    public void run() {
        try {
            while (true) {
                Usuario u;
                synchronized (this) {
                    u = pendientes.poll();      // siguiente de la cola, o null si no queda nadie
                    enAtencion = u;
                }
                if (u == null) {
                    break;                      // no quedan usuarios pendientes
                }

                // 1. Ir al piso de origen y dejar que el usuario suba
                moverA(u.getOrigen());
                synchronized (this) {
                    puedeEntrar = true;
                    notifyAll();
                    while (!usuarioDentro) {
                        wait();
                    }
                }

                // 2. Llevarlo a su destino y dejar que baje
                moverA(u.getDestino());
                synchronized (this) {
                    puedeSalir = true;
                    notifyAll();
                    while (usuarioDentro) {
                        wait();
                    }
                }
            }

            // 3. Sin usuarios: bajar solo hasta el piso -1 y terminar
            moverA(-1);
            System.out.println("Ascensor llegó a piso -1. Fin del programa.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Mueve el ascensor piso a piso hasta el piso indicado
    private void moverA(int destino) throws InterruptedException {
        while (pisoActual != destino) {
            Thread.sleep(500);
            synchronized (this) {
                pisoActual += (destino > pisoActual) ? 1 : -1;
                System.out.println("Ascensor en piso " + pisoActual);
            }
        }
    }
}