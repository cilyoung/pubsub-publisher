import com.google.api.core.ApiFuture;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class PubSubPublisher {
    public static void main(String... args) throws Exception {
        String projectId = "nttdata-c4e-bde";
        String topicId = "uc1-input-topic-9";
        String fileName = "/names.txt";

        publisherMessages(projectId, topicId, readFile(fileName));
    }

    private static List<String> readFile(String fileName) throws IOException, URISyntaxException {
        URL resource = PubSubPublisher.class.getResource(fileName);
        List<String> lines = new ArrayList<>();
        if (resource != null) {
            lines = Files.readAllLines(Paths.get(resource.toURI()));
        }
        return lines;
    }

    public static void publisherMessages(String projectId, String topicId, List<String> messages)
            throws IOException, ExecutionException, InterruptedException {
        TopicName topicName = TopicName.of(projectId, topicId);

        Publisher publisher = null;
        try {
            // Create a publisher instance with default settings bound to the topic
            publisher = Publisher.newBuilder(topicName).build();

            for (String message : messages) {
                ByteString data = ByteString.copyFromUtf8(message);
                PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();

                // Once published, returns a server-assigned message id (unique within the topic)
                ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);
                String messageId = messageIdFuture.get();
                System.out.println("Published message ID: " + messageId);
            }
        } finally {
            if (publisher != null) {
                // When finished with the publisher, shutdown to free up resources.
                publisher.shutdown();
                publisher.awaitTermination(1, TimeUnit.MINUTES);
            }
        }
    }
}
