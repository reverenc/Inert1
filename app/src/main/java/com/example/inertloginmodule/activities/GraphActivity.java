package com.example.inertloginmodule.activities;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;

import com.db.chart.Tools;
import com.db.chart.model.BarSet;
import com.db.chart.model.LineSet;
import com.db.chart.view.BarChartView;
import com.db.chart.view.ChartView;
import com.db.chart.view.LineChartView;
import com.example.inertloginmodule.R;
import com.example.inertloginmodule.models.Weather;
import com.example.inertloginmodule.tasks.ParseResult;
import com.example.inertloginmodule.utils.UnitConvertor;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;



public class GraphActivity extends BaseActivity {

    private SharedPreferences sp;

    private ArrayList<Weather> weatherList = new ArrayList<>();

    private Paint gridPaint = new Paint() {{
        setStyle(Paint.Style.STROKE);
        setAntiAlias(true);
        setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));
        setStrokeWidth(1);
    }};

    private SimpleDateFormat dateFormat = new SimpleDateFormat("E") {{
        setTimeZone(TimeZone.getDefault());
    }};

    private String labelColor = "#000000";
    private String lineColor = "#333333";
    private String backgroundBarColor = "#000000";

    private boolean darkTheme = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sp = PreferenceManager.getDefaultSharedPreferences(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
//
//
        Toolbar toolbar = findViewById(R.id.graph_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTheme(theme = getTheme(sp.getString("theme", "fresh")));
        darkTheme = theme == R.style.AppTheme_NoActionBar_Dark ||
                theme == R.style.AppTheme_NoActionBar_Black ||
                theme == R.style.AppTheme_NoActionBar_Classic_Dark ||
                theme == R.style.AppTheme_NoActionBar_Classic_Black;

        if (darkTheme) {
            toolbar.setPopupTheme(R.style.AppTheme_PopupOverlay_Dark);
            labelColor = "#FFFFFF";
            lineColor = "#FAFAFA";
            backgroundBarColor = "#FFFFFF";

            TextView temperatureTextView = findViewById(R.id.graph_temperature_textview);
            temperatureTextView.setTextColor(Color.parseColor(labelColor));

            TextView rainTextView = findViewById(R.id.graph_rain_textview);
            rainTextView.setTextColor(Color.parseColor(labelColor));

            TextView pressureTextView = findViewById(R.id.graph_pressure_textview);
            pressureTextView.setTextColor(Color.parseColor(labelColor));

            TextView windSpeedTextView = findViewById(R.id.graph_windspeed_textview);
            windSpeedTextView.setTextColor(Color.parseColor(labelColor));

            TextView humidityTextView = findViewById(R.id.graph_humidity_textview);
            humidityTextView.setTextColor(Color.parseColor(labelColor));
        }

        gridPaint.setColor(Color.parseColor(lineColor));

        String lastLongterm = sp.getString("lastLongterm", "");

        if (parseLongTermJson(lastLongterm) == ParseResult.OK) {
            temperatureGraph();
            rainGraph();
            pressureGraph();
            windSpeedGraph();
            humidityGraph();
        } else {
            Snackbar.make(findViewById(android.R.id.content), R.string.msg_err_parsing_json, Snackbar.LENGTH_LONG).show();
        }
    }

    private void temperatureGraph() {
        LineChartView lineChartView = findViewById(R.id.graph_temperature);

        float minTemp = 1000;
        float maxTemp = -1000;

        LineSet lineDataset = new LineSet();
        for (int i = 0; i < weatherList.size(); i++) {
            float temperature = UnitConvertor.convertTemperature(Float.parseFloat(weatherList.get(i).getTemperature()), sp);

            minTemp = Math.min(temperature, minTemp);
            maxTemp = Math.max(temperature, maxTemp);

            lineDataset.addPoint(getDateLabel(weatherList.get(i), i), temperature);
        }
        lineDataset.setSmooth(false);
        lineDataset.setColor(Color.parseColor("#FF5722"));
        lineDataset.setThickness(4);

        int middle = Math.round(minTemp + (maxTemp - minTemp) / 2);
        int stepSize = (int) Math.ceil(Math.abs(maxTemp - minTemp) / 4);
        int min = middle - 2 * stepSize;
        int max = middle + 2 * stepSize;

        lineChartView.addData(lineDataset);
        lineChartView.setGrid(ChartView.GridType.HORIZONTAL, 4, 1, gridPaint);
        lineChartView.setAxisBorderValues(min, max);
        lineChartView.setStep(stepSize);
        lineChartView.setLabelsColor(Color.parseColor(labelColor));
        lineChartView.setXAxis(false);
        lineChartView.setYAxis(false);
        lineChartView.setBorderSpacing(Tools.fromDpToPx(10));
        lineChartView.show();

        BarChartView backgroundChartView = getBackgroundBarChart(R.id.graph_temperature_background, min, max, false);
        backgroundChartView.show();

        TextView textView = findViewById(R.id.graph_temperature_textview);
        textView.setText(String.format("%s (%s)", getString(R.string.temperature), sp.getString("unit", "°C")));
    }

    private void rainGraph() {
        BarChartView barChartView = findViewById(R.id.graph_rain);

        float maxRain = 1;

        BarSet dataset = new BarSet();
        for (int i = 0; i < weatherList.size(); i++) {
            float rain = UnitConvertor.convertRain(Float.parseFloat(weatherList.get(i).getRain()), sp);

            maxRain = Math.max(rain, maxRain);

            dataset.addBar(getDateLabel(weatherList.get(i), i), rain);
        }
        dataset.setColor(Color.parseColor("#2196F3"));

        int stepSize = 1;
        if (maxRain > 6) {
            maxRain = (float) Math.ceil(maxRain / 6) * 6;
            stepSize = (int) Math.ceil(maxRain / 6);
        } else {
            maxRain = (float) Math.ceil(maxRain);
        }
        int max = (int) maxRain;

        barChartView.addData(dataset);
        barChartView.setGrid(ChartView.GridType.HORIZONTAL, max / stepSize, 1, gridPaint);
        barChartView.setAxisBorderValues(0, (int) Math.ceil(maxRain));
        barChartView.setStep(stepSize);
        barChartView.setLabelsColor(Color.parseColor(labelColor));
        barChartView.setXAxis(false);
        barChartView.setYAxis(false);
        barChartView.setBorderSpacing(Tools.fromDpToPx(10));
        barChartView.show();

        BarChartView backgroundChartView = getBackgroundBarChart(R.id.graph_rain_background, 0, max, true);
        backgroundChartView.show();

        TextView textView = findViewById(R.id.graph_rain_textview);
        textView.setText(String.format("%s (%s)", getString(R.string.rain), sp.getString("lengthUnit", "mm")));
    }

    private void pressureGraph() {
        LineChartView lineChartView = findViewById(R.id.graph_pressure);

        float minPressure = 100000;
        float maxPressure = 0;

        LineSet dataset = new LineSet();
        for (int i = 0; i < weatherList.size(); i++) {
            float pressure = UnitConvertor.convertPressure(Float.parseFloat(weatherList.get(i).getPressure()), sp);

            minPressure = Math.min(pressure, minPressure);
            maxPressure = Math.max(pressure, maxPressure);

            dataset.addPoint(getDateLabel(weatherList.get(i), i), pressure);
        }
        dataset.setSmooth(false);
        dataset.setColor(Color.parseColor("#4CAF50"));
        dataset.setThickness(4);

        int middle = Math.round(minPressure + (maxPressure - minPressure) / 2);
        int stepSize = (int) Math.ceil(Math.abs(maxPressure - minPressure) / 4);
        int min = middle - 2 * stepSize;
        int max = middle + 2 * stepSize;

        lineChartView.addData(dataset);
        lineChartView.setGrid(ChartView.GridType.HORIZONTAL, 4, 1, gridPaint);
        lineChartView.setAxisBorderValues(min, max);
        lineChartView.setStep(stepSize);
        lineChartView.setLabelsColor(Color.parseColor(labelColor));
        lineChartView.setXAxis(false);
        lineChartView.setYAxis(false);
        lineChartView.setBorderSpacing(Tools.fromDpToPx(10));
        lineChartView.show();

        BarChartView barChartView = getBackgroundBarChart(R.id.graph_pressure_background, min, max, false);
        barChartView.show();

        TextView textView = findViewById(R.id.graph_pressure_textview);
        textView.setText(String.format("%s (%s)", getString(R.string.pressure), sp.getString("pressureUnit", "hPa")));
    }

    private void windSpeedGraph() {
        LineChartView lineChartView = findViewById(R.id.graph_windspeed);
        String graphLineColor = "#efd214";

        float maxWindSpeed = 1;

        if (darkTheme) {
            graphLineColor = "#FFF600";
        }

        LineSet dataset = new LineSet();
        for (int i = 0; i < weatherList.size(); i++) {
            float windSpeed = (float) UnitConvertor.convertWind(Float.parseFloat(weatherList.get(i).getWind()), sp);

            maxWindSpeed = Math.max(windSpeed, maxWindSpeed);

            dataset.addPoint(getDateLabel(weatherList.get(i), i), windSpeed);
        }
        dataset.setSmooth(false);
        dataset.setColor(Color.parseColor(graphLineColor));
        dataset.setThickness(4);

        int stepSize = 1;
        if (maxWindSpeed > 6) {
            maxWindSpeed = (float) Math.ceil(maxWindSpeed / 6) * 6;
            stepSize = (int) Math.ceil(maxWindSpeed / 6);
        } else {
            maxWindSpeed = (float) Math.ceil(maxWindSpeed);
        }
        int max = (int) maxWindSpeed;

        lineChartView.addData(dataset);
        lineChartView.setGrid(ChartView.GridType.HORIZONTAL, max / stepSize, 1, gridPaint);
        lineChartView.setAxisBorderValues(0, (int) maxWindSpeed);
        lineChartView.setStep(stepSize);
        lineChartView.setLabelsColor(Color.parseColor(labelColor));
        lineChartView.setXAxis(false);
        lineChartView.setYAxis(false);
        lineChartView.setBorderSpacing(Tools.fromDpToPx(10));
        lineChartView.show();

        BarChartView barChartView = getBackgroundBarChart(R.id.graph_windspeed_background, 0, max, false);
        barChartView.show();

        TextView textView = findViewById(R.id.graph_windspeed_textview);
        textView.setText(String.format("%s (%s)", getString(R.string.wind_speed), sp.getString("speedUnit", "m/s")));
    }

    private void humidityGraph() {
        LineChartView lineChartView = findViewById(R.id.graph_humidity);

        float minHumidity = 100000;
        float maxHumidity = 0;

        LineSet dataset = new LineSet();
        for (int i = 0; i < weatherList.size(); i++) {
            float humidity = Float.parseFloat(weatherList.get(i).getHumidity());

            minHumidity = Math.min(humidity, minHumidity);
            maxHumidity = Math.max(humidity, maxHumidity);

            dataset.addPoint(getDateLabel(weatherList.get(i), i), humidity);
        }
        dataset.setSmooth(false);
        dataset.setColor(Color.parseColor("#2196F3"));
        dataset.setThickness(4);

        int min = (int) minHumidity / 10 * 10;
        int max = (int) Math.ceil(maxHumidity / 10) * 10;
        int stepSize = (max - min == 10) ? 20 : 10;

        lineChartView.addData(dataset);
        lineChartView.setGrid(ChartView.GridType.HORIZONTAL, (max - min) / stepSize, 1, gridPaint);
        lineChartView.setAxisBorderValues(min, max);
        lineChartView.setStep(stepSize);
        lineChartView.setLabelsColor(Color.parseColor(labelColor));
        lineChartView.setXAxis(false);
        lineChartView.setYAxis(false);
        lineChartView.setBorderSpacing(Tools.fromDpToPx(10));
        lineChartView.show();

        BarChartView barChartView = getBackgroundBarChart(R.id.graph_humidity_background, min, max, false);
        barChartView.show();

        TextView textView = findViewById(R.id.graph_humidity_textview);
        textView.setText(String.format("%s (%s)", getString(R.string.humidity), "%"));
    }

    public ParseResult parseLongTermJson(String result) {
        try {
            JSONObject reader = new JSONObject(result);

            final String code = reader.optString("cod");
            if ("404".equals(code)) {
                return ParseResult.CITY_NOT_FOUND;
            }

            JSONArray list = reader.getJSONArray("list");
            for (int i = 0; i < list.length(); i++) {
                Weather weather = new Weather();

                JSONObject listItem = list.getJSONObject(i);
                JSONObject main = listItem.getJSONObject("main");

                JSONObject windObj = listItem.optJSONObject("wind");
                weather.setWind(windObj.getString("speed"));

                weather.setPressure(main.getString("pressure"));
                weather.setHumidity(main.getString("humidity"));

                JSONObject rainObj = listItem.optJSONObject("rain");
                JSONObject snowObj = listItem.optJSONObject("snow");
                if (rainObj != null) {
                    weather.setRain(MainActivity.getRainString(rainObj));
                } else {
                    weather.setRain(MainActivity.getRainString(snowObj));
                }

                weather.setDate(listItem.getString("dt"));
                weather.setTemperature(main.getString("temp"));

                weather.setHumidity(main.getString("humidity"));

                weatherList.add(weather);
            }
        } catch (JSONException e) {
            Log.e("JSONException Data", result);
            e.printStackTrace();
            return ParseResult.JSON_EXCEPTION;
        }

        return ParseResult.OK;
    }

    /**
     * Returns a label for the dates, only one per day preferably at noon.
     * @param weather weather entity
     * @param i number of weather in long term forecast
     * @return label (either short form of day in week or empty string)
     */
    private String getDateLabel(Weather weather, int i) {
        String output = dateFormat.format(weather.getDate());

        // label for first day if it starts after 13:00
        if (i == 0 && weather.getDate().getHours() > 13) {
            return output;
        }
        // label for the last day if it ends before 11:00
        else if (i == weatherList.size() - 1 && weather.getDate().getHours() < 11) {
            return output;
        }
        // label in the middle of the day at 11:00 / 12:00 / 13:00 for all other days
        else if (weather.getDate().getHours() >= 11 && weather.getDate().getHours() <= 13) {
            return output;
        }
        // normal case: no date label
        else {
            return "";
        }
    }

    /**
     * Returns a background chart with alternating vertical bars for each day.
     * @param id BarChartView resource id
     * @param min foreground chart min label
     * @param max foreground chart max label
     * @param includeLast true for foreground bar charts, false for foreground line charts
     * @return background bar chart
     */
    private BarChartView getBackgroundBarChart(@IdRes int id, int min, int max, boolean includeLast) {
        boolean visible = false;
        int lastHour = 25;

        // get label with biggest visual length
        if (getLengthAsString(min) > getLengthAsString(max)) {
            max = min;
        }

        BarSet barDataset = new BarSet();
        for (int i = 0; i < weatherList.size(); i++) {
            if (i != weatherList.size() - 1 || includeLast) {
                for (int j = 0; j < 3; j++) {
                    int hour = (weatherList.get(i).getDate().getHours() + j) % 24;

                    // 23:00 to 0:00 new day
                    if (hour < lastHour) {
                        visible = !visible;
                    }

                    barDataset.addBar("", visible ? max : 0);
                    lastHour = hour;
                }
            }
        }
        barDataset.setColor(Color.parseColor(backgroundBarColor));
        barDataset.setAlpha(0.075f);

        BarChartView barChartView = findViewById(id);
        barChartView.addData(barDataset);
        barChartView.setBarSpacing(0); // visually join bars into on bar per day
        barChartView.setAxisBorderValues(Math.min(0, max), Math.max(0, max));
        barChartView.setLabelsColor(Color.parseColor("#00ffffff")); // fully transparent (= invisible) labels
        barChartView.setXAxis(false);
        barChartView.setYAxis(false);
        barChartView.setBorderSpacing(Tools.fromDpToPx(10));

        return barChartView;
    }

    /**
     * Returns a comparable abstract length/width an integer number uses as a chart label (works best for fonts with monospaced digits).
     * @param i number
     * @return length
     */
    private int getLengthAsString(int i) {
        char[] array = String.valueOf(i).toCharArray();
        int sum = 0;
        for (char c : array) {
            sum += (c == '-') ? 1 : 2; // minus is smaller than digits
        }
        return sum;
    }

    private int getTheme(String themePref) {
        switch (themePref) {
            case "dark":
                return R.style.AppTheme_NoActionBar_Dark;
            case "black":
                return R.style.AppTheme_NoActionBar_Black;
            case "classic":
                return R.style.AppTheme_NoActionBar_Classic;
            case "classicdark":
                return R.style.AppTheme_NoActionBar_Classic_Dark;
            case "classicblack":
                return R.style.AppTheme_NoActionBar_Classic_Black;
            default:
                return R.style.AppTheme_NoActionBar;
        }
    }
}
