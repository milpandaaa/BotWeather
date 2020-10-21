package database;
import model.DataBaseModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends Config {
    Connection dbConnection;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPost + "/" + dbName + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        Class.forName("com.mysql.cj.jdbc.Driver");
        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);
        return dbConnection;
    }

    public void insert(long id, Float lat, Float lon) throws SQLException, ClassNotFoundException {
        String insert = "INSERT INTO subscribes ( idUser, lat, lon) VALUES ("
                + "'" + id + "'," + "'" + lat.toString() + "'," + "'" + lon.toString() + "')";
        PreparedStatement prSt = getDbConnection().prepareStatement(insert);
        prSt.executeUpdate();
    }

    public void update(long id, int subscribe) throws SQLException, ClassNotFoundException {
        String update = "UPDATE subscribes SET subscribe = '"+ subscribe +"' WHERE idUser =" + id;
        PreparedStatement prSt = getDbConnection().prepareStatement(update);
        prSt.executeUpdate();
    }

    public void updateLocation(long id, Float lat, Float lon) throws SQLException, ClassNotFoundException {
        String update = "UPDATE subscribes SET lat = '"+ lat +"', lon = '" + lon +"' WHERE idUser =" + id;
        PreparedStatement prSt = getDbConnection().prepareStatement(update);
        prSt.executeUpdate();
    }

    public boolean checkUser(long id) throws SQLException, ClassNotFoundException {
        String check = "SELECT COUNT(*) FROM subscribes WHERE idUser =" + id;
        Statement statement = getDbConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(check);
        resultSet.next();
        int count = resultSet.getInt(1);
        System.out.println(count);
        return count == 1;
    }

    public List<DataBaseModel> mailing() throws SQLException, ClassNotFoundException {
        String select = "SELECT * FROM subscribes WHERE subscribe = 1 ";

        Statement statement = getDbConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(select);

        List<DataBaseModel> list = new ArrayList<DataBaseModel>();

        while (resultSet.next()){
            list.add(new DataBaseModel(resultSet.getInt(1), resultSet.getFloat(3), resultSet.getFloat(4)));
        }

        return list;
    }
}
