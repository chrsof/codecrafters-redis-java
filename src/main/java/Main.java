import redis.RedisServer;

public class Main {

    public static void main(String[] args) {
        RedisServer redis = new RedisServer();
        redis.start();
    }

}
