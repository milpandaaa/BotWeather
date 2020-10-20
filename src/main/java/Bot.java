import database.DatabaseHandler;
import model.Answer;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;
import org.telegram.telegrambots.meta.api.objects.stickers.StickerSet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Bot extends TelegramLongPollingBot {


    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());
        }
        catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        String message = update.getMessage().getText();
        if(update.getMessage().getLocation() != null){
            sendWeather(update.getMessage().getChatId().toString(), update.getMessage().getLocation().getLatitude(),
                    update.getMessage().getLocation().getLongitude());

            DatabaseHandler databaseHandler = new DatabaseHandler();
            try {
                if(databaseHandler.checkUser(update.getMessage().getChatId()))
                    databaseHandler.updateLocation(update.getMessage().getChatId(),update.getMessage().getLocation().getLatitude(),
                            update.getMessage().getLocation().getLongitude());
                else
                    databaseHandler.insert(update.getMessage().getChatId(), update.getMessage().getLocation().getLatitude(),
                            update.getMessage().getLocation().getLongitude());
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        }

        if(message != null){
            switch (message){
                case "/help":
                    sendMsg(update.getMessage().getChatId().toString(), "/start");
                    break;
                case "/start":
                    sendMsg(update.getMessage().getChatId().toString(), "Доброе пожаловать в бот!"+'\n'+
                            "Чтобы узнать погоду, нужмите на кнопку 'Узнать погоду' или отправьте команду"+'\n'+"/weather");
                    break;
                case "/subscription":
                case "Подписаться":
                    DatabaseHandler databaseHandler = new DatabaseHandler();
                    try {
                        databaseHandler.update(Math.toIntExact(update.getMessage().getChatId()),1);
                    } catch (SQLException | ClassNotFoundException throwables) {
                        throwables.printStackTrace();
                    }
                    sendMsg(update.getMessage().getChatId().toString(), "Подписка совершена удачно!");
                    break;
                case "/unsubscribe":
                case "Отписаться":
                    DatabaseHandler database = new DatabaseHandler();
                    try {
                        database.update(Math.toIntExact(update.getMessage().getChatId()),0);
                    } catch (SQLException | ClassNotFoundException throwables) {
                        throwables.printStackTrace();
                    }
                    sendMsg(update.getMessage().getChatId().toString(), "Отдписка совершена удачно!");
                    break;
                case "/weather":
                case "Узнать погоду":
                    sendLct(update.getMessage().getChatId().toString(), "Чтобы узнать погоду, отправьте свою геолокацию");
                    break;
                default:
                    sendMsg(update.getMessage().getChatId().toString(), "Вполне возможно, кто ж знает?!");
            }
        }
    }

    public void sendWeather(String idUser, Float latitude, Float longitude) {
        try {
            String response = "";
            WeatherService weatherService = new WeatherService();
            response = weatherService.getWeather(latitude, longitude);
            Answer answer = weatherService.onSuccess(response);
            sendMsg(idUser, answer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String chatId, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);
        try {
            setButtonsKeyboard(sendMessage);
            sendMessage(sendMessage);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendLct(String chatId, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);
        try {
            sendLocationKeyboard(sendMessage);
            sendMessage(sendMessage);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String getBotUsername(){
        return "WeatherLocationEveryDayBot";
    }

    public String getBotToken(){
        return "1299434277:AAEBzRLeLbkokjczCfTzvooE1iYSPB0kWjo";
    }

    public final Message sendMessage(SendMessage sendMessage) throws TelegramApiException{
        if(sendMessage == null)
            throw new TelegramApiException("Parameter sendMessage can not be null");
        else
            return this.sendApiMethod(sendMessage);
    }

    public synchronized void setButtonsKeyboard(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("Узнать погоду"));

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(new KeyboardButton("Подписаться"));
        keyboardSecondRow.add(new KeyboardButton("Отписаться"));

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public synchronized void sendLocationKeyboard(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("Отправить местоположение").setRequestLocation(true));
        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

}

