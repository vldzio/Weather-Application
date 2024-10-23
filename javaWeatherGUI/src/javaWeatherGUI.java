import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
public class javaWeatherGUI extends JFrame {
    private JSONObject weatherData;
    public javaWeatherGUI(){
        super ("Weather App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);    //end the program processes once it's closed
        setSize(450,650);   //set size of our GUI
        setLocationRelativeTo(null);    //sets our GUI at the center of the screen
        setLayout(null);    //set's the layout manager to null so that we can manage our layout manually
        setResizable(false);    //prevents any resizes of our GUI
        addGuiComponents();
    }
    private void addGuiComponents(){
        JTextField SearchField = new JTextField();
        SearchField.setBounds(15, 15, 350, 45);
        SearchField.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(SearchField);
        JLabel WeatherCondImage = new JLabel(loadImage("src/assets/cloudy.png"));
        WeatherCondImage.setBounds(0, 125, 450, 215);
        add(WeatherCondImage);
        JLabel setTempText = new JLabel("10 C");
        setTempText.setBounds(0, 350, 450, 54);
        setTempText.setFont(new Font("Dialog", Font.BOLD, 48));
        setTempText.setHorizontalAlignment(SwingConstants.CENTER);
        add(setTempText);
        JLabel weatherCond = new JLabel("Cloudy");
        weatherCond.setBounds(0, 405, 450, 36);
        weatherCond.setFont(new Font("Dialog", Font.PLAIN, 32));
        weatherCond.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherCond);
        JLabel humidityImg = new JLabel(loadImage("src/assets/humidity.png"));
        humidityImg.setBounds(15, 500, 74, 66);
        add(humidityImg);
        JLabel humidityText = new JLabel("<html><b>Humidity</b> 100%</html>");
        humidityText.setBounds(90, 500, 100, 55);
        humidityText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(humidityText);
        JLabel windspeedImage = new JLabel(loadImage("src/assets/windspeed.png"));
        windspeedImage.setBounds(220, 500, 74, 66);
        add(windspeedImage);
        JLabel windspeedText = new JLabel("<html><b>Windspeed</b> 15km/h</html>");
        windspeedText.setBounds(310, 500, 100, 55);
        windspeedText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(windspeedText);
        JButton SearchButton = new JButton(loadImage("src/assets/search.png"));
        SearchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        SearchButton.setBounds(375,13,47,45);
        SearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userInput = SearchField.getText();
                if(userInput.replaceAll("\\s", "").length() <= 0){
                    return;
                }
                weatherData = weatherApp.getWeatherData(userInput);
                String weatherCondition = (String) weatherData.get("weather_Condition");
                switch(weatherCondition){
                    case "Clear":
                        WeatherCondImage.setIcon(loadImage("src/assets/clear.png"));
                        break;
                    case "snow":
                        WeatherCondImage.setIcon(loadImage("src/assets/snow.png"));
                        break;
                    case "Rainy":
                        WeatherCondImage.setIcon(loadImage("src/assets/rain.png"));
                        break;
                    case "Cloudy":
                        WeatherCondImage.setIcon(loadImage("src/assets/cloudy.png"));
                        break;
                }
                double temperature = (double) weatherData.get("temperature");
                setTempText.setText(temperature + "C");
                weatherCond .setText(weatherCondition);
                long humidity = (long) weatherData.get("humidity");
                humidityText.setText("<html><b>Humidity</b><br>"+humidity+"%</html>");
                double windSpeed = (double) weatherData.get("windSpeed");
                windspeedText.setText("<html><b>Windspeed</b><br>"+windSpeed+"km/h</html>");

            }
        });
        add(SearchButton);
    }
    private ImageIcon loadImage(String resourcePath){
        try{
            BufferedImage img = ImageIO.read(new File(resourcePath));
            return new ImageIcon(img);
        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("Couldn't find image");
        return null;
    }
}
