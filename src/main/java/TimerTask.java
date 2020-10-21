import database.DatabaseHandler;
import model.DataBaseModel;

import java.sql.SQLException;
import java.util.List;

public class TimerTask extends java.util.TimerTask {

    @Override
    public void run() {
        DatabaseHandler databaseHandler = new DatabaseHandler();
        try {
            databaseHandler.getDbConnection();
            List<DataBaseModel> users = databaseHandler.mailing();
            Bot bot = new Bot();
            users.forEach(user -> bot.sendWeather(user.getIdUser().toString(), user.getLatitude(), user.getLongitude()));
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }

    }
}
