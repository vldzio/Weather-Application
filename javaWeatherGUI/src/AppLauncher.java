import javax.swing.*;
public class AppLauncher {
    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable(){
           @Override
           public void run(){
               new javaWeatherGUI().setVisible(true);
            //System.out.println(weatherApp.getLocationData("Tokyo"));
//               System.out.println(weatherApp.getCurrTime());
           }
        });
    }
}
