package model;

public class Answer {
    private String descriptionCur;
    private Float tempCur;

    private Float tempDay;
    private Float tempMin;
    private Float tempMax;
    private Float tempNight;
    private Float tempEve;
    private Float tempMorn;

    private String descriptionDay;

    public String getDescriptionCur() {
        return descriptionCur;
    }

    public void setDescriptionCur(String descriptionCur) {
        this.descriptionCur = descriptionCur;
    }

    public Float getTempCur() {
        return tempCur;
    }

    public void setTempCur(Float tempCur) {
        this.tempCur = tempCur;
    }


    public Float getTempDay() {
        return tempDay;
    }

    public void setTempDay(Float tempDay) {
        this.tempDay = tempDay;
    }

    public Float getTempMin() {
        return tempMin;
    }

    public void setTempMin(Float tempMin) {
        this.tempMin = tempMin;
    }

    public Float getTempMax() {
        return tempMax;
    }

    public void setTempMax(Float tempMax) {
        this.tempMax = tempMax;
    }

    public Float getTempNight() {
        return tempNight;
    }

    public void setTempNight(Float tempNight) {
        this.tempNight = tempNight;
    }

    public Float getTempEve() {
        return tempEve;
    }

    public void setTempEve(Float tempEve) {
        this.tempEve = tempEve;
    }

    public Float getTempMorn() {
        return tempMorn;
    }

    public void setTempMorn(Float tempMorn) {
        this.tempMorn = tempMorn;
    }

    public String getDescriptionDay() {
        return descriptionDay;
    }

    public void setDescriptionDay(String descriptionDay) {
        this.descriptionDay = descriptionDay;
    }

    @Override
    public String toString() {
        return "Погода сейчас: \n" +
                "Сейчас " + descriptionCur + '\n' +
                "при температуре " + tempCur + "°."+'\n' +'\n'+
                "Погода на сегодня: \n" +
                "Сегодня будет " + descriptionDay  +","+'\n' +
                "температура утром: " + tempMorn +"°,"+'\n' +
                "температура днем: " + tempDay +"°,"+'\n' +
                "температура вечером: " + tempEve +"°,"+'\n' +
                "температура ночью: " + tempNight +"°,"+'\n' +
                "минимальная температура: " + tempMin +"°,"+'\n' +
                "максимальная температура: " + tempMax + "°.";
    }

    //    @Override
//    public String toString() {
//        return "Погода сегодня: " +'\n' +
//                "" + description + '\n' +
//                "Температура: " + temp + '\n' +
//                "Ощущается как " + feelsLike + '\n' +
//                "Минимальная температура сегодня " + tempMin + '\n' +
//                "Максимальная температура сегодня " + tempMax + '\n' +
//                "Скорость ветра " + speed ;
//    }
}
