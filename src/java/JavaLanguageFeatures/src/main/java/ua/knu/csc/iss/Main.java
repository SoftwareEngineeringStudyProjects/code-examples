package ua.knu.csc.iss;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.printf("Hello and welcome!");

        for (int i = 1; i <= 5; i++) {
            //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
            // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
            System.out.println("i = " + i);
        }
        ObjectCreationDestructionDemo.main(args);
    }
}

class MyObject {
    private String id;

    // Constructor
    public MyObject(String id) {
        this.id = id;
        System.out.println("MyObject " + id + " created!");
    }

    // Finalizer (Note: Not recommended for general use)
    @Override
    protected void finalize() throws Throwable {
        System.out.println("MyObject " + id + " destroyed!");
        super.finalize();
    }
}

class ObjectCreationDestructionDemo {
    public static void main(String[] args) {
        System.out.println("Starting object creation...");

        // Create new objects with distinct identifiers
        MyObject obj1 = new MyObject("1");
        MyObject obj2 = new MyObject("2");

        // Set obj1 to null, making it eligible for garbage collection
        obj1 = null;
        System.out.println("obj1 is now eligible for garbage collection.");

        // Suggest garbage collection (not guaranteed to run immediately)
        System.gc();

        // Wait a moment to allow for garbage collection
        try {
            Thread.sleep(1000); // Pause for 1 second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Set obj2 to null, making it also eligible for garbage collection
        obj2 = null;
        System.out.println("obj2 is now eligible for garbage collection.");

        // Suggest garbage collection again
        System.gc();

        // Wait again for garbage collection
        try {
            Thread.sleep(1000); // Pause for 1 second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("End of main method.");
    }
}
