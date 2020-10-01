public class Timer implements Runnable{
    private volatile long time;
    private long pause;
    private boolean running;

    static enum timerFunction {START, STOP, RESET}
    public volatile timerFunction timerState;


    private void startTimer(){
        time = System.nanoTime();
        this.running = true;
    }

    private void stopTimer(){
        time = System.nanoTime() - time + pause;
        this.running = false;
    }

    private void resumeTimer(){
        pause = time;
        time = System.nanoTime();
        this.running = true;
    }

    public double getTime(){
        if(running)
            return (((double)System.nanoTime() - time + pause) / 1000000000);
        else
            return ((double)time / 1000000000);
    }

    private void resetTimer() {
        this.time = 0;;
        this.pause = 0;
        this.running = false;
    }

    public Timer(){
        this.time = 0;
        this.timerState = timerFunction.STOP;
        this.running = false;
    }

    @Override
    public void run(){
        while(timerState != timerFunction.START) {}
        while(true) {
            switch (timerState) {
                case START:
                    if (!running && time == 0)
                        startTimer();
                    else
                        if(!running)
                            resumeTimer();
                    break;
                case STOP:
                    if(running)
                        stopTimer();
                    break;
                case RESET:
                    stopTimer();
                    resetTimer();
                    timerState = timerFunction.STOP;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + timerState);
            }
        }
    }
}
