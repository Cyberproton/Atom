package project.cyberproton.atom;

import project.cyberproton.atom.config.atom.ConfigNode;
import project.cyberproton.atom.config.atom.ConfigParser;

import java.util.Map;

public class Test {
    public static void main(String[] args) {
        String s1 = "{}";
        Map<String, ConfigNode> o1 = ConfigParser.parseMap(s1);
        System.out.println(o1);
    }
}
