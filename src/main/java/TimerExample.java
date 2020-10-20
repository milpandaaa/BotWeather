import database.DatabaseHandler;
import model.DataBaseModel;

import java.sql.SQLException;
import java.util.List;
import java.util.TimerTask;

public class TimerExample extends TimerTask {

    @Override
    public void run() {
        DatabaseHandler databaseHandler = new DatabaseHandler();
        try {
            List<DataBaseModel> users = databaseHandler.mailing();
            Bot bot = new Bot();
            users.forEach(user ->{
                bot.sendWeather(user.getIdUser().toString(), user.getLatitude(), user.getLongitude());
            });
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

    }
}
