import view.ViewController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to JIRA!");
        ViewController.run();

//        Pattern pattern = Pattern.compile("user login --username ([^\\s]+) --password ([^\\s]+)");
//        Matcher matcher = pattern.matcher("user login --username idiot --password idiot");
//        matcher.find();
//        System.out.println(matcher.group(1));
//        System.out.println(matcher.group(2));
    }
}
