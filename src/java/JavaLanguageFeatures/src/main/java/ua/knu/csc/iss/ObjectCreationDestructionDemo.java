package ua.knu.csc.iss;


import java.lang.ref.WeakReference;

class MyObject {
    private MyObjectFinalize objFinalize;
    private MyObjectNoFinalize objNoFinalize;

    public MyObject(String id) {
        objFinalize = new MyObjectFinalize(id);
        objNoFinalize = new MyObjectNoFinalize((id));
    }


    @Override
    public String toString() {
        return "MyObject{" +
                "objFinalize=" + objFinalize.getId() +
                ", objNoFinalize=" + objNoFinalize.getId() +
                '}';
    }

    public WeakReference<MyObjectNoFinalize> getWeakRef() {
        return new WeakReference<>(objNoFinalize);
    }
}

class MyObjectFinalize {
    private String id;

    // Constructor
    public MyObjectFinalize(String id) {
        this.id = id;
        System.out.println("MyObjectFinalize " + id + " created!");
    }

    public String getId() {
        return id;
    }

    // Finalizer (Note: Not recommended for general use)
    @SuppressWarnings({ "removal"}) // removal should be correct, but IntelliJ IDEA still shows error
    @Override
    protected void finalize() throws Throwable {
        System.out.println("MyObjectFinalize " + id + " destroyed!");
        super.finalize();
    }
}

class MyObjectNoFinalize {
    private String id;

    public MyObjectNoFinalize(String id) {
        this.id = id;
        System.out.println("MyObjectNoFinalize " + id + " created!");
    }

    public String getId() {
        return id;
    }
}

public class ObjectCreationDestructionDemo {
    public static void main(String[] args) {
        System.out.println("Starting object creation...");

        // Create new objects with distinct identifiers
        MyObject obj1 = new MyObject("1");
        WeakReference<MyObjectNoFinalize> ref1 = obj1.getWeakRef();
        MyObject obj2 = new MyObject("2");
        WeakReference<MyObjectNoFinalize> ref2 = obj2.getWeakRef();

        // Set obj1 to null, making it eligible for garbage collection
        obj1 = null;
        System.out.println("obj1 is now eligible for garbage collection.");

        // Suggest garbage collection (not guaranteed to run immediately)
        System.gc();

        boolean waitForGC = false;

        if (waitForGC) {
            // Wait a moment to allow for garbage collection
            try {
                Thread.sleep(1000); // Pause for 1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        checkWeakRef(ref1, "1");

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

        checkWeakRef(ref2, "2");

        System.out.println("End of main method.");
    }

    private static void checkWeakRef(WeakReference<MyObjectNoFinalize> ref1, String id) {
        if (ref1.get() == null) {
            System.out.println("After GC: The object has been collected, id=" + id);
        } else {
            System.out.println("After GC: WeakReference.get() = " + ref1.get().getId());
        }
    }


}
