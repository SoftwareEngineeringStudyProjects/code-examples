package ua.knu.csc.iss;


class MyObject {
    private String id;

    // Constructor
    public MyObject(String id) {
        this.id = id;
        System.out.println("MyObject " + id + " created!");
    }

    // Finalizer (Note: Not recommended for general use)
    @SuppressWarnings({"deprecation", "removal"}) // removal should be correct, but IntelliJ IDEA still shows error
    @Override
    protected void finalize() throws Throwable {
        System.out.println("MyObject " + id + " destroyed!");
        super.finalize();
    }
}

public class ObjectCreationDestructionDemo {
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
