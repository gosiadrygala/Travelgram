package com.example.travelgram.Models;

import java.util.ArrayList;

public class WeatherResponse {
    private ArrayList<Weather> weather = new ArrayList<>();
    private Main main;
    private Sys sys;

    public ArrayList<Weather> getWeather() {
        return weather;
    }

    public Main getMain() {
        return main;
    }

    public Sys getSys() {
        return sys;
    }



    @Override
    public String toString() {
        return "WeatherResponse{" +
                "weather=" + weather +
                ", main=" + main +
                ", sys=" + sys +
                '}';
    }

    public class Sys {
        private String sunrise;
        private String sunset;

        public Sys(String sunrise, String sunset) {
            this.sunrise = sunrise;
            this.sunset = sunset;
        }

        public String getSunrise() {
            return sunrise;
        }

        public void setSunrise(String sunrise) {
            this.sunrise = sunrise;
        }

        public String getSunset() {
            return sunset;
        }

        public void setSunset(String sunset) {
            this.sunset = sunset;
        }

        @Override
        public String toString() {
            return "Sys{" +
                    "sunrise='" + sunrise + '\'' +
                    ", sunset='" + sunset + '\'' +
                    '}';
        }
    }

    public class Main {
        private double temp;
        private int pressure;

        public Main(double temp, int pressure) {
            this.temp = temp;
            this.pressure = pressure;
        }

        public double getTemp() {
            return temp;
        }

        public void setTemp(double temp) {
            this.temp = temp;
        }

        public int getPressure() {
            return pressure;
        }

        public void setPressure(int pressure) {
            this.pressure = pressure;
        }

        @Override
        public String toString() {
            return "Main{" +
                    "temp='" + temp + '\'' +
                    ", pressure='" + pressure + '\'' +
                    '}';
        }
    }

    public class Weather {
        private String description;

        public Weather(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return "Weather{" +
                    "description='" + description + '\'' +
                    '}';
        }
    }
}
