public class Main {
    public static void main(String[] args) throws InterruptedException {
        Ascensor ascensor = new Ascensor();         // una sola instancia compartida

        // Usuario(id, origen, destino, ascensor): los mismos casos del enunciado
        Usuario u1 = new Usuario(1, 0, 3, ascensor);
        Usuario u2 = new Usuario(2, 2, -1, ascensor);
        Usuario u3 = new Usuario(3, 1, 5, ascensor);

        // Primero se registran en la cola, en orden
        ascensor.registrar(u1);
        ascensor.registrar(u2);
        ascensor.registrar(u3);

        // Después se lanzan las hebras
        ascensor.start();
        u1.start();
        u2.start();
        u3.start();

        // Esperar a que todas terminen
        u1.join();
        u2.join();
        u3.join();
        ascensor.join();
    }
}