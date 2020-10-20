package model;

public class DataBaseModel {
    Integer idUser;
    Float latitude, longitude;

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public DataBaseModel(Integer idUser, Float latitude, Float longitude) {
        this.idUser = idUser;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
