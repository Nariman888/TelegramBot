package kz.projectTelegaBot.demoproject.CodeSchoolCourseBot.service;

import kz.projectTelegaBot.demoproject.CodeSchoolCourseBot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.xml.stream.events.Comment;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig botConfig;

    public TelegramBot(BotConfig botConfig) {

        this.botConfig = botConfig;
        List<BotCommand> commandList = new ArrayList<>();
        commandList.add(new BotCommand("/start","get a welcome message and menu"));
        commandList.add(new BotCommand("/mydata","получить инфу"));
        commandList.add(new BotCommand("/deletemydata","удалить инфу"));
        commandList.add(new BotCommand("/help","инструкция по боту"));
        commandList.add(new BotCommand("/settings","установка предпочтений"));
        try{
            this.execute(new SetMyCommands(commandList, new BotCommandScopeDefault(),null));
        }
        catch (TelegramApiException e){
            log.error("Error setting bot's command list: " + e.getMessage());
        }


    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId =update.getMessage().getChatId();
            switch (messageText){
                case "/start":

                        startCommandReceived(chatId,update.getMessage().getChat().getFirstName());
                        break;
                default:sendMessage(chatId,"Sorry bro, command wasn't recognized");
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }
    private void startCommandReceived(long chatId,String name){
        String answer = "Здравствуйте, " + name + " рад знакомству!)";
        log.info("Replied to user " + name);
        sendMessage(chatId,answer);
    }
    private void sendMessage(long chatId,String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try{
            execute(message);
        }
        catch (TelegramApiException e){
            log.error("Error occurred:" + e.getMessage());
        }
    }
}
