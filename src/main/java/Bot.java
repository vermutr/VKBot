import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import config.ApiConfiguration;
import service.MessageSender;

public class Bot {
    public static void main(String[] args) throws Exception {
        final ApiConfiguration apiConfiguration = new ApiConfiguration();
        final GroupActor groupActor = apiConfiguration.groupActor();
        final TransportClient transportClient = apiConfiguration.transportClient();
        final VkApiClient vkApiClient = apiConfiguration.vkApiClient(transportClient);

        final MessageSender messageSender = new MessageSender(
                vkApiClient,
                apiConfiguration.keyboard(),
                groupActor,
                apiConfiguration.ts(groupActor, vkApiClient)
        );

        messageSender.sendMessage();
    }
}
