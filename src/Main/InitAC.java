package Main;

public class InitAC {
    AutoClicker ac = new AutoClicker();
    InitAC (){
        ac.configGUI();
        ac.setColors();
        ac.addComponents();
        ac.initMainGUI();
    }
}
