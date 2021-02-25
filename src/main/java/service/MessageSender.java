package service;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class MessageSender {

    private static final Random random = new Random();

    private final VkApiClient vkApiClient;
    private final Keyboard keyboard;
    private final GroupActor groupActor;
    private final Integer ts;

    public MessageSender(final VkApiClient vkApiClient,
                         final Keyboard keyboard,
                         final GroupActor groupActor,
                         final Integer ts) {
        this.vkApiClient = vkApiClient;
        this.keyboard = keyboard;
        this.groupActor = groupActor;
        this.ts = ts;
    }


    public void sendMessage() throws ClientException, ApiException {
        Integer ts = this.ts;
        while (true) {
            final MessagesGetLongPollHistoryQuery historyQuery = vkApiClient.messages().getLongPollHistory(groupActor).ts(ts);
            final List<Message> messages = historyQuery.execute().getMessages().getItems();
            messages.forEach(consumeMessage());
            ts = vkApiClient.messages().getLongPollServer(groupActor).execute().getTs();
        }
    }

    private Consumer<Message> consumeMessage() {
        return message -> {
            try {
                switch (message.getText()) {
                    case "Привет":
                        vkApiClient.messages().send(groupActor).message("Привет").userId(message.getFromId()).randomId(random.nextInt(10000)).execute();
                        break;
                    case "Как дела?":
                        vkApiClient.messages().send(groupActor).message("Нормально").userId(message.getFromId()).randomId(random.nextInt(10000)).execute();
                        break;
                    case "Кнопки":
                        vkApiClient.messages().send(groupActor).message("Все возможные кнопки").userId(message.getFromId()).randomId(random.nextInt(10000)).keyboard(keyboard).execute();
                        break;
                    default:
                        vkApiClient.messages().send(groupActor).message("Я тебя не понял.").userId(message.getFromId()).randomId(random.nextInt(10000)).execute();
                        break;
                }
            } catch (ApiException | ClientException e) {
                e.printStackTrace();
            }
        };
    }
}
