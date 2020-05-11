//package pl.mini.server;
//
//import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
//import com.carrotsearch.junitbenchmarks.BenchmarkRule;
//import com.carrotsearch.junitbenchmarks.annotation.AxisRange;
//import com.carrotsearch.junitbenchmarks.annotation.BenchmarkHistoryChart;
//import com.carrotsearch.junitbenchmarks.annotation.BenchmarkMethodChart;
//import com.carrotsearch.junitbenchmarks.annotation.LabelType;
//import org.junit.*;
//import org.junit.rules.TestRule;
//import org.slf4j.LoggerFactory;
//import pl.mini.communication.*;
//
//import java.util.UUID;
//import java.util.logging.Logger;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//
//import pl.mini.player.*;
//
//
//public class ServerIntegrateTest{
//
//    public final static int BENCHMARK_ROUNDS = 50;
//    public final static int WARMUP_ROUNDS = 5;
//    public final static int CONCURRENT_BENCHMARK_ROUNDS = 500;
//    public final static int CONCURRENT_WARMUP_ROUNDS = 10;
//
//    private static Thread trd;
//
//
//    public TestRule benchmarkRun = new BenchmarkRule();
//
//
//    public static void setUp() throws InterruptedException {
//        trd = new Thread(() -> {
//            ServerRunner.main(new String[]{"server"});
//        });
//        trd.start();
//        Thread.sleep(10000);
//    }
//
//
//
//    public void playerSequential() throws InterruptedException {
//        tryPlayer();
//        Assert.assertTrue(true);
//    }
//
//
//    public void playerConcurrent() throws InterruptedException {
//        tryPlayer();
//        Assert.assertTrue(true);
//    }
//
//    public void tryPlayer() throws InterruptedException {
//        PlayerCommServer serv = new PlayerCommServer();
//        serv.openConnection();
//        String examp = IntStream.range(0, 1024).mapToObj(String::valueOf).collect(Collectors.joining());
//        serv.evaluate(UUID.randomUUID().toString() + ":" + examp);
//        serv.closeConnection();
//    }
//
//
//    public static void kill() throws InterruptedException {
//        Thread.sleep(1000);
//        trd.interrupt();
//        while(trd.isAlive()){
//            Thread.sleep(500);
//        }
//    }
//
//
//}