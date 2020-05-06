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
//@AxisRange(min = 0, max = 1)
//@BenchmarkMethodChart(filePrefix = "server")
//@BenchmarkHistoryChart(labelWith = LabelType.CUSTOM_KEY,maxRuns = 20,filePrefix = "hx-server")
//public class ServerIntegrateTest{
//
//    public final static int BENCHMARK_ROUNDS = 50;
//    public final static int WARMUP_ROUNDS = 5;
//    public final static int CONCURRENT_BENCHMARK_ROUNDS = 500;
//    public final static int CONCURRENT_WARMUP_ROUNDS = 10;
//
//    private static Thread trd;
//
//    @Rule
//    public TestRule benchmarkRun = new BenchmarkRule();
//
//    @BeforeClass
//    public static void setUp() throws InterruptedException {
//        trd = new Thread(() -> {
//            ServerRunner.main(new String[]{"server"});
//        });
//        trd.start();
//        Thread.sleep(10000);
//    }
//
//    @BenchmarkOptions(benchmarkRounds = BENCHMARK_ROUNDS, warmupRounds = WARMUP_ROUNDS,
//                      concurrency = BenchmarkOptions.CONCURRENCY_SEQUENTIAL)
//    @Test
//    public void playerSequential() throws InterruptedException {
//        tryPlayer();
//        Assert.assertTrue(true);
//    }
//
//    @BenchmarkOptions(benchmarkRounds = CONCURRENT_BENCHMARK_ROUNDS, warmupRounds = CONCURRENT_WARMUP_ROUNDS,
//                      concurrency = BenchmarkOptions.CONCURRENCY_AVAILABLE_CORES)
//    @Test
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
//    @AfterClass
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