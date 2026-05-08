package esngalir.utils;

public class Timer {
    private long startTime;
    private long endTime;
    private boolean running;

    public void start(){
        if (running) {
            throw new IllegalStateException("Timer sudah jalan!");
        }
        startTime = System.currentTimeMillis();
        running = true;
    }

    public void stop(){
        if (!running) {
            throw new IllegalStateException("Timer belum mulai!");
        }
        endTime = System.currentTimeMillis();
        running = false;
    }

    public long getTime(){
        if (running) {
            throw new IllegalStateException("Timer masih berjalan (call stop() dulu)");
        }
        return endTime - startTime;
    }

    public void reset(){
        startTime = 0;
        endTime = 0;
        running = false;
    }
}
