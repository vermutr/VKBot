package config;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ApiConfiguration {
    public ApiConfiguration() {
    }

    public TransportClient transportClient() {
        return new HttpTransportClient();
    }

    public VkApiClient vkApiClient(final TransportClient transportClient) {
        return new VkApiClient(transportClient);
    }

    public GroupActor groupActor() {
        return new GroupActor(202763156, "token");
    }

    public Integer ts(final GroupActor groupActor, final VkApiClient client) throws ClientException, ApiException {
        return client.messages().getLongPollServer(groupActor).execute().getTs();
    }

    public Keyboard keyboard() {
        final Keyboard keyboard = new Keyboard();
        final List<KeyboardButton> keyboardButtons = Arrays.asList(
                new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Привет").setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.POSITIVE),
                new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Кто я?").setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.POSITIVE)
        );
        final List<List<KeyboardButton>> allKey = Collections.singletonList(keyboardButtons);
        keyboard.setButtons(allKey);
        return keyboard;
    }
}
