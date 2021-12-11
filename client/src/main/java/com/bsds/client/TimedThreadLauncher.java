import java.util.concurrent.CyclicBarrier;

public class TimedThreadLauncher {
    private int numThreads;
    private int skierIDStart;
    private int skierIDEnd;
    private int numSkiLifts;
    private final String hostname;
    private final int port;
    private int numSkiers;
    private int startTime;
    private int endTime;
    private CyclicBarrier doneBarrier;

    public TimedThreadLauncher(int numThreads, int skierIDStart, int skierIDEnd, int numSkiLifts, 
        String hostname, int port, int startTime, int endTime, CyclicBarrier doneBarrier){
        this.numThreads = numThreads;
        this.skierIDStart = skierIDStart;
        this.skierIDEnd = skierIDEnd;
        this.numSkiLifts = numSkiLifts;
        this.numSkiers = skierIDEnd - skierIDStart + 1;
        this.hostname = hostname;
        this.port = port;
        this.startTime = startTime;
        this.endTime = endTime;
        this.doneBarrier = doneBarrier;
    }

    public void launch(){
        for (int i = 0; i < numThreads; i++){
            int threadIDStart = i * (numSkiers / numThreads) + 1;
            int threadIDEnd = (i + 1) * (numSkiers / numThreads);
            new TimedSkierThread(hostname, port, skierIDStart, threadIDStart, threadIDEnd,
             endTime, numSkiLifts, doneBarrier).start();
        }
    }
    
}