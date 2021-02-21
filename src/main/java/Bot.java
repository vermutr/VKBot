import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.*;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bot {
    public static void main(String[] args) throws Exception
    {
        TransportClient transportClient = new HttpTransportClient();
        VkApiClient vk = new VkApiClient(transportClient);
        Random random = new Random();
        Keyboard keyboard = new Keyboard();
        GroupActor actor = new GroupActor(202763156, "token");
        Integer ts = vk.messages().getLongPollServer(actor).execute().getTs();
        List<List<KeyboardButton>> allKey = new ArrayList<>();
        List<KeyboardButton> keyboardButtons = new ArrayList<>();
        keyboardButtons.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Привет").setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.POSITIVE));
        keyboardButtons.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Кто я?").setType(TemplateActionTypeNames .TEXT)).setColor(KeyboardButtonColor.POSITIVE));
        allKey.add(keyboardButtons);
        keyboard.setButtons(allKey);

        while (true){
            MessagesGetLongPollHistoryQuery historyQuery =  vk.messages().getLongPollHistory(actor).ts(ts);
            List<Message> messages = historyQuery.execute().getMessages().getItems();
            if (!messages.isEmpty()){
                messages.forEach(message -> {
                    try {
                        switch (message.getText()) {
                            case "Привет":
                                vk.messages().send(actor).message("Привет").userId(message.getFromId()).randomId(random.nextInt(10000)).execute();
                                break;
                            case "Как дела?":
                                vk.messages().send(actor).message("Нормально").userId(message.getFromId()).randomId(random.nextInt(10000)).execute();
                                break;
                            case "Кнопки":
                                vk.messages().send(actor).message("Все возможные кнопки").userId(message.getFromId()).randomId(random.nextInt(10000)).keyboard(keyboard).execute();
                                break;
                            default:
                                vk.messages().send(actor).message("Я тебя не понял.").userId(message.getFromId()).randomId(random.nextInt(10000)).execute();
                                break;
                        }
                    }
                    catch (ApiException | ClientException e) {
                        e.printStackTrace();
                    }
                });
            }
            ts = vk.messages().getLongPollServer(actor).execute().getTs();
        }
    }
}
